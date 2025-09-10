import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs/internal/Observable';
import { TipoEvento } from '../interface/tipo-evento';

@Injectable({
  providedIn: 'root'
})
export class TipoEventoService {

  private apiUrl = `${environment.apiUrl}/tipo_evento`;

  constructor(private http: HttpClient) { }

  // obtengo todos los tipos de evento 
  getTipoEventos(): Observable<TipoEvento[]> {
    return this.http.get<TipoEvento[]>(this.apiUrl);
  }

  createTipoEvento(tipoEvento: TipoEvento): Observable<TipoEvento> {
    return this.http.post<TipoEvento>(this.apiUrl, tipoEvento);
  }

    deleteTipoEvento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getTipoEventoById(id: number): Observable<TipoEvento> {
    return this.http.get<TipoEvento>(`${this.apiUrl}/${id}`);
  }

  updateTipoEvento(tipoEvento: TipoEvento): Observable<TipoEvento> {
    return this.http.put<TipoEvento>(`${this.apiUrl}/${tipoEvento.id}`, tipoEvento);
  }

  create(tipoEvento: TipoEvento): Observable<TipoEvento> {
    return this.http.post<TipoEvento>(this.apiUrl, tipoEvento);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
