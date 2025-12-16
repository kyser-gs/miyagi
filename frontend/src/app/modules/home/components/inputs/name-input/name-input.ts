import { Component, model } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";

@Component({
  selector: 'name-input',
  imports: [MatFormFieldModule, MatInputModule, FormsModule],
  templateUrl: './name-input.html',
  styleUrl: './name-input.scss',
})
export class NameInput {
  name = model<string>('');
}
