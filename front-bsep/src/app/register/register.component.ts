import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { RegisterService } from '../register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private registerService: RegisterService
  ) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordValidator]],
      passwordConfirm: ['', [Validators.required]],
      address: ['', Validators.required],
      city: ['', Validators.required],
      country: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]*$/)]],
      userType: ['INDIVIDUAL', Validators.required], // Default to INDIVIDUAL
      firstName: [''],
      lastName: [''],
      companyName: [''],
      pib: ['', Validators.pattern(/^[0-9]*$/)],
      packageType: ['BASIC', Validators.required] // Default to BASIC
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    this.registerForm.get('userType')?.valueChanges.subscribe(value => {
      if (value === 'INDIVIDUAL') {
        this.registerForm.get('firstName')?.setValidators([Validators.required]);
        this.registerForm.get('lastName')?.setValidators([Validators.required]);
        this.registerForm.get('companyName')?.clearValidators();
        this.registerForm.get('pib')?.clearValidators();
      } else if (value === 'LEGAL ENTITY') {
        this.registerForm.get('firstName')?.clearValidators();
        this.registerForm.get('lastName')?.clearValidators();
        this.registerForm.get('companyName')?.setValidators([Validators.required]);
        this.registerForm.get('pib')?.setValidators([Validators.required, Validators.pattern(/^[0-9]*$/)]);
      }
      this.registerForm.get('firstName')?.updateValueAndValidity();
      this.registerForm.get('lastName')?.updateValueAndValidity();
      this.registerForm.get('companyName')?.updateValueAndValidity();
      this.registerForm.get('pib')?.updateValueAndValidity();
    });
  }

  passwordValidator(control: AbstractControl): { [key: string]: any } | null {
    const password = control.value;
    if (password) {
      const hasUpperCase = /[A-Z]/.test(password);
      const hasNumber = /\d/.test(password);
      const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);
      const valid = hasUpperCase && hasNumber && hasSpecial;
      if (!valid) {
        return { 'passwordStrength': true };
      }
    }
    return null;
  }

  passwordMatchValidator(group: AbstractControl): { [key: string]: any } | null {
    const password = group.get('password')?.value;
    const passwordConfirm = group.get('passwordConfirm')?.value;
    return password === passwordConfirm ? null : { 'mismatch': true };
  }

  onSubmit() {
    if (this.registerForm.valid) {
      // Ažuriraj `userType` pre slanja zahteva
      if (this.registerForm.get('userType')?.value === 'LEGAL ENTITY') {
        this.registerForm.patchValue({ userType: 'LEGAL_ENTITY' });
      }

      this.registerService.register(this.registerForm.value).subscribe(response => {
        alert(response.message);
      }, error => {
        alert('Registration failed');
      });
    } else {
      this.validateAllFormFields(this.registerForm);
    }
  }

  validateAllFormFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.get(field);
      if (control instanceof FormGroup) {
        this.validateAllFormFields(control);
      } else {
        control?.markAsTouched({ onlySelf: true });
      }
    });
  }
}
