import { Component, OnInit } from '@angular/core';
import { EventoService } from '../../services/evento.service';
import { Evento } from '../../interface/evento';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-evento-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './evento-list.component.html',
  styleUrl: './evento-list.component.css'
})
export class EventoListComponent implements OnInit {

  eventos: Evento[] = [];
  searchTerm: string = '';

  constructor(private eventoService: EventoService, private router: Router) { }

  ngOnInit(): void {
    this.cargarEventos();
  }

  cargarEventos(nombre?: string): void {
    if (nombre) {
      this.eventoService.searchEventos(nombre).subscribe((data: Evento[]) => {
        this.eventos = data;
      });
    } else {
      this.eventoService.getEventos().subscribe((data: Evento[]) => {
        this.eventos = data;
      });
    }
  }

  buscarEventos(): void {
    this.cargarEventos(this.searchTerm);
  }

  limpiarBusqueda(): void {
    this.searchTerm = '';
    this.cargarEventos();
  }

  crearEvento(): void {
    this.router.navigate(['/crear-evento']);
  }

  editarEvento(id: number): void {
    this.router.navigate(['/editar-evento', id]);
  }

  eliminarEvento(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar este evento?')) {
      this.eventoService.deleteEvento(id).subscribe({
        next: () => {
          this.cargarEventos();
        },
        error: (err: HttpErrorResponse) => {
          if (err.status === 409) {
            alert('Error: No se puede eliminar el evento porque está en uso.');
          } else {
            alert('Ocurrió un error inesperado al intentar eliminar.');
          }
        }
      });
    }
  }

}
