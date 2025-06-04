import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../data-access/authentication.service';
import { AuthenticationRequest } from '../data-access/authenticationRequest';
import { NgClass, NgFor, NgIf } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [NgClass, NgIf, ReactiveFormsModule, ButtonModule, InputTextModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  loginForm!: FormGroup;
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: AuthenticationService,
  ) {}

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
        username: ['', Validators.required],
        password: ['', Validators.required]
    });
  }

  get form() { return this.loginForm.controls; }

  onSubmit() {
    this.submitted = true;
    if (this.loginForm.invalid) {
        return;
    }
    this.loading = true;
    let auth = new AuthenticationRequest();
    auth.username = this.form.username.value;
    auth.password = this.form.password.value
    this.authenticationService.login(auth).subscribe({
      next: (token) => {
        sessionStorage.setItem('token', token);
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.loading = false;
        console.error('Login error:', err);
      }
    });    
  }
}
