import { NgModule } from '@angular/core';
import { RecaptchaModule } from 'ng-recaptcha';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { AdminProfileComponent } from './components/admin-profile/admin-profile.component';
import { LoginComponent } from './components/login/login.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { EmployeeProfileComponent } from './components/employee-profile/employee-profile.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { CustomCaptchaComponent } from './components/custom-captcha/custom-captcha.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    AdminProfileComponent,
    LoginComponent,
    HomePageComponent,
    EmployeeProfileComponent,
    UserProfileComponent,
    CustomCaptchaComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    RecaptchaModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
