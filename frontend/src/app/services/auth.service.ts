import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private spsUrl    = '/sps/api/auth/login';
  private spayUrl   = '/spay/api/auth/login';

  constructor(private http: HttpClient) {}

  loginSps(cedula: string, password: string): Observable<any> {
    return this.http.post<any>(this.spsUrl, { cedula, password }).pipe(
      tap(res => {
        localStorage.setItem('sps_token',  res.token);
        localStorage.setItem('sps_cedula', res.cedula);
        localStorage.setItem('sps_nombre', res.nombre);
      })
    );
  }

  loginSaludPay(cedula: string, password: string): Observable<any> {
    return this.http.post<any>(this.spayUrl, { cedula, password }).pipe(
      tap(res => {
        localStorage.setItem('spay_token',  res.token);
        localStorage.setItem('spay_cedula', res.cedula);
      })
    );
  }

  getCedulaSps():    string | null { return localStorage.getItem('sps_cedula'); }
  getNombreSps():    string | null { return localStorage.getItem('sps_nombre'); }
  getCedulaSpay():   string | null { return localStorage.getItem('spay_cedula'); }
  isLoggedInSps():   boolean { return !!localStorage.getItem('sps_token'); }
  isLoggedInSpay():  boolean { return !!localStorage.getItem('spay_token'); }

  logoutSps() {
    localStorage.removeItem('sps_token');
    localStorage.removeItem('sps_cedula');
    localStorage.removeItem('sps_nombre');
  }

  logoutSpay() {
    localStorage.removeItem('spay_token');
    localStorage.removeItem('spay_cedula');
  }
}
