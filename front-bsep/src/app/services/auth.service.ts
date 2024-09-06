import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, switchMap, tap } from 'rxjs/operators';

import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/bsep/auth';  // Backend URL

  constructor(private http: HttpClient) { }

  // Store access and refresh tokens in localStorage
  storeTokens(accessToken: string, refreshToken: string): void {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  }

  // Retrieve access token from localStorage
  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  // Retrieve refresh token from localStorage
  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  // Login method
  login(credentials: { email: string, password: string, captchaToken: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials).pipe(
      tap((response: any) => {
        if (response.accessToken && response.refreshToken) {
          this.storeTokens(response.accessToken, response.refreshToken);
        }
      }),
      catchError(error => {
        console.error('Login failed', error);
        return of(null);  // Return null or an observable based on your preference
      })
    );
  }

  // Refresh access token using the refresh token
  refreshToken(): Observable<any> {
    const refreshToken = this.getRefreshToken();
    if (refreshToken) {
      return this.http.post(`${this.baseUrl}/refresh-token`, { refreshToken }).pipe(
        tap((response: any) => {
          if (response.accessToken && response.refreshToken) {
            // Store new tokens
            this.storeTokens(response.accessToken, response.refreshToken);
          }
        }),
        catchError(error => {
          console.error('Refresh token failed', error);
          return of(null);  // Return null or an observable based on your preference
        })
      );
    } else {
      console.warn('No refresh token found');
      return of(null);  // Return null or an observable based on your preference
    }
  }

  // Method to clear tokens from localStorage (for logout)
  clearTokens(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

    // Function to check if the token has expired
    isTokenExpired(token: string): boolean {
      const decodedToken = jwtDecode<{ exp: number }>(token);  // Dekodiraj token i dohvati 'exp' claim
      const expirationDate = new Date(0);  // Kreiraj datum sa vremenom 0
      expirationDate.setUTCSeconds(decodedToken.exp);  // Podesi vreme isteka na 'exp' iz tokena
      return expirationDate < new Date();  // Proveri da li je token istekao
    }

  // Logout method
  logout(): void {
    this.clearTokens();
    // Optionally, you can navigate to the login page here
  }
}
