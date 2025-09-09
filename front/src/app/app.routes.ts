import { Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { TipoEventoComponent } from './component/tipo-evento/tipo-evento.component';
import { EventoListComponent } from './component/evento-list/evento-list.component';



export const routes: Routes = [
  { path: 'inicio', component: HomeComponent },
  { path: 'tipo-eventos', component: TipoEventoComponent },
  { path: 'eventos', component: EventoListComponent },
  { path: '', redirectTo: '/inicio', pathMatch: 'full' },
  { path: '**', redirectTo: '/inicio' }
];
