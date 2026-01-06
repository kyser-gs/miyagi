import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../../../services/auth.service';

@Component({
  selector: 'sign-in-dialog',
  imports: [CommonModule, FormsModule],
  templateUrl: './sign-in-dialog.html',
  styleUrl: './sign-in-dialog.scss',
})
export class SignInDialog {
  private authService = inject(AuthService);

  username = signal('');
  password = signal('');
  errorMessage = signal('');
  isLoading = signal(false);

  isOpen = signal(false);

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

    this.authService.signIn(this.username(), this.password()).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        if (response.success) {
          this.authService.setAuthenticated(true);
          this.close();
        } else {
          this.errorMessage.set(response.message || 'Login failed');
        }
      },
      error: (error) => {
        this.isLoading.set(false);
        this.errorMessage.set(error.error?.message || 'An error occurred during login');
      }
    });
  }
}
