import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Commercial } from '../models/commercial.model';

@Injectable({
  providedIn: 'root'
})
export class CommercialService {
  private baseUrl = 'http://localhost:8080/bsep/commercial';

  constructor(private http: HttpClient) { }

  getCommercials(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/all`);
  }


}
