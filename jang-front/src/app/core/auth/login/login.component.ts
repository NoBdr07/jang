import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { filter, Subscription } from 'rxjs';
import { LoginRequest } from '../../../shared/payload/login-request.interface';
import { NotificationService } from '../../../services/notification.service';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatFormFieldModule, MatInputModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit, OnDestroy {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);
  private notificationService = inject(NotificationService);

  currentUser$ = this.authService.currentUser$;
  private sub = new Subscription;

  form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  })

  ngOnInit(): void {
      this.sub = this.currentUser$.pipe(
        filter(user => !!user))
        .subscribe(() => this.router.navigate(['/dashboard']));      
  }

  ngOnDestroy(): void {
      this.sub.unsubscribe();
  }

  onSubmit(): void {
    if(this.form.valid) {
      const req = this.form.value as LoginRequest;
      this.authService.login(req).subscribe({
        error: () => {
          this.notificationService.error("Le login n'a pas fonctionné.");
        }
      })
    }
  }
  
}
