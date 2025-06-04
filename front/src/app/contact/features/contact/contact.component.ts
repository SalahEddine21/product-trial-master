import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [ReactiveFormsModule, ToastModule, InputTextModule, ButtonModule, CommonModule],
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.css'
})
export class ContactComponent {
  contactForm!: FormGroup;

  constructor(private fb: FormBuilder, private messageService: MessageService) {
    this.initForm();
  }

  initForm(){
    this.contactForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      message: ['', [Validators.required, Validators.maxLength(10)]],
    });
  }

  get emailControl() {
    return this.contactForm.get('email');
  }

  get messageControl() {
    return this.contactForm.get('message');
  }

  onSubmit(): void {
    if (this.contactForm.valid) {
      this.messageService.add({
        severity: 'success',
        summary: 'Success',
        detail: 'Demande de contact envoyée avec succès',
      });
      this.clearForm();
    }
  }

  clearForm(): void {
    this.contactForm.reset();
  }
}
