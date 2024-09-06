import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    const token = this.authService.getAccessToken();

    if (token && !this.authService.isTokenExpired(token)) {
      // Token postoji i validan je
      return true;
    }

    // Ako token ne postoji ili je istekao, preusmeriti na login stranicu
    this.router.navigate(['/login']);
    return false;
  }
}
