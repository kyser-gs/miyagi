import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  street: string;
  apt?: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface UserViewModel {
  name: string;
  dateOfBirth: string;
  address: string;
}

export interface UserListResponse {
  users: UserViewModel[];
  total: number;
  monthCounts: number[];
}

export interface UserResponse {
  user?: User;
  errors?: any[];
  message?: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/api/user';

  constructor(private http: HttpClient) {}

  save(user: User): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.apiUrl}/save`, user);
  }

  list(name?: string, startDate?: Date, endDate?: Date, page: number = 0, size: number = 10): Observable<UserListResponse> {
    let params = new HttpParams();

    if (name) {
      params = params.set('name', name);
    }
    if (startDate) {
      params = params.set('startDate', startDate.toISOString().split('T')[0]);
    }
    if (endDate) {
      params = params.set('endDate', endDate.toISOString().split('T')[0]);
    }

    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    return this.http.get<UserListResponse>(`${this.apiUrl}/list`, { params });
  }
}
