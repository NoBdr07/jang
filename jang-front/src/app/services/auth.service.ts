import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
  BehaviorSubject,
  Observable,
  of,
  shareReplay,
  tap,
  finalize,
  map,
} from 'rxjs';
import { Router } from '@angular/router';

import { environment } from '../environments/environment';
import { MeResponse } from '../shared/payload/me-response.interface';
import { RegisterRequest } from '../shared/payload/register-request.interface';
import { LoginRequest } from '../shared/payload/login-request.interface';
import { AuthResponse } from '../shared/payload/auth-response.interface';

@Injectable({ providedIn: 'root' })
export class AuthService {
  /* ───────── configuration ───────── */
  private readonly url = `${environment.apiUrl}/auth`;

  /* ───────── état utilisateur ───────── */
  private currentUserSubject = new BehaviorSubject<MeResponse | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  /* ───────── mémo de la requête /me ───────── */
  /** contiendra la seule requête HTTP /me en cours ou déjà terminée */
  private meRequest$?: Observable<MeResponse>;

  constructor(private http: HttpClient, private router: Router) {}

  /* ───────── inscription ───────── */
  register(req: RegisterRequest): Observable<void> {
    return this.http
      .post<void>(`${this.url}/register`, req)
      .pipe(tap(() => this.router.navigate(['/login'])));
  }

  /* ───────── connexion ───────── */
  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.url}/login`, req, { withCredentials: true })
      .pipe(
        tap(() => this.me().subscribe()), // déclenche la mise en cache
        tap(() => this.router.navigate(['/dashboard']))
      );
  }

  /* ───────── profil courant (/me) ───────── */
  me(): Observable<MeResponse> {
    /* ① si l’utilisateur est déjà en mémoire → retour immédiat */
    const cached = this.currentUserSubject.value;
    if (cached) {
      return of(cached);
    }

    /* ② si une requête est déjà lancée, on la renvoie */
    if (this.meRequest$) {
      return this.meRequest$;
    }

    /* ③ sinon on la crée, on la partage, on la mémorise */
    this.meRequest$ = this.http
      .get<MeResponse>(`${this.url}/me`, { withCredentials: true })
      .pipe(
        tap((user) => this.currentUserSubject.next(user)),
        shareReplay(1), 
        finalize(() => {
          this.meRequest$ = undefined;
        })
      );

    return this.meRequest$;
  }

  /* ───────── déconnexion ───────── */
  logout(): void {
    this.currentUserSubject.next(null);
    this.meRequest$ = undefined; // vide le cache → prochaine nav = nouveau /me
    this.router.navigate(['/login']);
  }

  /* ───────── aide lecture rôle ───────── */
  role$ = this.currentUser$.pipe(map((user) => user?.role ?? null));
}
