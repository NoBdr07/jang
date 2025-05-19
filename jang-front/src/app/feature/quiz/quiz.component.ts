import { Component } from '@angular/core';
import {
  BehaviorSubject,
  combineLatest,
  map,
  Observable,
  switchMap,
} from 'rxjs';
import { QuestionDTO } from '../../shared/models/question.model';
import { Page } from '../../shared/models/page.model';
import { QuestionService } from '../../services/question-service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { FilterComponent } from '../../shared/components/filter/filter.component';
import { MatCardModule } from '@angular/material/card';
import { QuestionCardComponent } from '../../shared/components/filter/question-card/question-card.component';

@Component({
  selector: 'app-quiz',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    FilterComponent,
    QuestionCardComponent,
    MatCardModule,
  ],
  templateUrl: './quiz.component.html',
  styleUrl: './quiz.component.scss',
})
export class QuizComponent {
  // Nombre max de questions chargées
  private maxQuestions = 20;

  // Streams de critère
  private niveau$ = new BehaviorSubject<number[]>([]);
  private topics$ = new BehaviorSubject<string[]>([]);
  private page$ = new BehaviorSubject<number>(0);

  // Index de la question dans la page (0 à maxQuestions-1)
  private index$  = new BehaviorSubject<number>(0);

  // Stream principal : dès qu'un critère change, on relance la requête
  questionsPage$: Observable<Page<QuestionDTO>> = combineLatest([
    this.niveau$,
    this.topics$,
    this.page$,
  ]).pipe(
    switchMap(([niveaux, topics, page]) =>
      this.questionService.getFilteredQuestions(
        niveaux,
        topics,
        page,
        this.maxQuestions
      )
    )
  );

  // On en déduit la question courante
  currentQuestion$: Observable<QuestionDTO | undefined> =
    combineLatest([this.questionsPage$, this.index$])
    .pipe(
      map(([page, idx]) => page.content[idx] )
    );

  constructor(private questionService: QuestionService) {}

  // Méthodes appelées par le composant enfant filter
  onFilterChange(filter: { niveau: number[]; topics: string[] }) {
    console.log("Appel de onFilterChange dans quiz component");
    this.niveau$.next(filter.niveau);
    this.topics$.next(filter.topics);
    this.page$.next(0);
    this.index$.next(0);
  }

  nextPage() {
    const nextPage = this.page$.value + 1;
    this.page$.next(nextPage);
  }

  nextQuestion() {
    const idx = this.index$.value;
    if( idx < this.maxQuestions) {
      this.index$.next(idx + 1);
    } else {
      this.page$.next(this.page$.value + 1);
      this.index$.next(0);
    }
    
  }
}
