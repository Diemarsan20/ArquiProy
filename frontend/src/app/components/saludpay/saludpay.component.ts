import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SaludPayService } from '../../services/saludpay.service';
import { CompraPendiente } from '../../models/models';

@Component({
  selector: 'app-saludpay',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h1 class="page-title">SaludPay</h1>

      <!-- Login SaludPay -->
      <div class="form-card" *ngIf="!authService.isLoggedInSpay()" style="margin:0 auto;">
        <div class="form-title">Iniciar sesion en SaludPay</div>
        <div class="form-subtitle">Ingresa tus credenciales para ver y pagar tus compras pendientes</div>
        <div class="alert alert-error" *ngIf="loginError">{{ loginError }}</div>
        <form (ngSubmit)="loginSpay()">
          <div class="form-group">
            <label>Cedula</label>
            <input type="text" [(ngModel)]="cedula" name="cedula" placeholder="Ej: 11111111" />
          </div>
          <div class="form-group">
            <label>Contrasena</label>
            <input type="password" [(ngModel)]="password" name="password" placeholder="..." />
          </div>
          <button type="submit" class="btn btn-primary" [disabled]="loginLoading">
            {{ loginLoading ? 'Ingresando...' : 'Ingresar a SaludPay' }}
          </button>
        </form>
      </div>

      <!-- Panel de pagos -->
      <div *ngIf="authService.isLoggedInSpay()">
        <div class="card" style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
          <div>
            <strong>Cedula:</strong> {{ authService.getCedulaSpay() }}
          </div>
          <div style="display:flex; gap:0.75rem;">
            <button class="btn btn-outline btn-sm" (click)="cargarPendientes()">Actualizar</button>
            <button class="btn btn-outline btn-sm" (click)="authService.logoutSpay(); pendientes=[]">Cerrar sesion</button>
          </div>
        </div>

        <div class="spinner" *ngIf="loading">Cargando compras pendientes...</div>

        <div class="empty" *ngIf="!loading && pendientes.length === 0">
          <div class="empty-icon">&#10003;</div>
          No tienes compras pendientes de pago.
        </div>

        <div class="card" *ngFor="let p of pendientes">
          <div style="display:flex; justify-content:space-between; align-items:flex-start; margin-bottom:1rem;">
            <div>
              <div style="font-size:1rem; font-weight:700; color:#1e3a5f;">{{ p.numeroCompra }}</div>
              <div style="font-size:1.3rem; font-weight:800; color:#1a56db; margin-top:4px;">
                $ {{ p.valorTotal | number:'1.0-0' }}
              </div>
            </div>
            <span class="badge badge-warning">Pendiente de pago</span>
          </div>

          <div class="alert alert-info" style="font-size:0.85rem; padding:8px 12px; margin-bottom:1rem;">
            Confirma el pago por el valor exacto: <strong>$ {{ p.valorTotal | number:'1.0-0' }}</strong>
          </div>

          <div style="display:flex; gap:0.75rem; align-items:center;">
            <input type="number" [ngModel]="valoresIngresados.get(p.numeroCompra)"
              (ngModelChange)="valoresIngresados.set(p.numeroCompra, $event)"
              [placeholder]="'Valor a pagar: ' + p.valorTotal"
              style="flex:1; padding:10px 14px; border:1.5px solid #d1d5db; border-radius:8px; font-size:0.95rem;" />
            <button class="btn btn-success" (click)="pagar(p)" [disabled]="pagando === p.numeroCompra">
              {{ pagando === p.numeroCompra ? 'Procesando...' : 'Pagar' }}
            </button>
          </div>

          <div class="alert alert-error" *ngIf="errorPago === p.numeroCompra" style="margin-top:0.75rem; font-size:0.85rem;">
            Ingresa el valor correcto para proceder al pago.
          </div>
        </div>

        <div class="alert alert-success" *ngIf="pagoExitoso" style="margin-top:1rem;">
          {{ pagoExitoso }}<br>
          <span style="font-size:0.85rem;">SPS ha sido notificado. Revisa tu correo con la confirmacion.</span>
        </div>
      </div>
    </div>
  `
})
export class SaludPayComponent implements OnInit {
  pendientes:       CompraPendiente[] = [];
  valoresIngresados = new Map<string, number>();
  loading      = false;
  cedula       = '';
  password     = '';
  loginError   = '';
  loginLoading = false;
  pagando      = '';
  pagoExitoso  = '';
  errorPago    = '';

  constructor(
    public authService:      AuthService,
    private saludPayService: SaludPayService,
    private router:          Router
  ) {}

  ngOnInit() {
    if (this.authService.isLoggedInSpay()) this.cargarPendientes();
  }

  loginSpay() {
    this.loginError   = '';
    this.loginLoading = true;
    this.authService.loginSaludPay(this.cedula, this.password).subscribe({
      next: () => { this.loginLoading = false; this.cargarPendientes(); },
      error: () => { this.loginLoading = false; this.loginError = 'Credenciales invalidas.'; }
    });
  }

  cargarPendientes() {
    const cedula = this.authService.getCedulaSpay();
    if (!cedula) return;
    this.loading = true;
    this.saludPayService.getComprasPendientes(cedula).subscribe({
      next:  p  => { this.pendientes = p; this.loading = false; },
      error: () => this.loading = false
    });
  }

  pagar(p: CompraPendiente) {
    const valor = this.valoresIngresados.get(p.numeroCompra);
    if (!valor || Number(valor) <= 0) { this.errorPago = p.numeroCompra; return; }
    this.errorPago   = '';
    this.pagoExitoso = '';
    this.pagando     = p.numeroCompra;

    this.saludPayService.pagar(p.cedulaCliente, p.numeroCompra, Number(valor)).subscribe({
      next: () => {
        this.pagando     = '';
        this.pagoExitoso = 'Pago de $ ' + Number(valor).toLocaleString() + ' procesado para ' + p.numeroCompra + '.';
        this.cargarPendientes();
      },
      error: () => { this.pagando = ''; this.errorPago = p.numeroCompra; }
    });
  }
}
