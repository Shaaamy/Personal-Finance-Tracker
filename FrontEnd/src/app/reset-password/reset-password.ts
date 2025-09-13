import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-reset-password',
  standalone: true,  // ✅ makes it standalone so we can import modules directly
  imports: [CommonModule, ReactiveFormsModule],  // ✅ needed for *ngIf and [formGroup]
  templateUrl: './reset-password.html',  // ✅ fixed filename
  styleUrls: ['./reset-password.css']
})
export class ResetPasswordComponent {
  resetForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.resetForm = this.fb.group(
      {
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', [Validators.required]]
      },
      { validators: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirm = form.get('confirmPassword')?.value;
    return password === confirm ? null : { passwordMismatch: true };
  }

  onSubmit() {
    if (this.resetForm.valid) {
      console.log('Password Reset Data:', this.resetForm.value);
      alert('Password has been reset successfully!');
    }
  }
}
