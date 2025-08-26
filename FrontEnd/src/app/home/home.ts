import { Component, OnInit,Inject , PLATFORM_ID  } from '@angular/core';
import { Router } from '@angular/router';
import { UserDataService } from '../services/user-data.service';
import { CommonModule } from '@angular/common';
import { isPlatformBrowser } from '@angular/common';



@Component({
  selector: 'app-home',
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
    imports: [CommonModule]

})
export class HomeComponent implements OnInit {   // ✅ better to name it HomeComponent
  isBrowser: boolean;
  transactions: any[] = [];
  recurringTransactions: any[] = [];

  constructor(
    private router: Router,
    private userDataService: UserDataService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }
  ngOnInit() {
  if (!this.isBrowser) return;

  const token = localStorage.getItem('token');
  if (!token) {
    this.router.navigate(['/login']);
    return;
  }

  this.userDataService.getTransactions().subscribe({
    next: data => {
      this.transactions = data;
      if (data.length === 0) {
        console.log('No transactions found - this is normal for new users');
      }
    },
    error: err => console.error('Failed to fetch transactions', err)
  });

  this.userDataService.getRecurringTransactions().subscribe({
    next: data => this.recurringTransactions = data
  });
}
  isAdmin(): boolean {
 // Safely access localStorage only in the browser
    if (!this.isBrowser) return false;
    return localStorage.getItem('role') === 'ADMIN';}
 goToWelcome() {
    this.router.navigate(['/welcome']);   
  }
  goToHome() {
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

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
