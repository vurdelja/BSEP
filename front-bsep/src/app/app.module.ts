import { NgModule } from '@angular/core';
import { RecaptchaModule } from 'ng-recaptcha';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { CustomCaptchaComponent } from './components/custom-captcha/custom-captcha.component';
import { ProfilEditComponent } from './profil-edit/profil-edit/profil-edit.component';
import { RefreshTokenDialogComponent } from './refresh-token-dialog/refresh-token-dialog.component';
import { UserProfileComponent } from './user-profile/user-profile/user-profile.component';
import { AuthInterceptorService } from './services/auth-interceptor.service';



@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    HomePageComponent,
    CustomCaptchaComponent,
    ProfilEditComponent,
    UserProfileComponent,
    RefreshTokenDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    RecaptchaModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
