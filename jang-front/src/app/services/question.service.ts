import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Injectable } from '@angular/core';
import { QuestionDTO } from '../shared/models/question.model';
import { Page } from '../shared/models/page.model';
import { catchError, Observable, throwError } from 'rxjs';
import { AttemptLight } from '../shared/models/attempt-light.model';

@Injectable({
  providedIn: 'root',
})
export class QuestionService {
  private readonly url = `${environment.apiUrl}/questions`;

  constructor(private http: HttpClient) {}

  // - RECUPERER LES QUESTIONS ALEATOIRE OU NON -
  getFilteredQuestions(
    niveaux?: number[],
    topics?: string[],
    page = 0,
    size = 10,
    random = true,
    sort = 'id,asc'
  ): Observable<Page<QuestionDTO>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('random', random);

    if (!random) {
      params = params.set('sort', sort);
    } // tri si besoin

    if (niveaux && niveaux.length) {
      params = params.set('niveaux', niveaux.toString());
    }

    if (topics && topics.length) {
      topics.forEach((topic) => {
        params = params.append('topics', topic);
      });
    }

    return this.http
      .get<Page<QuestionDTO>>(`${this.url}/filter`, { params })
      .pipe(
        catchError((err) => {
          console.error('get filtered questions failed : ' + err);
          return throwError(
            () => new Error('Impossible de charger les questions')
          );
        })
      );
  }

  // - SI CONNECTE : RECUPERE DES QUESTIONS ADAPTES A L'HISTORIQUE UTILISATEUR -
  getAdaptiveQuestions(niveaux?: number[], topics?: string[], size = 10) {
    const params = new HttpParams()
      .set('size', size)
      .set('niveaux', niveaux?.toString() ?? '')
      .set('topics', topics?.join(',') ?? '');

    return this.http.get<Page<QuestionDTO>>(
  `${this.url}/adaptive`,
  { params, withCredentials: true }
);
  }

  // — CRÉER UNE QUESTION —
  createQuestion(dto: QuestionDTO): Observable<QuestionDTO> {
    return this.http.post<QuestionDTO>(this.url, dto).pipe(
      catchError((err) => {
        console.error('createQuestion a échoué :', err);
        return throwError(() => new Error('Impossible de créer la question'));
      })
    );
  }

  // — METTRE À JOUR UNE QUESTION —
  updateQuestion(dto: QuestionDTO): Observable<QuestionDTO> {
    if (!dto.id) {
      return throwError(
        () => new Error('L’ID de la question est requis pour la mise à jour')
      );
    }
    return this.http.put<QuestionDTO>(`${this.url}/${dto.id}`, dto).pipe(
      catchError((err) => {
        console.error('updateQuestion a échoué :', err);
        return throwError(
          () => new Error('Impossible de mettre à jour la question')
        );
      })
    );
  }

  // — SUPPRIMER UNE QUESTION —
  deleteQuestion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`).pipe(
      catchError((err) => {
        console.error('deleteQuestion a échoué :', err);
        return throwError(
          () => new Error('Impossible de supprimer la question')
        );
      })
    );
  }

  // - ENVOYER LA SERIE DE RESULTATS AU BACK -
  sendSeriesResult(attempts: AttemptLight[]) {
    return this.http.post<void>(`${environment.apiUrl}/series-results`, attempts, { withCredentials: true }   );
  }
}
