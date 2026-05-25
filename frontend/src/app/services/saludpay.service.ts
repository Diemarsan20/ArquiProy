import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompraPendiente } from '../models/models';

@Injectable({ providedIn: 'root' })
export class SaludPayService {
  private base = '/spay/api';

  constructor(private http: HttpClient) {}

  getComprasPendientes(cedula: string): Observable<CompraPendiente[]> {
    return this.http.get<CompraPendiente[]>(`${this.base}/compras/pendientes/${cedula}`);
  }

  pagar(cedulaCliente: string, numeroCompra: string, valorPagado: number): Observable<any> {
    return this.http.post<any>(`${this.base}/compras/pagar`, {
      cedulaCliente, numeroCompra, valorPagado
    });
  }
}
