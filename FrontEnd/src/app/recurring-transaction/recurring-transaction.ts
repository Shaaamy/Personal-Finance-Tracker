import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../services/auth';

export interface RecurringTransaction {
  name: string;
  amount: number;
  category: string;
  frequency: string;
  nextOccurrence: string;
}

@Component({
  selector: 'app-recurring-transaction',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './recurring-transaction.html',
  styleUrls: ['./recurring-transaction.css']
})
export class RecurringTransactionComponent {
  isBrowser = false;
  showForm = false;
  isEdit = false;
  editIndex: number | null = null;

  // 🆕 Filters
  filterDate = '';
  filterCategory = '';
  filterType = '';
  filterCurrency = '';
  filterExtra = '';

  searchTerm = '';
  filteredTransactions: RecurringTransaction[] = [];

  transactions: RecurringTransaction[] = [
    { name: 'Monthly Rent', amount: 1500, category: 'Housing', frequency: 'Monthly', nextOccurrence: '2025-10-01' },
    { name: 'Gym Membership', amount: 45.99, category: 'Health', frequency: 'Monthly', nextOccurrence: '2025-09-20' },
    { name: 'Netflix', amount: 19.99, category: 'Entertainment', frequency: 'Monthly', nextOccurrence: '2025-09-25' },
    { name: 'Weekly Groceries', amount: 120, category: 'Food', frequency: 'Weekly', nextOccurrence: '2025-09-18' },
    { name: 'Annual License', amount: 299, category: 'Utilities', frequency: 'Annual', nextOccurrence: '2025-12-30' }
  ];

  newTransaction: RecurringTransaction = { name: '', amount: 0, category: '', frequency: 'Monthly', nextOccurrence: '' };

  constructor(@Inject(PLATFORM_ID) platformId: Object, private router: Router, private auth: Auth) {
    this.isBrowser = isPlatformBrowser(platformId);
    this.filteredTransactions = [...this.transactions];
  }

  // Totals
  get dailyTotal()      { return this.totalBy('Daily'); }
  get weeklyTotal()     { return this.totalBy('Weekly'); }
  get monthlyTotal()    { return this.totalBy('Monthly'); }
  get halfAnnualTotal() { return this.totalBy('Half-Annual'); }
  get annualTotal()     { return this.totalBy('Annual'); }

  private totalBy(freq: string) {
    return this.transactions
      .filter(t => t.frequency.toLowerCase() === freq.toLowerCase())
      .reduce((sum, t) => sum + t.amount, 0);
  }

  // Filters & Search
  applyFilters() {
    const term = this.searchTerm.toLowerCase();
    this.filteredTransactions = this.transactions.filter(t => {
      const matchesSearch =
        t.name.toLowerCase().includes(term) ||
        t.category.toLowerCase().includes(term) ||
        t.frequency.toLowerCase().includes(term) ||
        t.amount.toString().includes(term);

      const matchesCategory = !this.filterCategory || t.category === this.filterCategory;
      const matchesType = !this.filterType || (this.filterType === 'Income' ? t.amount >= 0 : t.amount < 0);
      const matchesCurrency = !this.filterCurrency || true; // adapt if you add currency property
      const matchesExtra =
        !this.filterExtra ||
        this.filterExtra === 'All Categories' ||
        (this.filterExtra === 'Name' && t.name) ||
        (this.filterExtra === 'Frequency' && t.frequency);

      // Date filtering left as placeholder
      return matchesSearch && matchesCategory && matchesType && matchesCurrency && matchesExtra;
    });
  }

  addTransaction() {
    this.showForm = true;
    this.isEdit = false;
    this.newTransaction = { name: '', amount: 0, category: '', frequency: 'Monthly', nextOccurrence: '' };
  }

  saveTransaction() {
    if (this.isEdit && this.editIndex !== null) {
      this.transactions[this.editIndex] = { ...this.newTransaction };
    } else {
      this.transactions.unshift({ ...this.newTransaction });
    }
    this.applyFilters();
    this.showForm = false;
    this.isEdit = false;
    this.editIndex = null;
  }

  editTransaction(index: number) {
    this.showForm = true;
    this.isEdit = true;
    const realIndex = this.transactions.indexOf(this.filteredTransactions[index]);
    this.editIndex = realIndex;
    this.newTransaction = { ...this.transactions[realIndex] };
  }

  deleteTransaction(index: number) {
    const realIndex = this.transactions.indexOf(this.filteredTransactions[index]);
    if (realIndex > -1) this.transactions.splice(realIndex, 1);
    this.applyFilters();
  }

  cancelForm() { this.showForm = false; this.isEdit = false; }

  // Navigation
  goHome()        { this.router.navigate(['/home']); }
  goToTransaction(){ this.router.navigate(['/transaction']); }
  goToRecurring() { this.router.navigate(['/recurring-transaction']); }
  goToContact()   { this.router.navigate(['/contect-us']); }
  goToAbout()     { this.router.navigate(['/about']); }
  goTowelcome()   { this.router.navigate(['/login']); }
}
