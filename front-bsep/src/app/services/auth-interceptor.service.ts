import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError  } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getAccessToken();
    
    // Clone the request and add the access token to the headers
    let authReq = req;
    if (token) {
      authReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Access token expired, try to refresh it
          return this.authService.refreshToken().pipe(
            switchMap((newAccessToken: string) => {
              if (newAccessToken) {
                // Retry the failed request with the new access token
                const newAuthReq = req.clone({
                  setHeaders: { Authorization: `Bearer ${newAccessToken}` }
                });
                return next.handle(newAuthReq);
              } else {
                // If refresh token failed, log out and redirect to login
                this.authService.logout();
                this.router.navigate(['/login']);
                return throwError(error);
              }
            })
          );
        }

        // If the error is not 401, throw it back
        return throwError(error);
      })
    );
  }
}