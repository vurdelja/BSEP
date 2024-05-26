import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/bsep/user';  // Adjust this URL based on your actual backend URL

  constructor(private http: HttpClient) { }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/getById/${userId}`);
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/update/${user.id}`, user);
  }
}
