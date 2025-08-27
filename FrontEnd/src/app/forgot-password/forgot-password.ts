import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../services/auth';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.html',
  styleUrls: ['./forgot-password.css'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule]
})
export class ForgotPasswordComponent {
  forgotPasswordForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email])
  });

  errorMsg = '';
  successMsg = '';
  isLoading = false;

  constructor(
    private router: Router,
    private authService: Auth,
    private cdr: ChangeDetectorRef
  ) {}

  onSubmit() {
    this.errorMsg = '';
    this.successMsg = '';

    if (this.forgotPasswordForm.invalid) {
      this.errorMsg = 'Please enter a valid email address.';
      return;
    }

    const email = this.forgotPasswordForm.get('email')?.value;
    console.log(email);
    if (!email) return;

    this.isLoading = true;

    this.authService.forgotPassword(email).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.body?.success) {
          this.successMsg = 'Password reset instructions sent. Check your inbox.';
          this.forgotPasswordForm.reset();
          setTimeout(() => this.router.navigate(['/reset-password']), 2000);
        } else {
          this.errorMsg = response.body?.message || 'Failed to send reset email.';
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMsg = err?.error?.message || 'An error occurred. Please try again.';
        this.cdr.detectChanges();
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
