import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SignInRequest {
  username: string;
  password: string;
}

export interface SignInResponse {
  success: boolean;
  username?: string;
  message?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) {}

  private apiUrl = '/api/account';
  private isAuthenticated = signal(this.checkAuthState());

  private checkAuthState(): boolean {
    return localStorage.getItem('isAuthenticated') === 'true';
  }

  signIn(username: string, password: string): Observable<SignInResponse> {
    return this.http.post<SignInResponse>(`${this.apiUrl}/signin`, {
      username,
      password
    });
  }

  signUp(username: string, password: string): Observable<SignInResponse> {
    return this.http.post<SignInResponse>(`${this.apiUrl}/signup`, {
      username,
      password
    });
  }

  signOut(): Observable<any> {
    return this.http.post(`${this.apiUrl}/signout`, {});
  }

  setAuthenticated(authenticated: boolean) {
    this.isAuthenticated.set(authenticated);
    if (authenticated) {
      localStorage.setItem('isAuthenticated', 'true');
    } else {
      localStorage.removeItem('isAuthenticated');
    }
  }

  isLoggedIn() {
    return this.isAuthenticated();
  }
}
