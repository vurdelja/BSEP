// refresh-token-dialog.component.ts
import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-refresh-token-dialog',
  template: `
    <h1 mat-dialog-title>Session Expired</h1>
    <div mat-dialog-content>
      <p>Your session has expired. Would you like to refresh the token?</p>
    </div>
    <div mat-dialog-actions>
      <button mat-button (click)="onCancel()">No</button>
      <button mat-button (click)="onRefresh()">Yes</button>
    </div>
  `
})
export class RefreshTokenDialogComponent {
  constructor(private dialogRef: MatDialogRef<RefreshTokenDialogComponent>) {}

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onRefresh(): void {
    this.dialogRef.close(true);
  }
}
