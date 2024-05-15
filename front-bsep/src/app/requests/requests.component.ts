import { Component, OnInit } from '@angular/core';
import { RequestService } from '../request.service';
import { Request } from '../request';

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrl: './requests.component.css'
})
export class RequestsComponent implements OnInit {
  requests: Request[] = [];

  constructor(private requestService: RequestService) { }

  ngOnInit(): void {
    this.getRequests();
  }

  getRequests(): void {
    this.requestService.getAllRequests().subscribe(data => {
      this.requests = data;
    });
  }

}
