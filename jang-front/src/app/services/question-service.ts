import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Injectable } from '@angular/core';
import { QuestionDTO } from '../models/question.model';
import { Page } from '../models/page.model';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class QuestionService {
  private readonly url = `${environment.apiUrl}/questions/filter`;

  constructor(private http: HttpClient) {}

  getFilteredQuestions(
    niveau?: number[],
    topics?: string[],
    page = 0,
    size = 20
  ): Observable<Page<QuestionDTO>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (niveau && niveau.length) {
      params = params.set('niveau', niveau.toString());
    }

    if (topics && topics.length) {
      topics.forEach((topic) => {
        params = params.append('topics', topic);
      });
    }

    return this.http.get<Page<QuestionDTO>>(this.url, { params })
    .pipe(
        catchError(err => {
            console.error("get filtered questions failed : " + err);
            return throwError(() => new Error('Impossible de charger les questions'));
        })
    );
  }
}
