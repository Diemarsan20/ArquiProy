import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  template: `
    <nav class="navbar" *ngIf="authService.isLoggedInSps() || authService.isLoggedInSpay()">
      <span class="navbar-brand">🏥 SPS</span>
      <div class="navbar-links">
        <a (click)="nav('/planes')"  *ngIf="authService.isLoggedInSps()">Planes</a>
        <a (click)="nav('/compras')" *ngIf="authService.isLoggedInSps()">Mis Compras</a>
        <a (click)="nav('/saludpay')">SaludPay</a>
      </div>
      <div style="display:flex; align-items:center; gap:1rem;">
        <span class="navbar-user" *ngIf="authService.isLoggedInSps()">
          {{ authService.getNombreSps() }}
        </span>
        <button class="btn-logout" (click)="logout()">Salir</button>
      </div>
    </nav>
    <router-outlet />
  `
})
export class AppComponent {
  constructor(public authService: AuthService, private router: Router) {}

  nav(path: string) { this.router.navigate([path]); }

  logout() {
    this.authService.logoutSps();
    this.authService.logoutSpay();
    this.router.navigate(['/login']);
  }
}
