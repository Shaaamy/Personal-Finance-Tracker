import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  // لينك اللوجو
  logoUrl: string = 'https://agency.entasher.com/Images/actualsize/2024/5/Logo_847363b7554a46b48559c222f4f9053c.png';

  // دالة تسجيل الدخول (تجريبية)
  onSubmit() {
    alert('Logged in successfully (demo)');
  }
  constructor(private router: Router) {}

  goToSignup() {
    this.router.navigate(['/signup']);
  }

  goToForgot() {
  this.router.navigate(['/forgot-password']);
}
goTohome() {
    this.router.navigate(['/home']);
  }
  goTowelcome() {
    this.router.navigate(['/welcome']);
  }
  
}
