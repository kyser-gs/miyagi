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
  private isAuthenticated = signal(false);

  constructor(private http: HttpClient) {}

  signIn(username: string, password: string): Observable<SignInResponse> {
    return this.http.post<SignInResponse>('/api/account/signin', {
      username,
      password
    });
  }

  signUp(username: string, password: string): Observable<SignInResponse> {
    return this.http.post<SignInResponse>('/api/account/signup', {
      username,
      password
    });
  }

  signOut(): Observable<any> {
    return this.http.post('/api/account/signout', {});
  }

  setAuthenticated(authenticated: boolean) {
    this.isAuthenticated.set(authenticated);
  }

  isLoggedIn() {
    return this.isAuthenticated();
  }
}
