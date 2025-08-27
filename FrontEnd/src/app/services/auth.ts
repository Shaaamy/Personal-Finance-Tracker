import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private baseUrl = 'http://localhost:8081/users';

  constructor(private http: HttpClient) {}

  register(user: Record<string, any>): Observable<any> {
    return this.http.post<any>(
      `${this.baseUrl}/register`,
      user,
      {
        withCredentials: true,
        observe: 'response'
      }
    );
  }

  login(user: Record<string, any>): Observable<any> {
    return this.http.post<any>(
      `${this.baseUrl}/login`,
      user,
      {
        withCredentials: true,
        observe: 'response'
      }
    );
  }

  getCookie(name: string): string | null {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? decodeURIComponent(match[2]) : null;
  }

  // Add method to decode JWT token
  private decodeJWT(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      return JSON.parse(decoded);
    } catch (error) {
      console.error('Error decoding JWT:', error);
      return null;
    }
  }

  // Get access token from cookie
  getAccessToken(): string | null {
    return this.getCookie('accessToken');
  }

  // Extract role from JWT token
  getUserRole(): string | null {
    const token = this.getAccessToken();
    if (!token) {
      return null;
    }

    const decoded = this.decodeJWT(token);
    if (!decoded || !decoded.roles || !decoded.roles.length) {
      return null;
    }

    // Extract role and remove 'ROLE_' prefix if present
    const role = decoded.roles[0]; // Get first role
    return role.startsWith('ROLE_') ? role.substring(5) : role;
  }

  isAdmin(): boolean {
    const role = this.getUserRole();
    return role === 'ADMIN';
  }

  isUser(): boolean {
    const role = this.getUserRole();
    return role === 'USER';
  }

  // Check if user is authenticated
  isAuthenticated(): boolean {
    const token = this.getAccessToken();
    if (!token) {
      return false;
    }

    const decoded = this.decodeJWT(token);
    if (!decoded) {
      return false;
    }

    // Check if token is expired
    const currentTime = Math.floor(Date.now() / 1000);
    return decoded.exp > currentTime;
  }

  // Debug methods
  getAllCookies(): string {
    return document.cookie;
  }

  debugAuth(): void {
    console.log('All cookies:', document.cookie);
    const token = this.getAccessToken();
    console.log('Access token:', token);
    
    if (token) {
      const decoded = this.decodeJWT(token);
      console.log('Decoded token:', decoded);
      console.log('User role:', this.getUserRole());
      console.log('Is authenticated:', this.isAuthenticated());
      console.log('Is admin:', this.isAdmin());
      console.log('Is user:', this.isUser());
    }
  }

  
}