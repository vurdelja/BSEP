import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';
import { Commercial } from '../../models/commercial.model';
import { CommercialService } from '../../services/commercial.service';

@Component({
  selector: 'app-employee-profile',
  templateUrl: './employee-profile.component.html',
  styleUrls: ['./employee-profile.component.css']
})
export class EmployeeProfileComponent implements OnInit {
  user: User | undefined;
  userId: number;
  commercials: Commercial[] = [];

  constructor(private route: ActivatedRoute, 
    private userService: UserService, 
    private commercialService: CommercialService,
  private router: Router) {
    this.userId = +this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit() {
    this.loadUser();
  }

  loadUser() {
    this.userService.getUserById(this.userId).subscribe((data: User) => {
      this.user = data;
    });
  }

  updateUser() {
    if (this.user) {
      this.userService.updateUser(this.user).subscribe((data: User) => {
        this.user = data;
        alert('User updated successfully');
      });
    }
  }

  loadCommercials() {
    this.commercialService.getCommercials().subscribe((data: Commercial[]) => {
      this.commercials = data;
    });
  }

  navigateToCommercialRequests() {
    this.router.navigate(['/commercial-requests']);
  }
}
