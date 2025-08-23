import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-signup',
  templateUrl: './signup.html',
  styleUrls: ['./signup.css']
})
export class Signup {
  logoUrl: string = 'https://agency.entasher.com/Images/actualsize/2024/5/Logo_847363b7554a46b48559c222f4f9053c.png';

  onSubmit() {
    alert('Account created successfully (demo)');
  }
   constructor(private router: Router) {}

  goTo() {
    this.router.navigate(['/login']);
  }
}
