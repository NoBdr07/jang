import { Component, Output, EventEmitter, inject } from '@angular/core';
import { TopicService } from '../../../services/topic.service';
import { CommonModule } from '@angular/common';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatChipsModule } from '@angular/material/chips';


@Component({
  selector: 'app-filter',
  standalone: true,
  imports: [CommonModule, MatButtonToggleModule, MatChipsModule],
  templateUrl: './filter.component.html',
  styleUrl: './filter.component.scss',
})
export class FilterComponent {
  @Output() selectionChange = new EventEmitter<{
    niveau: number[];
    topics: string[];
  }>();

  niveaux = [1, 2, 3];
  readonly topics$ = inject(TopicService).getTopics();

  selectedNiveaux: number[] = [];
  selectedTopics: string[] = [];

  toggleNiveau(n: number) {
    const i = this.selectedNiveaux.indexOf(n);
    if (i >= 0) this.selectedNiveaux.splice(i, 1);
    else this.selectedNiveaux.push(n);
    this.emitSelection();
  }

  toggleTopic(name: string) {
    const i = this.selectedTopics.indexOf(name);
    if (i >= 0) this.selectedTopics.splice(i, 1);
    else this.selectedTopics.push(name);
    this.emitSelection();
  }

  private emitSelection() {
    this.selectionChange.emit({
      niveau: [...this.selectedNiveaux],
      topics: [...this.selectedTopics],
    });
  }
}
