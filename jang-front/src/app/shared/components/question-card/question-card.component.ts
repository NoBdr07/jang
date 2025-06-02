import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { QuestionDTO } from '../../models/question.model';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-question-card',
  standalone: true,
  imports: [MatCardModule, CommonModule, MatButtonModule],
  templateUrl: './question-card.component.html',
  styleUrl: './question-card.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class QuestionCardComponent {
  @Input() question?: QuestionDTO;
  @Input() questionIndex?: number;
  @Input() total = 0;
  @Output() nextQuestion = new EventEmitter<void>();
  @Output() nextSet = new EventEmitter<void>();
  @Output() pushEvaluation = new EventEmitter<number>();

  showAnswer = false;
  isEvaluated = false;
  isLastQuestion = false;

  toggleAnswer() {
    this.showAnswer = !this.showAnswer;
  }

  onNextQuestion() {
    this.nextQuestion.emit();
    this.showAnswer = false;
    this.isEvaluated = false;
  }

  onNextSet() {
    this.nextSet.emit();
    this.showAnswer = false;
    this.isEvaluated = false;
  }

  evaluate(evaluation: number) {
    switch (evaluation) {
      case 2 : 
        this.pushEvaluation.emit(2);
        this.isEvaluated = true;
        break;
      case 1 :
        this.pushEvaluation.emit(1);
        this.isEvaluated = true;
        break;
      case 0:
        this.pushEvaluation.emit(0);
        this.isEvaluated = true;
        break;
    }
  }


}
