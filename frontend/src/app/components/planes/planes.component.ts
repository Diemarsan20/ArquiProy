import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SpsService } from '../../services/sps.service';
import { AuthService } from '../../services/auth.service';
import { Plan } from '../../models/models';

@Component({
  selector: 'app-planes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h1 class="page-title">Catálogo de Planes de Salud</h1>

      <div class="card" style="display:flex; gap:1rem; align-items:center; margin-bottom:1.5rem;">
        <input type="text" [(ngModel)]="busqueda" placeholder="Buscar plan por nombre..."
          style="flex:1; padding:10px 14px; border:1.5px solid #d1d5db; border-radius:8px; font-size:0.95rem;"
          (keyup.enter)="buscar()" />
        <button class="btn btn-outline btn-sm" (click)="buscar()">Buscar</button>
        <button class="btn btn-outline btn-sm" (click)="cargarTodos()">Ver todos</button>
      </div>

      <div class="spinner" *ngIf="loading">Cargando planes...</div>

      <div class="card-grid" *ngIf="!loading">
        <div class="plan-card" *ngFor="let plan of planes"
             [class.selected]="seleccionados.has(plan.id)"
             (click)="togglePlan(plan)">
          <div class="check" *ngIf="seleccionados.has(plan.id)">&#10003;</div>
          <div class="plan-name">{{ plan.nombre }}</div>
          <div class="plan-price">$ {{ plan.precio | number:'1.0-0' }}</div>
          <p style="font-size:0.85rem; color:#6b7280; margin-bottom:12px;">{{ plan.descripcion }}</p>
          <ul class="plan-services">
            <li *ngFor="let s of plan.servicios">{{ s.nombre }} &mdash; $ {{ s.precio | number:'1.0-0' }}</li>
          </ul>
        </div>
      </div>

      <div class="empty" *ngIf="!loading && planes.length === 0">
        <div class="empty-icon">&#128203;</div>
        No se encontraron planes.
      </div>

      <div class="card" style="position:sticky; bottom:1rem; display:flex; justify-content:space-between; align-items:center;" *ngIf="seleccionados.size > 0">
        <div>
          <strong>{{ seleccionados.size }} plan(es) seleccionado(s)</strong>
          <span style="margin-left:1rem; font-size:1.1rem; color:#1a56db; font-weight:700;">
            Total: $ {{ totalSeleccionado() | number:'1.0-0' }}
          </span>
        </div>
        <div style="display:flex; gap:0.75rem;">
          <button class="btn btn-outline btn-sm" (click)="seleccionados.clear()">Limpiar</button>
          <button class="btn btn-success" (click)="comprar()" [disabled]="comprando">
            {{ comprando ? 'Procesando...' : 'Comprar seleccionados' }}
          </button>
        </div>
      </div>

      <div class="alert alert-success" *ngIf="mensajeExito" style="margin-top:1rem;">
        &#10003; {{ mensajeExito }}<br>
        <button class="btn btn-outline btn-sm" style="margin-top:8px;" (click)="irACompras()">Ver mis compras</button>
      </div>
      <div class="alert alert-error" *ngIf="mensajeError" style="margin-top:1rem;">{{ mensajeError }}</div>
    </div>
  `
})
export class PlanesComponent implements OnInit {
  planes:        Plan[]     = [];
  seleccionados: Set<number> = new Set();
  busqueda    = '';
  loading     = true;
  comprando   = false;
  mensajeExito = '';
  mensajeError = '';

  constructor(
    private spsService:  SpsService,
    private authService: AuthService,
    private router:      Router
  ) {}

  ngOnInit() { this.cargarTodos(); }

  cargarTodos() {
    this.loading  = true;
    this.busqueda = '';
    this.spsService.getPlanes().subscribe({
      next:  p  => { this.planes = p; this.loading = false; },
      error: () => this.loading = false
    });
  }

  buscar() {
    if (!this.busqueda.trim()) { this.cargarTodos(); return; }
    this.loading = true;
    this.spsService.buscarPlanes(this.busqueda).subscribe({
      next:  p  => { this.planes = p; this.loading = false; },
      error: () => this.loading = false
    });
  }

  togglePlan(plan: Plan) {
    this.mensajeExito = '';
    this.mensajeError = '';
    this.seleccionados.has(plan.id)
      ? this.seleccionados.delete(plan.id)
      : this.seleccionados.add(plan.id);
  }

  totalSeleccionado(): number {
    return this.planes
      .filter(p => this.seleccionados.has(p.id))
      .reduce((sum, p) => sum + p.precio, 0);
  }

  comprar() {
    const cedula = this.authService.getCedulaSps();
    if (!cedula) { this.router.navigate(['/login']); return; }
    this.comprando   = true;
    this.mensajeError = '';
    this.spsService.crearCompra(cedula, Array.from(this.seleccionados)).subscribe({
      next: res => {
        this.comprando    = false;
        this.seleccionados.clear();
        this.mensajeExito = 'Compra ' + res.codigoCompra + ' registrada. Recibirás un correo cuando sea aprobada por la SNS.';
      },
      error: () => {
        this.comprando   = false;
        this.mensajeError = 'Error al crear la compra. Intenta nuevamente.';
      }
    });
  }

  irACompras() { this.router.navigate(['/compras']); }
}
