import { Component, signal, computed, inject, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {UserResponse, UserService} from '../../../../../services/user.service';

@Component({
  selector: 'add-new-user-dialog',
  imports: [CommonModule, FormsModule],
  templateUrl: './add-new-user-dialog.component.html',
  styleUrl: './add-new-user-dialog.component.scss'
})
export class AddNewUserDialogComponent {
  private userService: UserService = inject(UserService);
  @Output() userCreated = new EventEmitter<void>();

  firstName = signal('');
  lastName = signal('');
  dateOfBirth = signal('');
  street = signal('');
  apt = signal('');
  city = signal('');
  state = signal('');
  zipCode = signal('');

  // Dialog state
  isOpen = signal(false);

  maxDate = computed(() => {
    const today = new Date();
    return today.toISOString().split('T')[0];
  });

  // Validation computed signals
  isZipCodeValid = computed(() => {
    const zip = this.zipCode().trim();
    return zip === '' || /^\d{5}$/.test(zip);
  });

  isDateOfBirthValid = computed(() => {
    const dob = this.dateOfBirth().trim();
    if (dob === '') return true;

    const selectedDate = new Date(dob);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return selectedDate <= today;
  });

  isFormValid = computed(() => {
    return this.firstName().trim() !== '' &&
           this.lastName().trim() !== '' &&
           this.dateOfBirth().trim() !== '' &&
           this.isDateOfBirthValid() &&
           this.street().trim() !== '' &&
           this.city().trim() !== '' &&
           this.state().trim() !== '' &&
           this.zipCode().trim() !== '' &&
           this.isZipCodeValid();
  });

  open() {
    this.isOpen.set(true);
  }

  close() {
    this.isOpen.set(false);
  }

  resetForm() {
    this.firstName.set('');
    this.lastName.set('');
    this.dateOfBirth.set('');
    this.street.set('');
    this.apt.set('');
    this.city.set('');
    this.state.set('');
    this.zipCode.set('');
  }

  onSubmit() {
    const newUser = {
      firstName: this.firstName(),
      lastName: this.lastName(),
      dateOfBirth: this.dateOfBirth(),
      street: this.street(),
      apt: this.apt(),
      city: this.city(),
      state: this.state(),
      zipCode: this.zipCode()
    };

    this.userService.save(newUser).subscribe({
      next: () => {
          this.resetForm();
          this.close();
          // Delay to allow RabbitMQ consumer to index to Elasticsearch
          setTimeout(() => {
            this.userCreated.emit();
          }, 1000);
      },
      error: (error) => {
        console.error('Error saving user:', error);
      }
    });
  }
}
