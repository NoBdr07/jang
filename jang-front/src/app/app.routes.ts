import { Routes } from '@angular/router';
import { QuizComponent } from './quiz/quiz.component';
import { authGuard } from './core/guard/auth.guard';

export const routes: Routes = [
    {
        path: '',
        component: QuizComponent
    },
    {
        path: 'login',
        loadComponent: () => 
            import('./auth/login/login.component')
            .then((c) => c.LoginComponent)
        
    },
    {
        path: 'register',
        loadComponent: () => 
            import('./auth/register/register.component')
            .then((c) => c.RegisterComponent)
        
    },
    {
        path: 'dashboard',
        loadComponent: () => 
            import('./dashboard/dashboard.component')
            .then((c) => c.DashboardComponent),
            canActivate: [authGuard]
    }

];
