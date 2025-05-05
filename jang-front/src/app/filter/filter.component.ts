import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-filter',
  standalone: true,
  imports: [],
  templateUrl: './filter.component.html',
  styleUrl: './filter.component.scss'
})
export class FilterComponent {
  @Output() selectionChange = new EventEmitter<{ niveau: number[], topics: string[] }>();

  niveaux = [1, 2, 3];

}
