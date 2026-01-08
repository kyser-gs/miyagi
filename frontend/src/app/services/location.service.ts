import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from "../../environments/environment";

export interface GeocodeResponse{
  results: {
    geometry : {
      location : {
        lat : number,
        lng : number
      }
    },
  }[]
}

export interface DistanceMatrixResponse{
  rows: {
    elements: {
      distance: {
        text: string,
        value: number
      },
    }[]
  }[]
}

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  constructor(private http: HttpClient) {}

  geocode(address: string): Observable<GeocodeResponse> {
    return this.http.get<GeocodeResponse>("https://maps.googleapis.com/maps/api/geocode/json", {
      params: {
        address: address,
        key: environment.googleMapsApiKey
      }
    });
  }

  distance(address: string): Observable<{distance: string}> {
    return this.http.get<{distance: string}>("/api/location/distance", {
      params: {
        destinations: address,
        origins: "1901 L St NW, Washington, D.C., 20036",
        units: "imperial",
        key: environment.googleMapsApiKey
      }
    });
  }
}
