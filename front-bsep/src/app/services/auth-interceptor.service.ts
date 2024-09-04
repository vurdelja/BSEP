import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authToken = localStorage.getItem('accessToken');
    console.log('Auth Token:', authToken); // Logovanje tokena

    if (authToken) {
      // Kloniranje zahteva i dodavanje zaglavlja
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`
        }
      });
      console.log('Sending request with new headers:', request); // Logovanje modifikovanog zahteva
    } else {
      console.log('Sending request without auth token:', request); // Logovanje zahteva bez tokena
    }

    return next.handle(request);
  }
}
