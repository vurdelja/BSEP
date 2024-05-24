import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './components/registration/registration.component';
import { AdminProfileComponent } from './components/admin-profile/admin-profile.component';
import { LoginComponent } from './components/login/login.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { UserProfileComponent } from './user-profile/user-profile/user-profile.component';
import { ProfilEditComponent } from './profil-edit/profil-edit/profil-edit.component';
import { ComercialComponent } from './comercial/comercial/comercial.component';


const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'admin', component: AdminProfileComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrationComponent },
  { path: 'user-profile', component: UserProfileComponent},
  { path: 'edit-profile', component: ProfilEditComponent},
  { path: 'comercial', component: ComercialComponent},


];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
