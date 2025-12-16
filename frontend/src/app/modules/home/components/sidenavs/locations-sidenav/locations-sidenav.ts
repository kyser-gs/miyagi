import { Component, input, output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import {UserViewModel} from '../../../../../services/user.service';

@Component({
  selector: 'locations-sidenav',
  imports: [MatIconModule],
  templateUrl: './locations-sidenav.html',
  styleUrl: './locations-sidenav.scss',
})
export class LocationsSidenav {
  close = output<void>();
  locationDetails = input<LocationDetails>({name:'', address: '', coordinates: '', distance: 0});

  onClose(){
    this.close.emit()
  }
}

export interface LocationDetails {
  name: string;
  address: string;
  coordinates: string;
  distance: number;
}
