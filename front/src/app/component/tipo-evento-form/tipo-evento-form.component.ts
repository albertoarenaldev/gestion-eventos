import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TipoEvento } from '../../interface/tipo-evento';
import { TipoEventoService } from '../../services/tipo-evento.service';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-tipo-evento-form',
  standalone: true,
  imports: [CommonModule, FormsModule,NgbAlertModule],
  templateUrl: './tipo-evento-form.component.html',
  styleUrl: './tipo-evento-form.component.css'
})
export class TipoEventoFormComponent {
  nuevoTipoEvento: TipoEvento = {
    nombre: '',
    duracionTipica: 0,
    duracionMinima: 0,
    duracionMaxima: 0,
    aforoHabitual: 0
    
  };
   errorMessage: string | null = null;
    constructor(
    private tipoEventoService: TipoEventoService,
    private router: Router
  ) {}

  guardar(): void {
      this.errorMessage = null;
  this.tipoEventoService.createTipoEvento(this.nuevoTipoEvento).subscribe(
    () => {
      this.router.navigate(['/tipo-eventos']);
    },
    (err: HttpErrorResponse) => {
      // error en caso de duplicado en el backend para un tipo de evento
      if (err.status === 500 && err.error?.message?.includes('Ya existe un tipo de evento')) {
        this.errorMessage = err.error.message; // muestro mensaje exacto del backend
      } else {
        // otro error diferente
        this.errorMessage = 'Ocurrió un error inesperado al crear el tipo de evento.';
      }
      console.error('Error al crear el tipo de evento:', err);
    }
  );
}

cancelar(): void {
  this.router.navigate(['/tipo-eventos']);
}
}