import { Component, OnInit } from '@angular/core';
import { TipoEvento } from '../../interface/tipo-evento';
import { CommonModule } from '@angular/common';
import { TipoEventoService } from '../../services/tipo-evento.service';
import { Router } from '@angular/router';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-tipo-evento',
  standalone: true,
  imports: [CommonModule, NgbAlertModule],
  templateUrl: './tipo-evento.component.html',
  styleUrl: './tipo-evento.component.css'
})
export class TipoEventoComponent implements OnInit {
  tipoEvento: TipoEvento[] = [];
  errorMessage: string | null = null;

  constructor(
    private tipoEventoService: TipoEventoService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.cargarTiposEvento();
  }

  cargarTiposEvento(): void {
    this.errorMessage = null;
    this.tipoEventoService.getTipoEventos().subscribe((data: TipoEvento[]) => {
      this.tipoEvento = data;
    });
  }


  abrirModalCreacion(): void {
    this.router.navigate(['/crear-tipo-evento']);
  }

  eliminarTipoEvento(id: number): void {
    this.errorMessage = null;
    if (confirm('¿Estás seguro de que quieres eliminar este tipo de evento?')) {
      this.tipoEventoService.deleteTipoEvento(id).subscribe({
        next: () => {
          this.cargarTiposEvento();
        },
        error: (err: HttpErrorResponse) => {
          if (err.status === 409) {
            this.errorMessage = err.error;
          } else {
            this.errorMessage = 'Ocurrió un error inesperado al intentar eliminar.';
          }
        }
      });
    }
  }
  editarTipoEvento(id: number): void {
    this.router.navigate(['/editar-tipo-evento', id]);
  }

}





