import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationService } from '../../services/registration.service';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  captchaResolved: boolean = false;
  captchaToken: string | null = null;
  totpCode: string | null = null;
  customCaptchaResolved: boolean = false;
  showCustomCaptcha: boolean = false;

  constructor(private registrationService: RegistrationService, private router: Router) {}

  resolved(captchaResponse: string | null) {
    this.captchaToken = captchaResponse;
    this.captchaResolved = !!captchaResponse;
    if (this.captchaResolved) {
      this.showCustomCaptcha = true;
    }
  }

  onCustomCaptchaResolved(resolved: boolean) {
    this.customCaptchaResolved = resolved;
  }

  redirectUser(role: string) {
    if (role === 'ADMIN') {
      this.router.navigate(['/admin-profile']);
    } else if (role === 'EMPLOYEE') {
      this.router.navigate(['/employee-profile']);
    } else if (role === 'USER') {
      this.router.navigate(['/user-profile']);
    } else {
      console.error('Unknown role:', role);
      alert('Unknown role: ' + role);
    }
  }
  login() {
    // Clear old tokens before sending a new login request
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');

    if (!this.captchaToken || !this.customCaptchaResolved) {
        alert('Please resolve the CAPTCHA first');
        return;
    }

    const credentials = {
        email: this.email,
        password: this.password,
        captchaToken: this.captchaToken,
        totpCode: this.totpCode
    };

    console.log('Sending login request with credentials:', credentials);

    // Calling the login method from the service and subscribing to the observable it returns
    this.registrationService.login(credentials).pipe(
        tap((response: any) => {
            // Storing the token received from the response in local storage
            localStorage.setItem('accessToken', response.accessToken);
            localStorage.setItem('refreshToken', response.refreshToken);
            console.log('Token received and stored:', response.accessToken);
        })
    ).subscribe(
        response => {
            console.log('Navigating to user profile');
            const userId = response.userId; // Assume the response contains the user's ID
            // Navigate to the user profile page with user ID
            this.router.navigate([`/protected`]); 
        },
        error => {
            console.error('Login failed:', error);
            alert('Login failed: ' + error.message);
        }
    );
}
}
