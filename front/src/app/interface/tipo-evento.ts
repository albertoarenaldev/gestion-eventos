export interface TipoEvento {
    id?: number;
    nombre: string;
    duracionTipica: number
    duracionMinima: number;
    duracionMaxima: number;
    aforoHabitual: number;
    descripcion?: string;


    //numero de eventos del tipo de evento buscado
    numeroEventos?: number;
}
