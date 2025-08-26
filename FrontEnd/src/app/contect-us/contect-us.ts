import { Component,OnInit,Inject , PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-contact-us',
    imports: [CommonModule], // <-- this is needed for *ngIf, *ngFor, etc.
  templateUrl: './contect-us.html',
  styleUrls: ['./contect-us.css'],
})
export class ContactUs {
      isBrowser: boolean;

constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }
  isAdmin(): boolean {
 // Safely access localStorage only in the browser
    if (!this.isBrowser) return false;
    return localStorage.getItem('role') === 'ADMIN';}
  goToWelcome() {
    this.router.navigate(['/welcome']);   
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