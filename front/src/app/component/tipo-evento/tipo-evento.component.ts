import { Component, OnInit } from '@angular/core';
import { TipoEvento } from '../../interface/tipo-evento';
import { NgFor } from '@angular/common';
import { CommonModule } from '@angular/common';
import { TipoEventoService } from '../../services/tipo-evento.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-tipo-evento',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tipo-evento.component.html',
  styleUrl: './tipo-evento.component.css'
})
export class TipoEventoComponent implements OnInit {
  tipoEvento: TipoEvento[] = [];
  mostrarFormulario = false;
  nuevoTipoEvento: TipoEvento = {
    nombre: '',
    duracionTipica: 0,
    duracionMinima: 0,
    duracionMaxima: 0,
    aforoHabitual: 0
  };
  constructor(private tipoEventoService: TipoEventoService) {
  }

  ngOnInit(): void {
    this.tipoEventoService.getTipoEventos().subscribe((data: TipoEvento[]) => {
      this.tipoEvento = data;
    });
  }

cargarTiposEvento(): void {
    this.tipoEventoService.getTipoEventos().subscribe((data: TipoEvento[]) => {
      this.tipoEvento = data;
    });
  }

 abrirFormulario(): void {
  this.mostrarFormulario = true;
}

cancelarCreacion(): void {
  this.mostrarFormulario = false;
  this.resetFormulario();
}

  crearTipoEvento(): void {
    this.tipoEventoService.createTipoEvento(this.nuevoTipoEvento).subscribe(() => {
      this.cargarTiposEvento();
      this.cancelarCreacion();
    });
  }

  eliminarTipoEvento(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar este tipo de evento?')) {
      this.tipoEventoService.deleteTipoEvento(id).subscribe({
        next: () => {
          this.cargarTiposEvento();
        },
        error: (err) => {
          if (err.status === 409) {
            alert('Error: No se puede eliminar el tipo de evento porque está en uso.');
          } else {
            alert('Ocurrió un error inesperado al intentar eliminar.');
          }
        }
      });
    }
  }

  resetFormulario(): void {
    this.nuevoTipoEvento = {
      nombre: '',
      duracionTipica: 0,
      duracionMinima: 0,
      duracionMaxima: 0,
      aforoHabitual: 0
    };
  }

}
