import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: 'USER' | 'CUSTOMER' | 'RESTAURANT_OWNER' | 'ADMIN';
  phone?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'kmato_jwt';
  private userKey = 'kmato_user';
  private currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/auth/login`, credentials).pipe(
      tap(response => {
        if (response.token) {
          this.saveToken(response.token);
          if (response.user) {
            this.saveUser(response.user);
          }
        }
      })
    );
  }

  register(dto: any): Observable<any> { 
    return this.http.post(`${environment.apiUrl}/auth/register`, dto); 
  }

  /**
   * Fetch current authenticated user from backend (requires token in headers)
   * Saves user into local storage on success.
   */
  fetchCurrentUser(): Observable<User> {
    return this.http.get<User>(`${environment.apiUrl}/auth/me`).pipe(
      tap(user => {
        if (user) this.saveUser(user);
      })
    );
  }

  saveToken(token: string) { 
    localStorage.setItem(this.tokenKey, token); 
  }

  saveUser(user: User) {
    localStorage.setItem(this.userKey, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  getToken(): string | null { 
    return localStorage.getItem(this.tokenKey); 
  }

  getUser(): User | null {
    return this.currentUserSubject.value;
  }

  getUserFromStorage(): User | null {
    const userStr = localStorage.getItem(this.userKey);
    return userStr ? JSON.parse(userStr) : null;
  }

  isLoggedIn(): boolean { 
    return !!this.getToken(); 
  }

  hasRole(role: string): boolean {
    const user = this.getUser();
    if (!user || !user.role) return false;
    try {
      return user.role.toString().toUpperCase() === role.toString().toUpperCase();
    } catch {
      return false;
    }
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isRestaurantOwner(): boolean {
    // Accept common variants returned by backends: 'RESTAURANT_OWNER' or 'OWNER'
    return this.hasRole('RESTAURANT_OWNER') || this.hasRole('OWNER');
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.currentUserSubject.next(null);
  }
}
