import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-contact-us',
  templateUrl: './contect-us.html',
  styleUrls: ['./contect-us.css']
})
export class ContactUs {
constructor(private router: Router) {}
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