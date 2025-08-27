import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../services/auth';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.html',
  styleUrls: ['./signup.css'],
  imports: [ReactiveFormsModule]
})
export class Signup {
  logoUrl: string = 'https://agency.entasher.com/Images/actualsize/2024/5/Logo_847363b7554a46b48559c222f4f9053c.png';

  signupForm = new FormGroup({
    username: new FormControl(''),
    email: new FormControl(''),
    fullName: new FormControl(''),
    password: new FormControl(''),
    confirmPassword: new FormControl(''),
    role: new FormControl(''),
    balance: new FormControl('')
  });
  onSubmit() {
    console.log(this.signupForm.value);
    this.authService.register(this.signupForm.value).subscribe((res) => {
      console.log(res);
    });
  }
   constructor(private router: Router, private authService: Auth) {}

  goTo() {
    this.router.navigate(['/login']);
  }
}
