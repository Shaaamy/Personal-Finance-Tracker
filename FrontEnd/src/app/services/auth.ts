import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

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
    ).pipe(
      tap((response: HttpResponse<any>) => {
        
        if (response.body) {
          console.log('Response Body:', response.body);
          
          const accessToken = response.body.data?.accessToken;
          
          if (accessToken) {
            localStorage.setItem('accessToken', accessToken);
            console.log('Access token stored in localStorage');
          }
          
          if (response.body.data) {
            const userData = {
              username: response.body.data.username,
              role: response.body.data.role
            };
            localStorage.setItem('userData', JSON.stringify(userData));
          }
        }
      })
    );
  }

  getCookie(name: string): string | null {
if (typeof document !== 'undefined') {
 const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? decodeURIComponent(match[2]) : null;
}
return null;
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

    if (typeof document !== 'undefined') {
 return this.getCookie('accessToken');
}
    return null;
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

  // Add these methods to your existing Auth service

forgotPassword(email: string): Observable<any> {
  return this.http.post<any>(
    `${this.baseUrl}/forgot-password`,
    null,  // No body needed since email is a query param
    {
      params: { email: email },  // Send email as query parameter
      withCredentials: true,
      observe: 'response'
    }
  );
}

resetPassword(token: string, newPassword: string): Observable<any> {
  return this.http.post<any>(
    `${this.baseUrl}/reset-password`,
    null,  // No body needed since params are query parameters
    {
      params: { 
        token: token,
        newPassword: newPassword
      },
      withCredentials: true,
      observe: 'response'
    }
  );
}
//    transaction(user: Record<string, any>): Observable<any> {
//     return this.http.post<any>(
//       `http://localhost:8081/transactions`,
//       user,
//       {
//         headers: { Authorization:' Bearer ${yourToken} ', 'Content-Type': 'application/json' },   
//         withCredentials: true,
//         observe: 'response'
//       }
//     );
// }

// transaction(data: Record<string, any>): Observable<any> {
//   const token = this.getAccessToken();            // ✅ use cookie
//   const headers = new HttpHeaders({
//     'Authorization': `Bearer ${token}`,
//     'Content-Type': 'application/json'
//   });

//   return this.http.post<any>(
//     'http://localhost:8081/transactions',
//     data,
//     { headers, withCredentials: true, observe: 'response' }
//   );
// }

private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    if (token) {
      return new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `${token}`
      });
    } else {
      console.warn('No access token found in localStorage');
      return new HttpHeaders({
        'Content-Type': 'application/json'
      });
    }
  }

  transaction(user: Record<string, any>): Observable<any> {
    const headers = this.getAuthHeaders();
    
    return this.http.post<any>(
      `http://localhost:8081/transaction`,
      user,
      {
        headers: headers,
        withCredentials: true,
        observe: 'response'
      }
    ).pipe(
      tap((response: HttpResponse<any>) => {
        console.log('Transaction API Response:', response);
        console.log('Response Status:', response.status);
        
        if (response.body) {
          console.log('Response Body:', response.body);
        }
      }),
      catchError((error: any) => {
        console.error('Transaction error:', error);
        if (error.status === 401) {
          console.error('Authentication failed - token may be invalid or expired');
          
        }
        return throwError(() => error);
      })
    );
  }


}
