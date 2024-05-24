import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private baseUrl = 'http://localhost:8080/bsep/request';  // Adjust this URL based on your actual backend URL.

  constructor(private http: HttpClient) { }

  registerUser(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/sendRequest`, userData);
  }
}
