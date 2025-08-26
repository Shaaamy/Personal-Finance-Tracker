import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../services/auth';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule]
})
export class Login {
  logoUrl: string = 'https://agency.entasher.com/Images/actualsize/2024/5/Logo_847363b7554a46b48559c222f4f9053c.png';

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  });

  errorMsg: string = '';

  constructor(private router: Router, private authService: Auth, private cdr: ChangeDetectorRef) {}

  onSubmit() {
    this.errorMsg = ''; // reset previous errors

    if (this.loginForm.invalid) {
      this.errorMsg = 'Please fill all required fields correctly.';
      return;
    }

    const loginData = {
      username: this.loginForm.get('username')?.value,
      password: this.loginForm.get('password')?.value
    };

    this.authService.login(loginData).subscribe({
      next: (res) => {
        console.log('Login response:', res);

        if (res.data?.accessToken) {
          localStorage.setItem('token', res.data.accessToken);
          localStorage.setItem('role', res.data.role);

          if (res.data.role === 'ADMIN') {
            this.router.navigate(['/dashboard']);
          } else {
            this.router.navigate(['/home']);
          }
        } else {
          // API sent a message without token (like auth error)
          this.errorMsg = res.message || 'Login failed';
          this.cdr.detectChanges(); // force view update
        }
      },
      error: (err) => {
        console.error('Login error:', err);

        if (err.status === 404) {
          this.errorMsg = err?.error?.message;
        }
          this.errorMsg = err?.error?.message || "Server Error"
        this.cdr.detectChanges(); // force view update
      }
    });
  }

  goToSignup() {
    this.router.navigate(['/signup']);
  }

  goToForgot() {
    this.router.navigate(['/forgot-password']);
  }

  goTowelcome() {
    this.router.navigate(['/welcome']);
  }
}
