import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { BehaviorSubject, Observable, tap } from "rxjs";
import { MeResponse } from "../shared/payload/me-response.interface";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { RegisterRequest } from "../shared/payload/register-request.interface";
import { LoginRequest } from "../shared/payload/login-request.interface";
import { AuthResponse } from "../shared/payload/auth-response.interface";


@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly url = `${environment.apiUrl}/auth`;

  private currentUserSubject = new BehaviorSubject<MeResponse|null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  register(req: RegisterRequest): Observable<any> {
    return this.http.post(`${this.url}/register`, req);
  }

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.url}/login`,
      { req },
      { withCredentials: true }
    ).pipe(
      tap(() => {
        // Une fois le login OK, on récupère /me
        this.me().subscribe();
        this.router.navigate(['/dashboard']);
      })
    );
  }

  me(): Observable<MeResponse> {
    return this.http.get<MeResponse>(`${this.url}/me`, { withCredentials: true })
      .pipe(tap(user => this.currentUserSubject.next(user)));
  }

  logout(): void {
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  get role(): string|null {
    return this.currentUserSubject.value?.role ?? null;
  }

}