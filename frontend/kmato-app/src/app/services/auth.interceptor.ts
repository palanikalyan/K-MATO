import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.auth.getToken();
    console.log('AuthInterceptor - Token:', token ? 'Present' : 'Missing');
    console.log('AuthInterceptor - Request URL:', req.url);
    
    if (token) {
      const cloned = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
      console.log('AuthInterceptor - Added Authorization header');
      return next.handle(cloned);
    }
    console.log('AuthInterceptor - No token, request continues without auth');
    return next.handle(req);
  }
}

