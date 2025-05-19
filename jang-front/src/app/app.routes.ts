import { Routes } from '@angular/router';
import { QuizComponent } from './feature/quiz/quiz.component';
import { authGuard } from './core/guard/auth.guard';

export const routes: Routes = [
    {
        path: '',
        component: QuizComponent
    },
    {
        path: 'login',
        loadComponent: () => 
            import('./core/auth/login/login.component')
            .then((c) => c.LoginComponent)
        
    },
    {
        path: 'register',
        loadComponent: () => 
            import('./core/auth/register/register.component')
            .then((c) => c.RegisterComponent)
        
    },
    {
        path: 'dashboard',
        loadComponent: () => 
            import('./feature/dashboard/dashboard.component')
            .then((c) => c.DashboardComponent),
            canActivate: [authGuard]
    }

];
