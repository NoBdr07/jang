import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { filter, Subscription } from 'rxjs';
import { NotificationService } from '../../../services/notification.service';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RegisterRequest } from '../../../shared/payload/register-request.interface';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatButtonModule, MatFormFieldModule, MatInputModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  private fb = inject(FormBuilder);
    private router = inject(Router);
    private authService = inject(AuthService);
    private notificationService = inject(NotificationService);
  
    currentUser$ = this.authService.currentUser$;
    private sub = new Subscription;
  
    form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', Validators.required],
      password: ['', Validators.required]
    })
  
    ngOnInit(): void {
       
    }
  
    ngOnDestroy(): void {
        this.sub.unsubscribe();
    }
  
    onSubmit(): void {
      if(this.form.valid) {
        const req = this.form.value as RegisterRequest;
        this.authService.register(req).subscribe({
          error: () => {
            this.notificationService.error("Le register n'a pas fonctionné.");
          }
        })
      }
    }

}
