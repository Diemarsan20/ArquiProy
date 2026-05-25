import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SpsService } from '../../services/sps.service';
import { AuthService } from '../../services/auth.service';
import { Compra } from '../../models/models';

@Component({
  selector: 'app-compras',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container">
      <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
        <h1 class="page-title" style="margin-bottom:0;">Mis Compras</h1>
        <div style="display:flex; gap:0.75rem; align-items:center;">
          <span style="font-size:0.85rem; color:#6b7280;" *ngIf="autoRefresh">Actualizando cada 10s</span>
          <button class="btn btn-outline btn-sm" (click)="cargar()">Actualizar</button>
          <button class="btn btn-outline btn-sm" (click)="irAPlanes()">+ Nueva compra</button>
        </div>
      </div>

      <div class="spinner" *ngIf="loading">Cargando compras...</div>

      <div class="empty" *ngIf="!loading && compras.length === 0">
        <div class="empty-icon">&#128722;</div>
        No tienes compras aún.<br>
        <button class="btn btn-primary btn-sm" style="margin-top:1rem;" (click)="irAPlanes()">
          Explorar planes
        </button>
      </div>

      <div class="card" *ngFor="let c of compras">
        <div class="estado-row">
          <span class="compra-codigo">{{ c.codigo }}</span>
          <span [class]="badgeClass(c.estadoCompra)">{{ estadoLabel(c.estadoCompra) }}</span>
          <span style="margin-left:auto; font-size:0.85rem; color:#6b7280;">
            {{ c.fechaCreacion | date:'dd/MM/yyyy HH:mm' }}
          </span>
        </div>

        <div class="compra-valor" style="margin-bottom:1rem;">
          Total: <strong>$ {{ c.valorTotal | number:'1.0-0' }}</strong>
        </div>

        <div style="display:flex; flex-wrap:wrap; gap:0.5rem; margin-bottom:0.75rem;">
          <span *ngFor="let item of c.items" style="background:#f1f5f9; padding:4px 10px; border-radius:6px; font-size:0.82rem;">
            {{ item.plan.nombre }}
            <span style="font-size:0.75rem; margin-left:4px;"
                  [style.color]="snsColor(item.estadoValidacionSns)">
              ({{ item.estadoValidacionSns }})
            </span>
          </span>
        </div>

        <div class="alert alert-info" *ngIf="c.estadoCompra === 'PENDIENTE_VALIDACION'" style="font-size:0.85rem; padding:8px 12px;">
          Validando con SNS... puede tomar hasta 30 segundos.
        </div>
        <div class="alert alert-info" *ngIf="c.estadoCompra === 'PENDIENTE_PAGO'" style="font-size:0.85rem; padding:8px 12px;">
          Aprobada. Dirgete a <strong>SaludPay</strong> para completar el pago.
          <button class="btn btn-success btn-sm" style="margin-left:8px;" (click)="irASaludPay()">Ir a pagar</button>
        </div>
        <div *ngIf="c.estadoCompra === 'PAGADO'" style="font-size:0.85rem; padding:8px 12px; background:#fef3c7; color:#92400e; border:1px solid #d97706; border-radius:6px; margin-top:8px;">
          Pago recibido. Revisa tu correo y confirma que leiste el aviso para completar la compra.
          <button class="btn btn-sm" style="margin-left:8px; background:#d97706; color:white; padding:4px 10px;"
                  (click)="confirmar(c.codigo)" [disabled]="confirmando === c.codigo">
            {{ confirmando === c.codigo ? 'Confirmando...' : 'Confirmar recepcion' }}
          </button>
        </div>
        <div class="alert alert-success" *ngIf="c.estadoCompra === 'TERMINADA'" style="font-size:0.85rem; padding:8px 12px;">
          Compra completada. Tus planes estan activos.
        </div>
        <div class="alert alert-error" *ngIf="c.estadoCompra === 'RECHAZADO'" style="font-size:0.85rem; padding:8px 12px;">
          Rechazada por SNS. Uno o mas planes no estan autorizados.
        </div>
      </div>
    </div>
  `
})
export class ComprasComponent implements OnInit, OnDestroy {
  compras:     Compra[] = [];
  loading      = true;
  autoRefresh  = false;
  confirmando  = '';
  private timer: any;

  constructor(
    private spsService:  SpsService,
    private authService: AuthService,
    private router:      Router
  ) {}

  ngOnInit() {
    this.cargar();
    this.autoRefresh = true;
    this.timer = setInterval(() => this.cargar(), 10000);
  }

  ngOnDestroy() { clearInterval(this.timer); }

  cargar() {
    const cedula = this.authService.getCedulaSps();
    if (!cedula) { this.router.navigate(['/login']); return; }
    this.spsService.getComprasPorCliente(cedula).subscribe({
      next:  c  => { this.compras = c.sort((a, b) => b.id - a.id); this.loading = false; },
      error: () => this.loading = false
    });
  }

  badgeClass(estado: string): string {
    const map: Record<string, string> = {
      'PENDIENTE_VALIDACION': 'badge badge-warning',
      'PENDIENTE_PAGO':       'badge badge-info',
      'PAGADO':               'badge badge-info',
      'TERMINADA':            'badge badge-success',
      'RECHAZADO':            'badge badge-danger'
    };
    return map[estado] ?? 'badge badge-secondary';
  }

  estadoLabel(estado: string): string {
    const map: Record<string, string> = {
      'PENDIENTE_VALIDACION': 'Validando SNS',
      'PENDIENTE_PAGO':       'Pendiente de pago',
      'PAGADO':               'Pago recibido',
      'TERMINADA':            'Completada',
      'RECHAZADO':            'Rechazada'
    };
    return map[estado] ?? estado;
  }

  snsColor(estado: string): string {
    const map: Record<string, string> = {
      'APROBADO':  '#059669',
      'RECHAZADO': '#dc2626',
      'ENPROCESO': '#d97706',
      'PENDIENTE': '#6b7280'
    };
    return map[estado] ?? '#6b7280';
  }

  confirmar(codigo: string) {
    this.confirmando = codigo;
    this.spsService.confirmarRecepcion(codigo).subscribe({
      next:  () => { this.confirmando = ''; this.cargar(); },
      error: () => { this.confirmando = ''; alert('Error al confirmar. Intenta de nuevo.'); }
    });
  }

  irAPlanes()   { this.router.navigate(['/planes']);   }
  irASaludPay() { this.router.navigate(['/saludpay']); }
}
