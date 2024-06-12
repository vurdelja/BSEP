import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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


  login(credentials: {email: string, password: string, captchaToken: string}): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, credentials);
  }


  refreshToken(refreshToken: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/refresh-token`, { refreshToken });
  }

}
