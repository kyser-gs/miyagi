import { Component, EventEmitter, inject, Output, model } from '@angular/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from '@angular/material/input';
import { provideNativeDateAdapter } from '@angular/material/core';
import { NameInput } from "../../inputs/name-input/name-input";
import {UserService} from '../../../../../services/user.service';
import { FormsModule } from "@angular/forms";

export interface FilterValues {
  name?: string;
  startDate?: Date;
  endDate?: Date;
}

@Component({
  selector: 'filters-sidenav',
  imports: [MatFormFieldModule, MatDatepickerModule, MatInputModule, NameInput, FormsModule],
  providers: [provideNativeDateAdapter()],
  templateUrl: './filters-sidenav.html',
  styleUrl: './filters-sidenav.scss',
})
export class FiltersSidenav {
  private userService: UserService = inject(UserService);
  @Output() filterApplied = new EventEmitter<FilterValues>();
  name = model<string>('')
  startDate = model<Date>()
  endDate = model<Date>()

  submitFilter() {

    this.filterApplied.emit({
      name: this.name() || undefined,
      startDate: this.startDate(),
      endDate: this.endDate()
    });
  }
}
