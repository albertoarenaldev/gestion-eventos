import { Component } from '@angular/core';
import { TipoEvento } from '../../interface/tipo-evento';
import { TIPO_EVENTO_MOCK } from '../../mockPrueba/TipoEventoMock';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-tipo-evento',
  standalone: true,
  imports: [NgFor],
  templateUrl: './tipo-evento.component.html',
  styleUrl: './tipo-evento.component.css'
})
export class TipoEventoComponent {
TipoEvento: TipoEvento[]= TIPO_EVENTO_MOCK
}
