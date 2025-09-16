import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../services/auth';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.html',
  styleUrls: ['./signup.css'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule]
})
export class Signup {
  logoUrl: string =
    'https://agency.entasher.com/Images/actualsize/2024/5/Logo_847363b7554a46b48559c222f4f9053c.png';

  signupForm = new FormGroup({
    username: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    fullName: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    confirmPassword: new FormControl('', Validators.required),
    role: new FormControl(''),
    balance: new FormControl('', Validators.required)
  });

  constructor(private router: Router, private authService: Auth) {}

  onSubmit() {
    if (this.signupForm.invalid) {
      // إظهار رسائل الخطأ لكل الحقول
      this.signupForm.markAllAsTouched();
      return;
    }

    console.log(this.signupForm.value);
    this.authService.register(this.signupForm.value).subscribe(res => {
      console.log(res);
      this.router.navigate(['/home']);
    });
  }

  goToSignup() {
    this.router.navigate(['/home']);
  }
  goTo() {
    this.router.navigate(['/login']);
  }
}   
