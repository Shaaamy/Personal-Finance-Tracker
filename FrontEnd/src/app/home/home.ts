import { Component, OnInit,Inject , PLATFORM_ID  } from '@angular/core';
import { Router } from '@angular/router';
import { UserDataService } from '../services/user-data.service';
import { CommonModule } from '@angular/common';
import { isPlatformBrowser } from '@angular/common';
import { Auth } from '../services/auth';



@Component({
  selector: 'app-home',
  templateUrl: './home.html',
    standalone: true,  // ✅ Add this!

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
    private authService : Auth,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }
  ngOnInit() {
  if (!this.isBrowser) return;

  // ✅ Use your auth service instead of manual cookie checking
    if (!this.authService.isAuthenticated()) {
      console.log('User not authenticated, redirecting to login');
      this.router.navigate(['/login']);
      return;
    }
        console.log('User authenticated, loading home page data');


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
    return this.authService.isAdmin();
  }

  // ✅ Helper to check if auth cookie exists
  private hasAuthCookie(): boolean {
    if (!this.isBrowser) return false;
    return document.cookie.includes('token='); // or your actual cookie name
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

  // ✅ Add logout functionality
  logout() {
    // Clear cookies (you might want to add this to your auth service)
    document.cookie = 'accessToken=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/';
    document.cookie = 'refreshToken=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/';
    this.router.navigate(['/login']);
  }
}
