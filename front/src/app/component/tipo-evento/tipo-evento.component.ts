import { Component } from '@angular/core';
import { TipoEvento } from '../../interface/tipo-evento';
import { NgFor } from '@angular/common';
import { TipoEventoService } from '../../services/tipo-evento.service';

@Component({
  selector: 'app-tipo-evento',
  standalone: true,
  imports: [NgFor],
  templateUrl: './tipo-evento.component.html',
  styleUrl: './tipo-evento.component.css'
})
export class TipoEventoComponent {
tipoEvento: TipoEvento[]= [];
  

  constructor(private tipoEventoService: TipoEventoService) {
  }

  ngOnInit(): void {
    this.tipoEventoService.getTipoEventos().subscribe((data: TipoEvento[]) => {
      this.tipoEvento = data;
    });
  }
}
