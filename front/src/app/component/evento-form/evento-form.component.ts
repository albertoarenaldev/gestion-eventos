import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventoService } from '../../services/evento.service';
import { Evento } from '../../interface/evento';
import { TipoEventoService } from '../../services/tipo-evento.service';
import { TipoEvento } from '../../interface/tipo-evento';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-evento-form',
  standalone: true,
  imports: [FormsModule, CommonModule, NgbAlertModule],
  templateUrl: './evento-form.component.html',
  styleUrls: ['./evento-form.component.css']
})
export class EventoFormComponent implements OnInit {

  nuevoEvento: Evento = {
    nombre: '',
    fechaHora: '',
    duracionEspecifica: 0,
    aforoEspecifico: 0,
    descripcion: '',
    lugar: '',
    tipoEvento: { // Inicializar tipoEvento con un objeto vacío o valores por defecto
      nombre: '',
      duracionTipica: 0,
      duracionMinima: 0,
      duracionMaxima: 0,
      aforoHabitual: 0,
      descripcion: ''
    }
  };

  tiposEvento: TipoEvento[] = [];
  isEditMode: boolean = false;
  eventoId: number | null = null;
  errorMessage: string | null = null;
  duracionAyuda: { min: number, max: number } | null = null;

  constructor(
    private eventoService: EventoService,
    private tipoEventoService: TipoEventoService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // carga de todos los tipos de evento 
    this.tipoEventoService.getTipoEventos().subscribe(tipos => {
      this.tiposEvento = tipos;

      // suscribirse a los parámetros de la ruta para cargar el evento si está en modo edición
      this.route.paramMap.subscribe(params => {
        const id = params.get('id');
        if (id) {
          this.isEditMode = true;
          this.eventoId = +id; // convertir string a number
          this.eventoService.getEventoById(this.eventoId).subscribe((evento: Evento) => {
            this.nuevoEvento = evento;
            
            if (this.nuevoEvento.tipoEvento && this.nuevoEvento.tipoEvento.id) {
              const matchedTipo = this.tiposEvento.find(t => t.id === this.nuevoEvento.tipoEvento!.id);
              if (matchedTipo) {
                this.nuevoEvento.tipoEvento = matchedTipo;
              }
            }
          });
        }
      });
    });
  }

  guardar(): void {
    this.errorMessage = null;
    let operation: Observable<Evento>;

    if (this.isEditMode) {
      operation = this.eventoService.updateEvento(this.eventoId!, this.nuevoEvento);
    } else {
      operation = this.eventoService.createEvento(this.nuevoEvento);
    }

    operation.subscribe({
      next: () => {
        this.router.navigate(['/eventos']);
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 409 || err.status === 400) {
          if (typeof err.error === 'string') {
            this.errorMessage = err.error;
          } else {
            const errors = Object.values(err.error).join(', ');
            this.errorMessage = errors;
          }
        } else {
          this.errorMessage = 'Ocurrió un error inesperado al guardar el evento.';
        }
        console.error('Error al guardar el evento:', err);
      }
    });
  }

  onTipoEventoChange(): void {
    if (this.nuevoEvento.tipoEvento) {
      const tipoSeleccionado = this.tiposEvento.find(t => t.id === this.nuevoEvento.tipoEvento.id);
      if (tipoSeleccionado) {
        this.duracionAyuda = {
          min: tipoSeleccionado.duracionMinima,
          max: tipoSeleccionado.duracionMaxima
        };
        // Opcional: auto-rellenar la duración específica con la típica
        if (!this.isEditMode) {
          this.nuevoEvento.duracionEspecifica = tipoSeleccionado.duracionTipica;
        }
      }
    } else {
      this.duracionAyuda = null;
    }
  }

  cancelar(): void {
    this.router.navigate(['/eventos']);
  }

}