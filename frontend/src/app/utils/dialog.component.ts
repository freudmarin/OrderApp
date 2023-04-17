import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-dialog',
  template: `
    <h2 mat-dialog-title>{{ data.title }}</h2>
    <mat-dialog-content>
      <mat-icon>error_outline</mat-icon>
      <p>{{ data.message }}</p>
    </mat-dialog-content>
    <mat-dialog-actions>
      <button mat-button mat-dialog-close>OK</button>
    </mat-dialog-actions>
  `,
  styleUrls: ['./dialog.component.scss']
})
export class DialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: { title: string; message: string }) {}
}
