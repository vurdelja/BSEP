import { Component, OnInit } from '@angular/core';
import { RegistrationService } from '../../services/registration.service';

@Component({
  selector: 'app-admin-profile',
  templateUrl: './admin-profile.component.html',
  styleUrls: ['./admin-profile.component.css']
})
export class AdminProfileComponent implements OnInit {
  requests: any[] = [];

  constructor(private registrationService: RegistrationService) {}

  ngOnInit() {
    this.loadRequests();
  }

  loadRequests() {
    this.registrationService.getAllRequests().subscribe(
      (data) => {
        this.requests = data;
      },
      (error) => {
        console.error('Failed to load requests', error);
      }
    );
  }

  approveRequest(id: number) {
    this.registrationService.approveRequest(id).subscribe(
      response => {
        console.log('Request approved:', response);
        this.loadRequests(); // Refresh the list
      },
      error => {
        console.error('Error approving request', error);
      }
    );
  }

  rejectRequest(id: number) {
    const reason = prompt('Please enter the reason for rejection:');
    if (reason) {
      this.registrationService.rejectRequest(id, reason).subscribe(
        response => {
          console.log('Request rejected:', response);
          this.loadRequests(); // Refresh the list
        },
        error => {
          console.error('Error rejecting request', error);
        }
      );
    }
  }
}
