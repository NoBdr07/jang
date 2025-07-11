import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-progress',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './progress.component.html',
  styleUrl: './progress.component.scss',
})
export class ProgressComponent {
  @Input() current = 0;
  @Input() total = 0;
  @Input() stats = { ok: 0, bof: 0, ko: 0 };

  isMobile = false;

  constructor(bp: BreakpointObserver){
  bp.observe([Breakpoints.Handset]).subscribe(res => this.isMobile = res.matches);
}
}
