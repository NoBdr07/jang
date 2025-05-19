import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { QuestionDTO } from '../shared/models/question.model';
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
  @Output() nextQuestion = new EventEmitter<void>();

  showAnswer = false;

  toggleAnswer() {
    this.showAnswer = !this.showAnswer;
  }

  onNext() {
    this.nextQuestion.emit();
    this.showAnswer = false;
  }


}
