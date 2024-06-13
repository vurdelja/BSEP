import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/bsep/auth';  // Adjust this URL based on your actual backend URL.

  constructor(private http: HttpClient) { }

  // Store tokens
  storeTokens(accessToken: string, refreshToken: string): void {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  }

  // Login method
  login(credentials: { email: string, password: string, captchaToken: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials);
  }

  // Refresh token method
  refreshToken(): Observable<any> {
    const refreshToken = localStorage.getItem('refreshToken');
    if (refreshToken) {
      return this.http.post(`${this.baseUrl}/refresh-token`, { refreshToken }).pipe(
        switchMap((response: any) => {
          this.storeTokens(response.accessToken, response.refreshToken);
          return of(response.accessToken);
        }),
        catchError(error => {
          console.error('Refresh token failed', error);
          return of(null);
        })
      );
    }
    return of(null);
  }
}
