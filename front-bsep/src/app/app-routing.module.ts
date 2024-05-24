import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppModule } from './app.module';
import { RegistrationComponent } from './components/registration/registration.component';
import { AdminProfileComponent } from './components/admin-profile/admin-profile.component';


const routes: Routes = [
  { path: '', component: RegistrationComponent },
  { path: '\admin', component: AdminProfileComponent },
];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
