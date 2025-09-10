import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { TipoEvento } from '../../interface/tipo-evento';
import { TipoEventoService } from '../../services/tipo-evento.service';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-tipo-evento-form',
  standalone: true,
  imports: [CommonModule, FormsModule, NgbAlertModule],
  templateUrl: './tipo-evento-form.component.html',
  styleUrl: './tipo-evento-form.component.css'
})
export class TipoEventoFormComponent implements OnInit {
  nuevoTipoEvento: TipoEvento = {
    nombre: '',
    duracionTipica: 0,
    duracionMinima: 0,
    duracionMaxima: 0,
    aforoHabitual: 0,
    descripcion: ''
  };
  errorMessage: string | null = null;
  isEditMode: boolean = false;
  tipoEventoId: number | null = null;

  constructor(
    private tipoEventoService: TipoEventoService,
    private router: Router,
     private route: ActivatedRoute
  ) { }
 ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.tipoEventoId = +id; // Convert string to number
        this.tipoEventoService.getTipoEventoById(this.tipoEventoId).subscribe(tipo => {
          this.nuevoTipoEvento = tipo;
        });
      }
    });
  }
  guardar(): void {
    this.errorMessage = null;
    let operation: Observable<TipoEvento>;

    if (this.isEditMode) {
      operation = this.tipoEventoService.updateTipoEvento(this.nuevoTipoEvento);
    } else {
      operation = this.tipoEventoService.createTipoEvento(this.nuevoTipoEvento);
    }

    operation.subscribe({
      next: () => {
        this.router.navigate(['/tipo-eventos']);
      },
      error: (err: HttpErrorResponse) => {
        // Comprueba la firma específica del error de duplicado que devuelve el backend
        if (err.status === 500 && err.error?.message?.includes('Ya existe un tipo de evento')) {
          this.errorMessage = err.error.message; // Muestra el mensaje exacto del backend
        } else {
          // Para cualquier otro error
          this.errorMessage = 'Ocurrió un error inesperado al crear el tipo de evento.';
        }
        console.error('Error al crear el tipo de evento:', err);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/tipo-eventos']);
  }
}