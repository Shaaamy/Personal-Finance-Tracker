import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.html',
  styleUrls: ['./forgot-password.css']
})
export class ForgotPassword {
  logoUrl: string = 'https://agency.entasher.com/Images/actualsize/2024/5/Logo_847363b7554a46b48559c222f4f9053c.png';

  onSubmit(contactInfo: string) {
    if (!contactInfo) {
      alert('⚠️ Please enter your email or phone number.');
      return;
    }

    if (contactInfo.includes('@')) {
      alert(`📧 Reset instructions sent to email: ${contactInfo}`);
    } else {
      alert(`📱 Reset instructions sent to phone: ${contactInfo}`);
    }
  }
  constructor(private router: Router) {}

  goBackToLogin() {
    this.router.navigate(['/login']);
  }
  
}
