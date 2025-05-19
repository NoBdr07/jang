import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const cloned = req.clone({
      withCredentials: true
      // si tu utilises CSRF via cookie XSRF-TOKEN :
      // , headers: req.headers.set('X-XSRF-TOKEN', this.cookieService.get('XSRF-TOKEN') || '')
    });
    return next.handle(cloned);
  }
}