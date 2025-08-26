import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    const role = localStorage.getItem('role');
    if (role === 'ADMIN') {
      return true;
    } else {
      this.router.navigate(['/home']); // redirect non-admin users
      return false;
    }
  }
}
