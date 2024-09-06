import { Component } from '@angular/core';
import { RegistrationService } from '../../services/registration.service';
import { Router } from '@angular/router';  
import { FormsModule, NgForm } from '@angular/forms';
import { QRCodeModule } from 'angularx-qrcode';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  email: string = '';
  password: string = '';
  passwordConfirm: string = '';
  address: string = '';
  city: string = '';
  country: string = '';
  phoneNumber: string = '';
  firstName: string = '';
  lastName: string = '';
  companyName: string = '';
  pib: string = '';
  packageType: string = 'BASIC'; 
  userType: string = 'INDIVIDUAL';

  qrCodeUrl: string | null = null; // For storing the QR code URL
  isFormVisible: boolean = true; // Initially, the form is visible

  constructor(private registrationService: RegistrationService, private router: Router) {}

   // Method to navigate to the login page
   goToLogin() {
    this.router.navigate(['/login']); // Adjust '/login' if your route is different
  }

  save(form: NgForm) {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    
    if (form.invalid) {
      return;
    }

    let bodyData = {
      email: this.email,
      password: this.password,
      passwordConfirm: this.passwordConfirm,
      address: this.address,
      city: this.city,
      country: this.country,
      phoneNumber: this.phoneNumber,
      firstName: this.firstName,
      lastName: this.lastName,
      companyName: this.companyName,
      pib: this.pib,
      packageType: this.packageType,
      userType: this.userType
    };

    
    

    this.registrationService.registerUser(bodyData).subscribe(
      (resultData: any) => {
        console.log(resultData);
        if (resultData.qrCodeUrl) {
          this.qrCodeUrl = resultData.qrCodeUrl; // Save QR code URL
          this.isFormVisible = false; // Hide the form to show the QR code section
          alert('Registration successful. Please configure your 2FA by scanning the QR Code.');
        } else {
          alert('Registered successfully.');
        }
      },
      (error) => {
        console.error("Error occurred:", error);
        console.log("Error status: ", error.status);
        console.log("Error message: ", error.message);
        console.log("Error headers: ", error.headers);
        alert('Unsuccessful registration. ' + error.message);
      }
      
    );

    
    
  }



  
}
