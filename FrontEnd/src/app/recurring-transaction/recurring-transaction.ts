import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
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
  isBrowser: boolean;
  showForm = false;
  isEdit = false;
  editIndex: number | null = null;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router,
    private authService: Auth
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  transactions: RecurringTransaction[] = [
    { name: 'Monthly Rent', amount: 1500.00, category: 'Housing', frequency: 'Monthly', nextOccurrence: '2024-07-01' },
    { name: 'Gym Membership', amount: 45.99, category: 'Health', frequency: 'Monthly', nextOccurrence: '2024-06-15' },
    { name: 'Netflix Subscription', amount: 19.99, category: 'Entertainment', frequency: 'Monthly', nextOccurrence: '2024-06-25' },
    { name: 'Weekly Groceries Budget', amount: 120.00, category: 'Food', frequency: 'Weekly', nextOccurrence: '2024-06-10' },
    { name: 'Annual Software License', amount: 299.00, category: 'Utilities', frequency: 'Yearly', nextOccurrence: '2024-08-07' },
  ];

  newTransaction: RecurringTransaction = {
    name: '',
    amount: 0,
    category: '',
    frequency: 'Monthly',
    nextOccurrence: ''
  };

  get totalMonthlyExpenses(): number {
    return this.transactions.reduce((sum, t) => {
      if (t.frequency === 'Monthly') {
        return sum + t.amount;
      } else if (t.frequency === 'Weekly') {
        return sum + t.amount * 4;
      } else if (t.frequency === 'Yearly') {
        return sum + t.amount / 12;
      }
      return sum;
    }, 0);
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
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
    this.showForm = false;
    this.isEdit = false;
    this.editIndex = null;
  }

  editTransaction(index: number) {
    this.showForm = true;
    this.isEdit = true;
    this.editIndex = index;
    this.newTransaction = { ...this.transactions[index] };
  }

  deleteTransaction(index: number) {
    this.transactions.splice(index, 1);
  }

  cancelForm() {
    this.showForm = false;
    this.isEdit = false;
  }

  goHome() { this.router.navigate(['/home']); }
  goToTransaction() { this.router.navigate(['/transaction']); }
  goToRecurring() { this.router.navigate(['/recurring-transaction']); }
  goToDashboard() { this.router.navigate(['/dashboard']); }
  goToContact() { this.router.navigate(['/contect-us']); }
  goToAbout() { this.router.navigate(['/about']); }
  goTowelcome() { this.router.navigate(['/welcome']); }
}
