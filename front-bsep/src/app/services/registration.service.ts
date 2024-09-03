import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private baseUrl = 'http://localhost:8080/bsep';  // Adjust this URL based on your actual backend URL.

  constructor(private http: HttpClient) { }

  // Register a new user
  registerUser(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/register`, userData);
  }


  login(credentials: {email: string, password: string, captchaToken: string}): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/login`, credentials);
  }


  refreshToken(refreshToken: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/refresh-token`, { refreshToken });
  }
}
