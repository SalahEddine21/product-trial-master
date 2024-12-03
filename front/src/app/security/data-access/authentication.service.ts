import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, of, tap, throwError } from 'rxjs';
import { AuthenticationRequest } from './authenticationRequest';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private readonly http = inject(HttpClient);
  private readonly path = "/api/auth/token";

  public login(authenticationRequest: AuthenticationRequest): Observable<string> {
    return this.http.post(this.path, authenticationRequest, { responseType: 'text' }).pipe(
      tap((token) => {
        return of(token);
      }),
      catchError((error) => {
        console.error('Login error:', error);
        return throwError(() => new Error('Login failed'));
      })
    );
  }  

}
