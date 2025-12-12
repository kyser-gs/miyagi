import {Component, Input } from '@angular/core';
import { MatTableModule} from '@angular/material/table';
import { UserViewModel } from '../../../../../services/user.service';
import { NgFor } from "@angular/common";

@Component({
  selector: 'users-table',
  imports: [MatTableModule, NgFor],
  templateUrl: './users-table.html',
  styleUrl: './users-table.scss',
})
export class UsersTable {
  displayedColumns: string[] = ['name', 'dateOfBirth', 'address'];
  @Input() dataSource: UserViewModel[] = [];
}
