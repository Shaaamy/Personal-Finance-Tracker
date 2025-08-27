import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Auth } from '../services/auth';

@Injectable({ providedIn: 'root' })
export class UserDataService {
  private baseUrl = 'http://localhost:8081'; // your backend URL

  constructor(private http: HttpClient
    ,private authService : Auth
  ) {}
private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getAccessToken();
    if (token) {
      return new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      });
    }
    return new HttpHeaders({
      'Content-Type': 'application/json'
    });
  }
  

  getTransactions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/transactions`, {
      headers: this.getAuthHeaders(),
      withCredentials: true
    })    .pipe(catchError(this.handleError));
;
  }
getUserTransactions(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/transactions/user`, {headers: this.getAuthHeaders(), withCredentials: true })
    .pipe(catchError(this.handleError));
}



   getRecurringTransactions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/recurring-transactions`, {
      headers: this.getAuthHeaders(),
      withCredentials: true
    })    .pipe(catchError(this.handleError));
;
  }

isAdmin(): boolean {
    return this.authService.isAdmin();
  }




// user-data.service.ts
private handleError(error: any) {
  console.error('API Error:', error);
  
  let errorMessage = 'Something went wrong; please try again later.';
  
  if (error.status === 401) {
    errorMessage = 'Session expired. Please login again.';
  } else if (error.status === 403) {
    errorMessage = 'Access denied. You don\'t have permission to perform this action.';
  } else if (error.status === 404) {
    errorMessage = 'No transactions found.'; // Handle 404 gracefully
    return of([]); // Return empty array instead of throwing error
  } else if (error.status === 500) {
    errorMessage = 'Server error. Please try again later.';
  }
  
  return throwError(() => new Error(errorMessage));
}
}