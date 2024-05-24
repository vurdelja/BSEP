import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationService } from '../../services/registration.service';

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

  login() {
    if (!this.captchaToken || !this.customCaptchaResolved) {
      alert('Please resolve the CAPTCHA first');
      return;
    }

    const credentials = {
      email: this.email,
      password: this.password,
      captchaToken: this.captchaToken
    };

    this.registrationService.login(credentials).subscribe(
      (response: any) => {
        console.log(response);
        alert('Login successful');
        this.router.navigate(['admin']); // Navigate to home or another route
      },
      (error) => {
        console.error(error);
        alert('Login failed: ' + error.message);
      }
    );
  }
}
