import { Component } from '@angular/core';
import { QuizComponent } from "./feature/quiz/quiz.component";
import { AuthService } from './services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'jang';

  constructor(private auth: AuthService) {
    // récupère l'état de connexion actuel
    this.auth.me().subscribe();
  }
}
