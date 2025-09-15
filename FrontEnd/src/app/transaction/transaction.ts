import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../services/auth';

export interface Transaction {
  date: string;
  description: string;
  currency: string;
  categoryName: string;
  type: string;
  amount: number;
}

@Component({
  selector: 'app-transaction',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './transaction.html',
  styleUrls: ['./transaction.css']
})
export class TransactionComponent {
  transactionform = new FormGroup({
    amount: new FormControl('', Validators.required),
    type: new FormControl('Expense', Validators.required),
    currency: new FormControl('EGP', Validators.required),
    categoryName: new FormControl('', Validators.required),
    date: new FormControl('', Validators.required),
    description: new FormControl('', Validators.required)
  });

  isBrowser: boolean;
  showForm = false;
  isEdit = false;
  editIndex: number | null = null;

  // Filters
  filterDate = '';
  filterCategory = '';
  filterType = '';
  filterCurrency = '';
  filterText = '';
  filteredTransactions: Transaction[] = [];

  // Budget
  budget = 5000;
  remainingBudget = 5000;
  budgetWarning = false;
  budgetExceeded = false;

  transactions: Transaction[] = [
    { date: '2023-10-26', description: 'Groceries', currency: 'EGP', categoryName: 'Food', type: 'Expense', amount: 54.20 },
    { date: '2023-10-25', description: 'Monthly Salary', currency: 'EGP', categoryName: 'Work', type: 'Income', amount: 3200.00 },
  ];

  newTransaction: Transaction = {
    date: '', description: '', currency: 'EGP', categoryName: '', type: 'Expense', amount: 0
  };

  constructor(@Inject(PLATFORM_ID) private platformId: Object,
              private router: Router,
              private authService: Auth) {
    this.isBrowser = isPlatformBrowser(this.platformId);
    this.filteredTransactions = [...this.transactions];
    this.updateBudget();
  }

  // Totals
  get totalIncome(): number {
    return this.transactions.filter(t => t.type === 'Income')
                            .reduce((s, t) => s + t.amount, 0);
  }
  get totalExpense(): number {
    return this.transactions.filter(t => t.type === 'Expense')
                            .reduce((s, t) => s + t.amount, 0);
  }
  get netBalance(): number {
    return this.totalIncome - this.totalExpense;
  }
  get status(): string {
    return this.netBalance >= 0 ? 'Profit' : 'Loss';
  }

  addTransaction() {
    this.showForm = true;
    this.isEdit = false;
    this.newTransaction = { date: '', description: '', currency: 'EGP', categoryName: '', type: 'Expense', amount: 0 };
    this.transactionform.reset({ type: 'Expense', currency: 'EGP' });
  }

  saveTransaction(form: FormGroup) {
    if (form.invalid) return;

    const temp = { ...this.newTransaction };
    // تحقق من تجاوز الميزانية إذا كانت معاملة مصروف
    if (temp.type === 'Expense' && (this.totalExpense + temp.amount) > this.budget) {
      this.budgetExceeded = true;
      return;
    }
    this.budgetExceeded = false;

    if (this.isEdit && this.editIndex !== null) {
      this.transactions[this.editIndex] = temp;
    } else {
      this.transactions.unshift(temp);
    }

    this.applyFilters();
    this.showForm = false;
    this.isEdit = false;
    this.editIndex = null;
    this.updateBudget();
  }

  editTransaction(index: number) {
    this.showForm = true;
    this.isEdit = true;
    this.editIndex = index;
    this.newTransaction = { ...this.transactions[index] };
    this.transactionform.setValue({
      ...this.transactions[index],
      amount: this.transactions[index].amount.toString()
    });
  }

  deleteTransaction(index: number) {
    this.transactions.splice(index, 1);
    this.applyFilters();
    this.updateBudget();
  }

  cancelForm() {
    this.showForm = false;
    this.isEdit = false;
    this.editIndex = null;
    this.budgetExceeded = false;
  }

  // فلترة شاملة
  applyFilters() {
    this.filteredTransactions = this.transactions.filter(t => {
      const textMatch = this.filterText
        ? Object.values(t).some(val =>
            val.toString().toLowerCase().includes(this.filterText.toLowerCase()))
        : true;

      const categoryMatch = !this.filterCategory || t.categoryName === this.filterCategory;
      const typeMatch = !this.filterType || t.type === this.filterType;
      const currencyMatch = !this.filterCurrency || t.currency === this.filterCurrency;

      return textMatch && categoryMatch && typeMatch && currencyMatch;
    });
  }

  updateBudget() {
    const expenses = this.transactions.filter(t => t.type === 'Expense')
                                      .reduce((s, t) => s + t.amount, 0);
    this.remainingBudget = this.budget - expenses;
    this.budgetWarning = this.remainingBudget <= (this.budget * 0.1) || this.remainingBudget <= 0;

    if (this.budgetWarning && this.isBrowser && Notification.permission === 'granted') {
      new Notification('Budget Alert', { body: 'Your budget is running low or finished!' });
    }
  }

  // Navigation
  goHome() { this.router.navigate(['/home']); }
  goToTransaction() { this.router.navigate(['/transaction']); }
  goToRecurring() { this.router.navigate(['/recurring-transaction']); }
  goToContact() { this.router.navigate(['/contect-us']); }
  goToAbout() { this.router.navigate(['/about']); }
  goTowelcome() { this.router.navigate(['/login']); }
}
