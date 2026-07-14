import { Component, OnInit } from '@angular/core';
import { EventoService } from '../../services/evento.service';
import { Evento } from '../../interface/evento';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  eventosDeHoy: Evento[] = [];

  constructor(private eventoService: EventoService) { }

  ngOnInit(): void {
    this.eventoService.getEventosDeHoy().subscribe((data: Evento[]) => {
      this.eventosDeHoy = data;
    });
  }
}