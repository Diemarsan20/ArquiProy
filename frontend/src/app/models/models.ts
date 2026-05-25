export interface Plan {
  id: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  precio: number;
  servicios: ServicioMedico[];
}

export interface ServicioMedico {
  id: number;
  nombre: string;
  tipo: string;
  precio: number;
}

export interface Cliente {
  id: number;
  nombre: string;
  correo: string;
  cedula: string;
}

export interface Compra {
  id: number;
  codigo: string;
  cliente: Cliente;
  estadoCompra: string;
  valorTotal: number;
  fechaCreacion: string;
  items: ItemCompra[];
}

export interface ItemCompra {
  id: number;
  plan: Plan;
  estadoValidacionSns: string;
  precio: number;
}

export interface CompraPendiente {
  id: number;
  cedulaCliente: string;
  numeroCompra: string;
  valorTotal: number;
  pagada: boolean;
}
