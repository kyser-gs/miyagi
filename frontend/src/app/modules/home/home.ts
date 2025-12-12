import {ChangeDetectorRef, Component, inject, viewChild} from '@angular/core';
import {AddNewUserDialogComponent} from './components/dialogs/add-new-user-dialog/add-new-user-dialog.component';
import {UserViewModel, UserService} from '../../services/user.service';
import { UsersTable } from "./components/tables/users-table/users-table";

@Component({
  selector: 'home',
  imports: [
    AddNewUserDialogComponent, UsersTable
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  private userService: UserService = inject(UserService);
  private changeDetectorRef: ChangeDetectorRef = inject(ChangeDetectorRef);
  dataSource: UserViewModel[] = []

  dialog = viewChild(AddNewUserDialogComponent);

  // Method to open the dialog
  openDialog() {
    this.dialog()?.open();
  }

  ngOnInit() {
    this.fetchUsers();
  }

  fetchUsers(){
    this.userService.list().subscribe({
      next: (users: UserViewModel[]) => {
        this.dataSource = users;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Error loading users:', error);
      }
    });
  }
}
