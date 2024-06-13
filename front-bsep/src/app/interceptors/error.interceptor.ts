import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          // Handle 401 errors
          const refreshToken = localStorage.getItem('refreshToken');
          if (refreshToken) {
            return this.handle401Error(request, next, refreshToken);
          } else {
            // If no refresh token is available, handle accordingly, maybe logout the user or redirect to login
            return throwError(() => new Error('Authentication required, please log in.'));
          }
        }
        return throwError(() => error);
      })
    );
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler, refreshToken: string) {
    return this.authService.refreshToken().pipe(
      switchMap((tokenResponse: any) => {
        localStorage.setItem('accessToken', tokenResponse.accessToken);
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${tokenResponse.accessToken}`
          }
        });
        return next.handle(request);
      }),
      catchError(err => {
        // If refreshing the token fails, handle accordingly
        return throwError(() => new Error('Failed to refresh token'));
      })
    );
  }
}
