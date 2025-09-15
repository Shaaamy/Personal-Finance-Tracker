import { Component,OnInit,Inject , PLATFORM_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { Auth } from '../services/auth';
import { FormControl, FormGroup,ReactiveFormsModule, Validators } from '@angular/forms';

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
  imports: [CommonModule, FormsModule,ReactiveFormsModule],
  templateUrl: './transaction.html',
  styleUrls: ['./transaction.css']
})
export class TransactionComponent {

  transactionform:FormGroup = new FormGroup({
    amount: new FormControl('', Validators.required),
    type: new FormControl('Expense', Validators.required),
    categoryName: new FormControl('', Validators.required),
    date: new FormControl('', Validators.required),
    description: new FormControl('', Validators.required),
    currency: new FormControl('EGP', Validators.required)
  });

  isBrowser: boolean;
  showForm = false;
  isEdit = false;
  editIndex: number | null = null;

  // ✅ Added for filter controls
  filterDate = '';
  filterCategory = '';
  filterType = '';
  filterCurrency = '';
  filterText = '';
  filteredTransactions: Transaction[] = [];

  // ✅ Error flag for form validation
  formError = false;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router,
    private authService: Auth
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
    this.filteredTransactions = this.transactions;
  }

  transactions: Transaction[] = [
    { date: '2023-10-26', description: 'Groceries at FreshMart',currency:'EGP', categoryName: 'Food', type: 'Expense', amount: 54.20 },
    { date: '2023-10-25', description: 'Monthly Salary',currency:'EGP', categoryName: 'Work', type: 'Income', amount: 3200.00 },
    { date: '2023-10-24', description: 'Dinner with Friends',currency:'EGP', categoryName: 'Entertainment', type: 'Expense', amount: 75.50 },
    { date: '2023-10-23', description: 'Utility Bill Payment',currency:'EGP', categoryName: 'Bills', type: 'Expense', amount: 112.75 },
    { date: '2023-10-22', description: 'Investment Dividend',currency:'EGP', categoryName: 'Investments', type: 'Income', amount: 150.00 },
  ];

  newTransaction: Transaction = {
    date: '',
    description: '',
    currency: '',
    categoryName: '',
    type: 'Expense',
    amount: 0
  };

  get totalIncome(): number {
    return this.transactions.filter(t => t.type === 'Income').reduce((s, t) => s + t.amount, 0);
  }
  get totalExpense(): number {
    return this.transactions.filter(t => t.type === 'Expense').reduce((s, t) => s + t.amount, 0);
  }
  get netBalance(): number { return this.totalIncome - this.totalExpense; }
  get status(): string { return this.netBalance >= 0 ? 'Profit' : 'Loss'; }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  addTransaction() {
    this.showForm = true;
    this.isEdit = false;
    this.newTransaction = { date: '', description: '', currency:'', categoryName: '', type: 'Expense', amount: 0 };
    this.formError = false;
    this.transactionform.reset({ type:'Expense', currency:'EGP' });
  }

  // ✅ Now checks that all required fields are filled
  saveTransaction(transactionForm: FormGroup) {
    if (transactionForm.invalid) {
      this.formError = true;
      return;
    }
    this.formError = false;

    if (!this.authService.getAccessToken()) {
      console.error('No token found. Cannot save transaction.');
      return;
    }

    this.authService.transaction(transactionForm.value).subscribe({
      next: (response) => console.log('Transaction saved successfully', response),
      error: (err) => {
        console.error('Save Transaction Error:', err);
        if (err.status === 403) {
          console.error('⚠️ 403 Forbidden: Check API auth/roles or token expiry');
        }
      }
    });

    if (this.isEdit && this.editIndex !== null) {
      this.transactions[this.editIndex] = { ...this.newTransaction };
    } else {
      this.transactions.unshift({ ...this.newTransaction });
    }
    this.filteredTransactions = this.transactions;
    this.showForm = false;
    this.isEdit = false;
    this.editIndex = null;
  }

  editTransaction(index: number) {
    this.showForm = true;
    this.isEdit = true;
    this.editIndex = index;
    this.newTransaction = { ...this.transactions[index] };
    this.formError = false;
    this.transactionform.setValue({ ...this.transactions[index] });
  }

  deleteTransaction(index: number) {
    this.transactions.splice(index, 1);
    this.applyFilters();
  }

  cancelForm() {
    this.showForm = false;
    this.isEdit = false;
    this.editIndex = null;
    this.formError = false;
  }

  // ✅ Filter method
  applyFilters() {
    this.filteredTransactions = this.transactions.filter(t => {
      return (!this.filterCategory || t.categoryName.includes(this.filterCategory)) &&
             (!this.filterType || t.type === this.filterType) &&
             (!this.filterCurrency || t.currency === this.filterCurrency) &&
             (!this.filterText || t.description.toLowerCase().includes(this.filterText.toLowerCase()));
    });
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

  goTowelcome() {
    this.router.navigate(['/welcome']);
  }
  
}
