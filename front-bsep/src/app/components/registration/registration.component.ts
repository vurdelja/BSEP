import { Component } from '@angular/core';
import { RegistrationService } from '../../services/registration.service';
import { Router } from '@angular/router';  
import { FormsModule, NgForm } from '@angular/forms';

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

  constructor(private registrationService: RegistrationService, private router: Router) {}

  save(form: NgForm) {
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
        alert('Registered successfully.');
      },
      (error) => {
        console.error(error);
        alert('Unsuccessfull registration. ' + error.message);
      }
    );
  }
}
