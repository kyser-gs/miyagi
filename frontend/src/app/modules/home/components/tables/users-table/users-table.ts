import {Component, computed, input, output, viewChild } from '@angular/core';
import { MatTableModule} from '@angular/material/table';
import { UserViewModel } from '../../../../../services/user.service';
import { NgFor, NgIf } from "@angular/common";
import { MatPaginator, MatPaginatorModule, PageEvent } from "@angular/material/paginator";

export enum ColumnNames{
  name = 'Name',
  dateOfBirth = 'DOB',
  address = 'Address'
}

@Component({
  selector: 'users-table',
  imports: [MatTableModule, NgFor, NgIf, MatPaginatorModule],
  templateUrl: './users-table.html',
  styleUrl: './users-table.scss',
})
export class UsersTable {
  displayedColumns: string[] = ['name', 'dateOfBirth', 'address'];
  columnNames = ColumnNames;
  dataSource = input<UserViewModel[]>([]);
  totalCount = input<number>(0);
  isTableEmpty = computed(() => this.dataSource().length === 0);

  paginator = viewChild(MatPaginator);
  pageChange = output<PageEvent>();
  userClicked = output<UserViewModel>();

  onClick(user: UserViewModel){
    this.userClicked.emit(user);
  }

  onPageChange(event: PageEvent) {
    this.pageChange.emit(event);
  }

  getColumnName(column: string): string {
    return this.columnNames[column as keyof typeof ColumnNames];
  }
}


