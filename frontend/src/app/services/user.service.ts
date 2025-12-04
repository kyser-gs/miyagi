import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

export interface UserResponse {
  success: boolean;
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

  list(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/list`);
  }

  get(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/show/${id}`);
  }

  delete(id: number): Observable<UserResponse> {
    return this.http.delete<UserResponse>(`${this.apiUrl}/delete/${id}`);
  }
}
