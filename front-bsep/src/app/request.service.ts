import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Request } from './request';

@Injectable({
  providedIn: 'root'
})
export class RequestService {
  baseServerUrl = 'http://localhost:8080/bsep';

  constructor(private http: HttpClient) { }

  public getAllRequests(): Observable<Request[]> {
    return this.http.get<Request[]>(`${this.baseServerUrl}/request/all`);
  }
}