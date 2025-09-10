import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { Evento } from '../interface/evento';
@Injectable({
  providedIn: 'root'
})
export class EventoService {

  private apiUrl = `${environment.apiUrl}/evento`;

  constructor(private http: HttpClient) { }

  getEventos(): Observable<Evento[]> {
    return this.http.get<Evento[]>(this.apiUrl);
  }

  getEventosDeHoy(): Observable<Evento[]> {
    return this.http.get<Evento[]>(`${this.apiUrl}/hoy`);
  }

  createEvento(evento: Evento): Observable<Evento> {
    return this.http.post<Evento>(this.apiUrl, evento);
  }

  updateEvento(id: number, evento: Evento): Observable<Evento> {
    return this.http.put<Evento>(`${this.apiUrl}/${id}`, evento);
  }

  deleteEvento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

   searchEventos(nombre: string): Observable<Evento[]> {
    return this.http.get<Evento[]>(`${this.apiUrl}?nombre=${nombre}`);
  }

  getEventoById(id: number): Observable<Evento> {
    return this.http.get<Evento>(`${this.apiUrl}/${id}`);
  }
}