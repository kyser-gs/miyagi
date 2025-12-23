import {ChangeDetectorRef, Component, inject, viewChild} from '@angular/core';
import {AddNewUserDialogComponent} from './components/dialogs/add-new-user-dialog/add-new-user-dialog.component';
import {UserViewModel, UserService} from '../../services/user.service';
import { UsersTable } from "./components/tables/users-table/users-table";
import {MatSidenavModule} from '@angular/material/sidenav';
import { FiltersSidenav, FilterValues } from "./components/sidenavs/filters-sidenav/filters-sidenav";
import { BirthMonthList } from "./components/lists/birth-month-list/birth-month-list";
import { PageEvent } from '@angular/material/paginator';
import {LocationDetails, LocationsSidenav} from "./components/sidenavs/locations-sidenav/locations-sidenav";
import { environment } from "../../../environments/environment";
import {GeocodeResponse, LocationService } from "../../services/location.service";
import { Observable } from "rxjs/internal/Observable";

@Component({
  selector: 'home',
  imports: [
    AddNewUserDialogComponent, UsersTable, MatSidenavModule, FiltersSidenav, BirthMonthList, LocationsSidenav
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  private userService: UserService = inject(UserService);
  private locationService: LocationService = inject(LocationService);
  private changeDetectorRef: ChangeDetectorRef = inject(ChangeDetectorRef);
  dataSource: UserViewModel[] = []
  totalCount: number = 0;
  pageSize: number = 10;
  currentFilters: FilterValues = {};
  monthCounts: number[] = Array(12).fill(0);
  locationNavOpened: boolean = false;
  locationDetails: LocationDetails = {name:'', address: '', coordinates: '', distance: ''};
  apiKey = environment.googleMapsApiKey;

  dialog = viewChild(AddNewUserDialogComponent);
  usersTable = viewChild(UsersTable);

  openDialog() {
    this.dialog()?.open();
  }

  ngOnInit() {
    this.fetchUsers();
  }

  onFiltersApplied(filters: FilterValues) {
    this.currentFilters = filters;
    // Reset paginator to page 0
    const paginator = this.usersTable()?.paginator();
    if (paginator) {
      paginator.pageIndex = 0;
    }
    this.fetchUsers(filters.name, filters.startDate, filters.endDate, 0, this.pageSize);
  }

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.fetchUsers(
      this.currentFilters.name,
      this.currentFilters.startDate,
      this.currentFilters.endDate,
      event.pageIndex,
      event.pageSize
    );
  }

  onUserClicked(user: UserViewModel){
    this.locationService.geocode(user.address).subscribe({
      next: (response) => {
        const location = response.results[0].geometry.location;
        this.locationDetails.coordinates = `${location.lat}, ${location.lng}`
      }
    })

    this.locationService.distance(user.address).subscribe({
      next: (response) => {
        this.locationDetails.distance = response.distance
      }
    })

    this.locationDetails.name = user.name;
    this.locationDetails.address = user.address;

    this.locationNavOpened = true;
  }

  onClose(){
    this.locationNavOpened = false;
  }

  fetchUsers(name?: string, startDate?: Date, endDate?: Date, page: number = 0, size: number = 5){
    this.userService.list(name, startDate, endDate, page, size).subscribe({
      next: (response) => {
        this.dataSource = response.users;
        this.totalCount = response.total;
        this.monthCounts = response.monthCounts;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Error loading users:', error);
      }
    });
  }
}
