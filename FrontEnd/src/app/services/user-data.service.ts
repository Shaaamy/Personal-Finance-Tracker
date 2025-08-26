import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class UserDataService {
  private baseUrl = 'http://localhost:8081'; // your backend URL

  constructor(private http: HttpClient) {}

  private getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      })
    };
  }

  getTransactions(): Observable<any> {
    return this.http.get(`${this.baseUrl}/transactions`, this.getAuthHeaders())
      .pipe(
        catchError(this.handleError)
      );
  }
getUserTransactions(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/transactions/user`, this.getAuthHeaders())
    .pipe(catchError(this.handleError));
}



  getRecurringTransactions(): Observable<any> {
    return this.http.get(`${this.baseUrl}/recurring-transactions`, this.getAuthHeaders())
      .pipe(
        catchError(this.handleError)
      );
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