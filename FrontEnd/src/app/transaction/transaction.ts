import { Component,OnInit,Inject , PLATFORM_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { Auth } from '../services/auth';
export interface Transaction {
  date: string;
  description: string;
  category: string;
  type: string;
  amount: number;
}

@Component({
  selector: 'app-transaction',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transaction.html',
  styleUrls: ['./transaction.css']
})
export class TransactionComponent {
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

  transactions: Transaction[] = [
    { date: '2023-10-26', description: 'Groceries at FreshMart', category: 'Food', type: 'Expense', amount: 54.20 },
    { date: '2023-10-25', description: 'Monthly Salary', category: 'Work', type: 'Income', amount: 3200.00 },
    { date: '2023-10-24', description: 'Dinner with Friends', category: 'Entertainment', type: 'Expense', amount: 75.50 },
    { date: '2023-10-23', description: 'Utility Bill Payment', category: 'Bills', type: 'Expense', amount: 112.75 },
    { date: '2023-10-22', description: 'Investment Dividend', category: 'Investments', type: 'Income', amount: 150.00 },
  ];

  newTransaction: Transaction = {
    date: '',
    description: '',
    category: '',
    type: 'Expense',
    amount: 0
  };

  // ✅ الحسابات تتحدث أوتوماتيك
  get totalIncome(): number {
    return this.transactions
      .filter(t => t.type === 'Income')
      .reduce((sum, t) => sum + t.amount, 0);
  }

  get totalExpense(): number {
    return this.transactions
      .filter(t => t.type === 'Expense')
      .reduce((sum, t) => sum + t.amount, 0);
  }

  get netBalance(): number {
    return this.totalIncome - this.totalExpense;
  }

  get status(): string {
    return this.netBalance >= 0 ? 'Profit' : 'Loss';
  }

   // ====== Role-based Access ======
  
isAdmin(): boolean {
    return this.authService.isAdmin();
  }

    // ====== Transaction Management ======

  addTransaction() {
    this.showForm = true;
    this.isEdit = false;
    this.newTransaction = { date: '', description: '', category: '', type: 'Expense', amount: 0 };
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
    this.editIndex = null;
  }

 
  goHome() {
    this.router.navigate(['/home']);   
  }

  goToTransaction() {
    this.router.navigate(['/transaction']);   
  }

  goToRecurring() {
    this.router.navigate(['/recurring-transaction']);   
  }

  goToDashboard() {
    this.router.navigate(['/dashboard']);
  }

  goToContact() {
    this.router.navigate(['/contect-us']);   // ✅ fixed typo
  }

  goToAbout() {
    this.router.navigate(['/about']);
  }

}
