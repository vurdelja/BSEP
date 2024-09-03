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
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { CustomCaptchaComponent } from './components/custom-captcha/custom-captcha.component';
import { ProfilEditComponent } from './profil-edit/profil-edit/profil-edit.component';
import { ComercialComponent } from './comercial/comercial/comercial.component';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { RefreshTokenDialogComponent } from './refresh-token-dialog/refresh-token-dialog.component';



@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    HomePageComponent,
    UserProfileComponent,
    CustomCaptchaComponent,
    UserProfileComponent,
    ProfilEditComponent,
    ComercialComponent,
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
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
