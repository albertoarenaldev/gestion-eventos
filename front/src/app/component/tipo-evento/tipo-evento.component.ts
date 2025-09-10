import { Component, OnInit } from '@angular/core';
import { TipoEvento } from '../../interface/tipo-evento';
import { CommonModule } from '@angular/common';
import { TipoEventoService } from '../../services/tipo-evento.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tipo-evento',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tipo-evento.component.html',
  styleUrl: './tipo-evento.component.css'
})
export class TipoEventoComponent implements OnInit {
  tipoEvento: TipoEvento[] = [];

  constructor(
  private tipoEventoService: TipoEventoService,
  private router: Router
) {
}

ngOnInit(): void {
  this.cargarTiposEvento();
}

cargarTiposEvento(): void {
  this.tipoEventoService.getTipoEventos().subscribe((data: TipoEvento[]) => {
    this.tipoEvento = data;
  });
}


abrirModalCreacion(): void {
  this.router.navigate(['/crear-tipo-evento']);
}

eliminarTipoEvento(id: number): void {
  if(confirm('¿Estás seguro de que quieres eliminar este tipo de evento?')) {
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


}
  




