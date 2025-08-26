import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './welcome.html',
  styleUrls: ['./welcome.css']
})
export class Welcome {
  // ✅ خلى الـ properties هنا جوه الكلاس، مش جوه الدالة
  logoUrl: string = 'https://agency.entasher.com/Images/actualsize/2024/5/Logo_847363b7554a46b48559c222f4f9053c.png';
  phoneUrl: string = 'https://aimst.edu.my/wp-content/uploads/2022/12/finance-accounting.webp';

  constructor(private router: Router) {}

  goToLogin() {
    this.router.navigate(['/login']);
  }
  goTosignup() {
    this.router.navigate(['/signup']);
  }
  isAdmin(): boolean {
  return localStorage.getItem('role') === 'ADMIN';
}
}
