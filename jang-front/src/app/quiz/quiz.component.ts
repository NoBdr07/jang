import { Component } from '@angular/core';
import {
  BehaviorSubject,
  combineLatest,
  map,
  Observable,
  switchMap,
} from 'rxjs';
import { QuestionDTO } from '../models/question.model';
import { Page } from '../models/page.model';
import { QuestionService } from '../services/question-service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { FilterComponent } from "../filter/filter.component";
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-quiz',
  standalone: true,
  imports: [CommonModule, MatButtonModule, FilterComponent, MatCardModule],
  templateUrl: './quiz.component.html',
  styleUrl: './quiz.component.scss',
})
export class QuizComponent {
  // Nombre max de questions chargées
  private maxQuestions = 20 ;

  // Streams de critère
  private niveau$ = new BehaviorSubject<number[]>([]);
  private topics$ = new BehaviorSubject<string[]>([]);
  private page$ = new BehaviorSubject<number>(0);

  // Stream principal : dès qu'un critère change, on relance la requête
  questionsPage$: Observable<Page<QuestionDTO>> = combineLatest([
    this.niveau$,
    this.topics$,
    this.page$,
  ]).pipe(
    switchMap(([niveaux, topics, page]) =>
      this.questionService.getFilteredQuestions(niveaux, topics, page, this.maxQuestions)
    )
  );

  // On en déduit la question courante
  currentQuestion$: Observable<QuestionDTO | undefined> =
    this.questionsPage$.pipe(map((page) => page.content[0]));

  constructor(private questionService: QuestionService) {}

  // Méthodes appelées par le composant enfant filter
  onFilterChange(filter: {niveau: number[]; topics: string[]}) {
    this.niveau$.next(filter.niveau);
    this.topics$.next(filter.topics);
    this.page$.next(0);
  }

  nextQuestion() {
    const nextPage = this.page$.value + 1;
    this.page$.next(nextPage);
  }

}
