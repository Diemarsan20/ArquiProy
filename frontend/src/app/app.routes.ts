import { Routes } from '@angular/router';
import { LoginComponent }     from './components/login/login.component';
import { PlanesComponent }    from './components/planes/planes.component';
import { ComprasComponent }   from './components/compras/compras.component';
import { SaludPayComponent }  from './components/saludpay/saludpay.component';

export const routes: Routes = [
  { path: '',          redirectTo: 'login', pathMatch: 'full' },
  { path: 'login',     component: LoginComponent },
  { path: 'planes',    component: PlanesComponent },
  { path: 'compras',   component: ComprasComponent },
  { path: 'saludpay',  component: SaludPayComponent },
  { path: '**',        redirectTo: 'login' }
];
