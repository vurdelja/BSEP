import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RequestsComponent } from './requests/requests.component';
import { LogInComponent } from './log-in/log-in.component';
import { RegisterComponent } from './register/register.component';
import { UserProfileComponent } from './user-profile/user-profile/user-profile.component';
import { ProfilEditComponent } from './profil-edit/profil-edit/profil-edit.component';
import { ComercialComponent } from './comercial/comercial/comercial.component';


@NgModule({
  declarations: [
    AppComponent,
    RequestsComponent,
    LogInComponent,
    RegisterComponent,
    UserProfileComponent,
    ProfilEditComponent,
    ComercialComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
