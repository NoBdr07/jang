import { CommonModule } from '@angular/common';
import { Component, inject, ViewChild } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { QuestionService } from '../../services/question.service';
import { QuestionDTO } from '../../shared/models/question.model';
import { MatIconModule } from '@angular/material/icon';
import { BehaviorSubject, catchError, Observable, throwError } from 'rxjs';
import { TopicService } from '../../services/topic.service';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';

@Component({
  selector: 'app-gestion-questions',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
  ],
  templateUrl: './gestion-questions.component.html',
  styleUrl: './gestion-questions.component.scss',
})
export class GestionQuestionsComponent {
  questions: QuestionDTO[] = [];
  levels: number[] = [1, 2, 3];
  selectedTopic: string | null = null;
  selectedLevel: number | null = null;
  displayedColumns: string[] = ['id', 'title', 'level', 'topicName', 'actions'];

  // pagination
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  totalElements = 0;
  pageSize = 20;
  pageIndex = 0;

  showForm = false;
  editingQuestion: QuestionDTO | null = null;
  questionForm!: FormGroup;

  readonly topics$ = inject(TopicService).getTopics();

  constructor(
    private fb: FormBuilder,
    private questionService: QuestionService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadQuestions();
  }

  private initForm(): void {
    this.questionForm = this.fb.group({
      id: [null], // conservé pour l’update (sera ignoré en création)
      title: ['', Validators.required],
      answer: ['', Validators.required],
      level: [1, [Validators.required, Validators.min(1), Validators.max(3)]],
      topicName: ['', Validators.required],
    });
  }

  loadQuestions(): void {
    console.log("selectedLevel = " + this.selectedLevel);
    const niveaux = this.selectedLevel ? [this.selectedLevel] : [];
    console.log("niveaux = " + niveaux);
    const topics = this.selectedTopic ? [this.selectedTopic] : [];

    this.questionService
      .getFilteredQuestions(
        niveaux,
        topics,
        this.pageIndex,
        this.pageSize,
        false
      )
      .pipe(
        catchError((err) => {
          console.error('Erreur au chargement des questions', err);
          return throwError(
            () => new Error('Impossible de charger les questions')
          );
        })
      )
      .subscribe((page) => {
        this.questions = page.content;
        this.totalElements = page.totalElements;
      });
  }


  onAddNew(): void {
    this.editingQuestion = null;
    this.questionForm.reset({
      id: null,
      title: '',
      answer: '',
      level: 1,
      topicName: '',
    });
    this.showForm = true;
  }

  onEdit(q: QuestionDTO): void {
    this.editingQuestion = q;
    this.questionForm.patchValue({
      id: q.id,
      title: q.title,
      answer: q.answer,
      level: q.level,
      topicName: q.topicName,
    });
    this.showForm = true;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  onCancel(): void {
    this.showForm = false;
    this.questionForm.reset();
  }

  onPageChange(e: PageEvent) {
    this.pageIndex = e.pageIndex;
    this.pageSize = e.pageSize;
    this.loadQuestions();
  }

  onSubmit(): void {
    if (this.questionForm.invalid) return;

    const formValue = this.questionForm.value as QuestionDTO;

    if (this.editingQuestion?.id) {
      // Mise à jour
      formValue.id = this.editingQuestion.id;
      this.questionService.updateQuestion(formValue).subscribe({
        next: () => {
          this.loadQuestions();
          this.showForm = false;
        },
        error: (err) => console.error('Erreur lors de la mise à jour', err),
      });
    } else {
      // Création
      this.questionService.createQuestion(formValue).subscribe({
        next: (created) => {
          this.loadQuestions();
          this.showForm = false;
        },
        error: (err) => console.error('Erreur lors de la création', err),
      });
    }
  }

  onDelete(id: number): void {
    const confirmed = window.confirm(
      'Voulez-vous vraiment supprimer cette question ?'
    );
    if (!confirmed) return;

    this.questionService.deleteQuestion(id).subscribe({
      next: () => this.loadQuestions(),
      error: (err) => console.error('Erreur lors de la suppression', err),
    });
  }
}
