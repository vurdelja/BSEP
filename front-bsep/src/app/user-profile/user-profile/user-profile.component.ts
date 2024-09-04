import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationService } from '../../services/registration.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent {
  constructor(private registrationService: RegistrationService, private router: Router) {}
  
  logout() {
    // Čistimo token iz storage
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  
    // Preusmeravamo korisnika na stranicu za logovanje
    this.router.navigate(['/login']);
  }
  

  onSubmit(): void {
    
      };
}