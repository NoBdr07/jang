import { Component } from '@angular/core';
import {
  BehaviorSubject,
  combineLatest,
  debounceTime,
  distinctUntilChanged,
  map,
  Observable,
  switchMap,
  take,
} from 'rxjs';
import { QuestionDTO } from '../../shared/models/question.model';
import { Page } from '../../shared/models/page.model';
import { QuestionService } from '../../services/question.service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { FilterComponent } from '../../shared/components/filter/filter.component';
import { MatCardModule } from '@angular/material/card';
import { QuestionCardComponent } from '../../shared/components/question-card/question-card.component';
import { ProgressComponent } from '../../shared/components/progress/progress.component';
import { MeResponse } from '../../shared/payload/me-response.interface';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-quiz',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    FilterComponent,
    QuestionCardComponent,
    ProgressComponent,
    MatCardModule,
  ],
  templateUrl: './quiz.component.html',
  styleUrl: './quiz.component.scss',
})
export class QuizComponent {
  // Nombre max de questions chargées
  maxQuestions = 10;

  // Streams de critère
  private niveau$ = new BehaviorSubject<number[]>([]);
  private topics$ = new BehaviorSubject<string[]>([]);
  private page$ = new BehaviorSubject<number>(0);

  // Index de la question dans la page (0 à maxQuestions-1)
  private index$ = new BehaviorSubject<number>(0);

  // Note de 0 à 2 pour chaque question
  evaluations = new Map<number, number>();

  // flux utilisateur (null si visiteur)
  user$: Observable<MeResponse | null> = this.authService.currentUser$;
  isVisitor$ = this.user$.pipe(map((user) => user == null));

  // Stream principal : dès qu'un critère change, on relance la requête
  questionsPage$: Observable<Page<QuestionDTO>> = combineLatest([
    this.niveau$.pipe(debounceTime(500), distinctUntilChanged()),
    this.topics$.pipe(debounceTime(500), distinctUntilChanged()),
    this.page$,
    this.user$,
  ]).pipe(
    switchMap(([niveaux, topics, page, user]) => {
      const size = this.maxQuestions;

      if (!user) {
        return this.questionService.getFilteredQuestions(
          niveaux,
          topics,
          page,
          size,
          true
        );
      }

      return this.questionService.getAdaptiveQuestions(niveaux, topics, size);
    })
  );

  // On en déduit la question courante
  currentQuestion$: Observable<QuestionDTO | undefined> = combineLatest([
    this.questionsPage$,
    this.index$,
  ]).pipe(map(([page, idx]) => page.content[idx]));

  constructor(
    private questionService: QuestionService,
    private authService: AuthService
  ) {}

  // Méthodes appelées par le composant enfant filter
  onFilterChange(filter: { niveau: number[]; topics: string[] }) {
    console.log('Appel de onFilterChange dans quiz component');
    this.niveau$.next(filter.niveau);
    this.topics$.next(filter.topics);
    this.page$.next(0);
    this.index$.next(0);
    this.evaluations.clear();
  }

  // Série suivante
  nextPage() {
    this.finishSeries();
  }

  // Question suivante
  nextQuestion() {
    const idx = this.index$.value;
    this.index$.next(idx + 1);
  }

  // Attribue une note a une question dans l'historique local
  evaluate(note: number, question: QuestionDTO) {
    this.evaluations.set(question.id, note);
  }

  // Pour envoyer les stats courantes au composant enfant progress
  getStats() {
    let ok = 0,
      bof = 0,
      ko = 0;
    for (const note of this.evaluations.values()) {
      if (note == 2) ok++;
      else if (note == 1) bof++;
      else ko++;
    }
    return { ok, bof, ko };
  }

  // Pour envoyer l'index courant aux composants enfants question-card et progress
  get questionIndex() {
    return this.index$.value + 1;
  }

  // Pour envoyer les resultats d'une série quand utilisateur connecté
  private finishSeries() {
    console.log('appel de finishSeries');
    const attempts = Array.from(this.evaluations).map(
      ([questionId, score]) => ({ questionId, score })
    );

    this.user$.pipe(take(1)).subscribe((user) => {
      const afterPost = () => {
        this.evaluations.clear();
        this.page$.next(this.page$.value + 1);
        this.index$.next(0);
      };

      console.log('user = ' + user);
      if (user) {
        this.questionService.sendSeriesResult(attempts).subscribe({
          next: afterPost,
          error: (err) => {
            console.error(err);
            afterPost();
          },
        });
      } else {
        afterPost();
      }
    });
  }
}
