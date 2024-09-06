import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-protected',
  templateUrl: './protected.component.html',
  styleUrls: ['./protected.component.css']
})
export class ProtectedComponent implements OnInit {
  accessToken: string | null = '';
  refreshToken: string | null = '';
  message: string = '';

  constructor(private authService: AuthService, private http: HttpClient) {}

  ngOnInit(): void {
    this.loadTokens();
  }

  // Load tokens from localStorage
  loadTokens(): void {
    this.accessToken = this.authService.getAccessToken();
    this.refreshToken = this.authService.getRefreshToken();
  }

  // Simulate an API call to test token expiration and refresh
  simulateApiCall(): void {
    this.http.get('http://localhost:8080/protected-api') // Change the endpoint to your backend API
      .subscribe(
        (response) => {
          this.message = 'API call was successful!';
        },
        (error) => {
          this.message = 'API call failed: ' + error.message;
        }
      );
  }

  // Manually refresh the token
  refreshAccessToken(): void {
    this.authService.refreshToken().subscribe(
      (newAccessToken) => {
        if (newAccessToken) {
          this.message = 'Access token refreshed successfully!';
          this.loadTokens(); // Reload the tokens after refresh
        } else {
          this.message = 'Failed to refresh the access token.';
        }
      },
      (error) => {
        this.message = 'Error during token refresh: ' + error.message;
      }
    );
  }
}
