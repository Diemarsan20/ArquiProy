import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div style="min-height:100vh; display:flex; align-items:center; justify-content:center; background:linear-gradient(135deg,#1a56db 0%,#1e3a5f 100%);">
      <div class="form-card">
        <div style="text-align:center; margin-bottom:2rem;">
          <div style="font-size:2.5rem; margin-bottom:0.5rem;">🏥</div>
          <div class="form-title">SPS</div>
          <div class="form-subtitle">Sistema de Compra de Planes de Salud</div>
        </div>

        <div class="tabs">
          <button class="tab-btn" [class.active]="tab==='sps'"    (click)="tab='sps'">Portal SPS</button>
          <button class="tab-btn" [class.active]="tab==='spay'"   (click)="tab='spay'">SaludPay</button>
        </div>

        <div class="alert alert-error" *ngIf="error">{{ error }}</div>

        <form (ngSubmit)="login()">
          <div class="form-group">
            <label>Cédula</label>
            <input type="text" [(ngModel)]="cedula" name="cedula" placeholder="Ej: 11111111" required />
          </div>
          <div class="form-group">
            <label>Contraseña</label>
            <input type="password" [(ngModel)]="password" name="password" placeholder="••••••••" required />
          </div>
          <button type="submit" class="btn btn-primary" [disabled]="loading">
            {{ loading ? 'Ingresando...' : 'Ingresar' }}
          </button>
        </form>

        <div style="margin-top:1.5rem; padding:1rem; background:#f8fafc; border-radius:8px; font-size:0.82rem; color:#64748b;">
          <strong>Usuarios de prueba:</strong><br>
          11111111 / 22222222 / 33333333 — contraseña: <code>pass123</code>
        </div>
      </div>
    </div>
  `
})
export class LoginComponent {
  tab      = 'sps';
  cedula   = '';
  password = '';
  error    = '';
  loading  = false;

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.error   = '';
    this.loading = true;
    const call$ = this.tab === 'sps'
      ? this.authService.loginSps(this.cedula, this.password)
      : this.authService.loginSaludPay(this.cedula, this.password);

    call$.subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate([this.tab === 'sps' ? '/planes' : '/saludpay']);
      },
      error: () => {
        this.loading = false;
        this.error   = 'Cédula o contraseña incorrectos.';
      }
    });
  }
}
