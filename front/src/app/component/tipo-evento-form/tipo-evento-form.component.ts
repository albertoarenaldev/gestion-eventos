import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TipoEvento } from '../../interface/tipo-evento';
import { TipoEventoService } from '../../services/tipo-evento.service';


@Component({
  selector: 'app-tipo-evento-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tipo-evento-form.component.html',
  styleUrl: './tipo-evento-form.component.css'
})
export class TipoEventoFormComponent {
  nuevoTipoEvento: TipoEvento = {
    nombre: '',
    duracionTipica: 0,
    duracionMinima: 0,
    duracionMaxima: 0,
    aforoHabitual: 0,
    descripcion: ''
  };
    constructor(
    private tipoEventoService: TipoEventoService,
    private router: Router
  ) {}

  guardar(): void {
    this.tipoEventoService.createTipoEvento(this.nuevoTipoEvento).subscribe(() => {
      this.router.navigate(['/tipo-eventos']);
    });
  }

  cancelar(): void {
    this.router.navigate(['/tipo-eventos']);
  }
}
