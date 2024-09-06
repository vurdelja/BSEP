import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { UserProfileComponent } from './user-profile/user-profile/user-profile.component';
import { ProtectedComponent } from './components/protected/protected.component';
import { ProfilEditComponent } from './profil-edit/profil-edit/profil-edit.component';
import { AuthGuard } from './auth.guard';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegistrationComponent },
  { path: 'user-profile/:id', component: UserProfileComponent },
  { path: 'edit-profile', component: ProfilEditComponent },
  { path: 'protected', component: ProtectedComponent, canActivate: [AuthGuard] },  // Zaštićena ruta
  { path: '**', redirectTo: 'protected', pathMatch: 'full' }
];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
