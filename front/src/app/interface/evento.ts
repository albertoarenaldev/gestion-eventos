import { TipoEvento } from './tipo-evento';

export interface Evento {
    id?: number;
    nombre: string;
    fechaHora: string;
    duracionEspecifica: number;
    aforoEspecifico: number;
    descripcion: string;
    lugar: string;
    tipoEvento: TipoEvento;
}