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
        
        const userId = response.userId;  // Assume the response contains the user's ID
        const userRole = response.role;  // Assume the response contains the user's role
        if (userRole === 'ADMIN') {
          this.router.navigate([`/admin/${userId}`]);  // Navigate to the admin profile page with user ID
        } else if (userRole === 'EMPLOYEE') {
          this.router.navigate([`/employee-profile/${userId}`]);  // Navigate to the employee profile page with user ID
        } else if (userRole === 'USER') {
          this.router.navigate([`/user-profile/${userId}`]);  // Navigate to the user profile page with user ID
        } else {
          alert('Invalid user role');
        }
      },
      (error) => {
        console.error(error);
        alert('Login failed: ' + error.message);
      }
    );


  

  }
}
