// token.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { RefreshTokenDialogComponent } from '../refresh-token-dialog/refresh-token-dialog.component';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private dialog: MatDialog) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          const dialogRef = this.dialog.open(RefreshTokenDialogComponent);
          return dialogRef.afterClosed().pipe(
            switchMap(result => {
              if (result) {
                return this.authService.refreshToken().pipe(
                  switchMap(newToken => {
                    if (newToken) {
                      request = request.clone({
                        setHeaders: {
                          Authorization: `Bearer ${newToken}`
                        }
                      });
                      return next.handle(request);
                    }
                    return throwError(error);
                  })
                );
              }
              return throwError(error);
            })
          );
        }
        return throwError(error);
      })
    );
  }
}
