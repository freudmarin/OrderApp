import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-dialog',
  template: `
    <h2 mat-dialog-title class="dialog-title">{{ data.title }}</h2>
    <mat-dialog-content>
      <div class="icon-container">
        <mat-icon class="dialog-icon">error_outline</mat-icon>
      </div>
      <p class="dialog-message">{{ data.message }}</p>
    </mat-dialog-content>
    <mat-dialog-actions>
      <button mat-raised-button color="primary" mat-dialog-close class="dialog-button">OK</button>
    </mat-dialog-actions>
  `,
  styleUrls: ['./dialog.component.scss']
})
export class DialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: { title: string; message: string }) {}
}
