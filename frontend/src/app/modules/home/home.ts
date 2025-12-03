import {Component, viewChild} from '@angular/core';
import {AddNewUserDialogComponent} from './components/dialogs/add-new-user-dialog/add-new-user-dialog.component';

@Component({
  selector: 'home',
  imports: [
    AddNewUserDialogComponent
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {

  dialog = viewChild(AddNewUserDialogComponent);

  // Method to open the dialog
  openDialog() {
    this.dialog()?.open();
  }
}
