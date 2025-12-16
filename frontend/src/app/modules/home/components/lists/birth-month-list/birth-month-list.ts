import { NgFor, NgClass } from "@angular/common";
import { Component, computed, input } from '@angular/core';
import {UserViewModel} from '../../../../../services/user.service';

@Component({
  selector: 'birth-month-list',
  imports: [NgFor, NgClass],
  templateUrl: './birth-month-list.html',
  styleUrl: './birth-month-list.scss',
})
export class BirthMonthList {
  dataSource = input<UserViewModel[]>([]);
  monthCounts = input<number[]>([]);

  months: string[] = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  monthData = computed(() => {
    return this.months.map((month, index) => ({
      name: month,
      count: this.monthCounts()[index]
    }))
  })
}
