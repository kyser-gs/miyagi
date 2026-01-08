import { Component, computed, inject, input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../../../services/auth.service';

@Component({
  selector: 'auth-dialog',
  imports: [CommonModule, FormsModule],
  templateUrl: './auth-dialog.html',
  styleUrl: './auth-dialog.scss',
})
export class AuthDialog {
  private authService = inject(AuthService);

  isSignUp = input.required<boolean>();

  username = signal('');
  password = signal('');
  errorMessage = signal('');
  isLoading = signal(false);
  isOpen = signal(false);

  title = computed(() => this.isSignUp() ? 'Sign Up' : 'Sign In');
  submitButtonText = computed(() => {
    if (this.isLoading()) {
      return this.isSignUp() ? 'Signing Up...' : 'Signing In...';
    }
    return this.isSignUp() ? 'Sign Up' : 'Sign In';
  });

  open() {
    this.isOpen.set(true);
    this.errorMessage.set('');
  }

  close() {
    this.isOpen.set(false);
    this.username.set('');
    this.password.set('');
    this.errorMessage.set('');
  }

  isFormValid = computed(() => {
    return this.username().trim() !== '' &&
           this.password().trim() !== ''
  });

  onSubmit() {
    if (!this.isFormValid()) return;
    this.isLoading.set(true);
    this.errorMessage.set('');

    const authMethod = this.isSignUp()
      ? this.authService.signUp(this.username(), this.password())
      : this.authService.signIn(this.username(), this.password());

    authMethod.subscribe({
      next: (response) => {
        this.isLoading.set(false);
        if (response.success) {
          this.authService.setAuthenticated(true);
          this.close();
        } else {
          this.errorMessage.set(response.message || `${this.isSignUp() ? 'Sign up' : 'Login'} failed`);
        }
      },
      error: (error) => {
        this.isLoading.set(false);
        this.errorMessage.set(error.error?.message || `An error occurred during ${this.isSignUp() ? 'sign up' : 'login'}`);
      }
    });
  }
}
