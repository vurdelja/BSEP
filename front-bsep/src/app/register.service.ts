// src/app/register.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface RegisterRequest {
  email: string;
  password: string;
  passwordConfirm: string;
  address: string;
  city: string;
  country: string;
  phoneNumber: string;
  userType: string;
  firstName: string;
  lastName: string;
  companyName: string;
  pib: string;
  packageType: string;
}

interface LoginResponse {
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private baseUrl = 'http://localhost:8080/bsep/auth';

  constructor(private http: HttpClient) { }

  register(request: RegisterRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/register`, request);
  }
}
