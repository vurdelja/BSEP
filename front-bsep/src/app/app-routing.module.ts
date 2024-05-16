import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RequestsComponent } from './requests/requests.component';
import { RegisterComponent } from './register/register.component';
import { UserProfileComponent } from './user-profile/user-profile/user-profile.component';
import { ProfilEditComponent } from './profil-edit/profil-edit/profil-edit.component';
import { ComercialComponent } from './comercial/comercial/comercial.component';

const routes: Routes = [

  { path: '', component: RegisterComponent },
  {path : 'user-profile', component:UserProfileComponent},
  { path: 'edit-profile', component:ProfilEditComponent},
  { path: 'comercial', component:ComercialComponent},

];







@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
