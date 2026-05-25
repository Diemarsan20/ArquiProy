import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Plan, Compra } from '../models/models';

@Injectable({ providedIn: 'root' })
export class SpsService {
  private base = '/sps/api';

  constructor(private http: HttpClient) {}

  getPlanes(): Observable<Plan[]> {
    return this.http.get<Plan[]>(`${this.base}/planes`);
  }

  buscarPlanes(nombre: string): Observable<Plan[]> {
    return this.http.get<Plan[]>(`${this.base}/planes/buscar?nombre=${nombre}`);
  }

  crearCompra(cedulaCliente: string, planIds: number[]): Observable<any> {
    return this.http.post<any>(`${this.base}/compras`, { cedulaCliente, planIds });
  }

  getComprasPorCliente(cedula: string): Observable<Compra[]> {
    return this.http.get<Compra[]>(`${this.base}/compras/cliente/${cedula}`);
  }

  getCompraPorCodigo(codigo: string): Observable<Compra> {
    return this.http.get<Compra>(`${this.base}/compras/codigo/${codigo}`);
  }

  confirmarRecepcion(codigo: string): Observable<any> {
    return this.http.post<any>(`${this.base}/compras/${codigo}/confirmar`, {});
  }
}
