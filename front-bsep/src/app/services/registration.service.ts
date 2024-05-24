import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private baseUrl = 'http://localhost:8080/bsep/request';  // Adjust this URL based on your actual backend URL.

  constructor(private http: HttpClient) { }

  // Register a new user
  registerUser(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/sendRequest`, userData);
  }

  // Get all registration requests
  getAllRequests(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/all`);
  }

  // Approve a registration request
  approveRequest(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/approve/${id}`, {});
  }

   // Reject a registration request with a reason
   rejectRequest(id: number, reason: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/reject/${id}`, { reason: reason }, { responseType: 'text' });
  }
}
