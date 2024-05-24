import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppModule } from './app.module';
import { RegistrationComponent } from './components/registration/registration.component';
import { AdminProfileComponent } from './components/admin-profile/admin-profile.component';
import { LoginComponent } from './components/login/login.component';
import { HomePageComponent } from './components/home-page/home-page.component';


const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'admin', component: AdminProfileComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrationComponent }
];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
