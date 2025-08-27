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

  constructor(private router: Router, private authService: Auth, private cdr: ChangeDetectorRef) { }

  onSubmit() {
    this.errorMsg = '';

    if (this.loginForm.invalid) {
      this.errorMsg = 'Please fill all required fields correctly.';
      return;
    }

    const loginData = {
      username: this.loginForm.get('username')?.value,
      password: this.loginForm.get('password')?.value
    };

    this.authService.login(loginData).subscribe({
      next: (response) => {
        console.log('Login successful, checking authentication...');

        // Wait a short time for the cookies to be set
        setTimeout(() => {
          // Debug authentication info
          this.authService.debugAuth();

          if (!this.authService.isAuthenticated()) {
            console.warn('User not authenticated!');
            this.errorMsg = 'Authentication failed';
            return;
          }

          const role = this.authService.getUserRole();
          console.log('User role:', role);

          if (this.authService.isAdmin()) {
            console.log('Navigating to admin dashboard');
            this.router.navigate(['/dashboard']);
          } else if (this.authService.isUser()) {
            console.log('Navigating to user home');
            this.router.navigate(['/home']).then(success => {
    console.log('Home navigation result:', success);
    if (!success) {
      console.error('Navigation to /home failed - route might not exist');
    }
  }).catch(err => {
    console.error('Home navigation error:', err);
  });
          } else {
            console.log('Unknown role, navigating to default home');
            this.router.navigate(['/welcome']);
          }
        }, 500);
      },
      error: (err) => {
        console.error('Login error:', err);
        this.errorMsg = err?.error?.message || 'Server Error';
        this.cdr.detectChanges();
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
