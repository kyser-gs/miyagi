import { Component, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'add-new-user-dialog',
  imports: [CommonModule, FormsModule],
  templateUrl: './add-new-user-dialog.component.html',
  styleUrl: './add-new-user-dialog.component.scss'
})
export class AddNewUserDialogComponent {
  // Form data using signals
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
      address: {
        street: this.street(),
        apt: this.apt(),
        city: this.city(),
        state: this.state(),
        zipCode: this.zipCode()
      }
    };

    console.log('New user:', newUser);

    // TODO: Call your API service here to save the user
    // Example: this.userService.createUser(newUser).subscribe(...)

    this.resetForm();
    this.close();
  }
}
