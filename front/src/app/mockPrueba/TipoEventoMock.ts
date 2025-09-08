import { TipoEvento } from '../interface/tipo-evento';

export const TIPO_EVENTO_MOCK: TipoEvento[] = [
  {
    id: 1,
    nombre: "Concierto",
    duracionTipica: 120,
    duracionMinima: 90,
    duracionMaxima: 180,
    aforoHabitual: 500,
    numeroEventos: 15
  },
  {
    id: 2,
    nombre: "Conferencia",
    duracionTipica: 60,
    duracionMinima: 45,
    duracionMaxima: 90,
    aforoHabitual: 200,
    numeroEventos: 8
  },
  {
    id: 3,
    nombre: "Taller",
    duracionTipica: 180,
    duracionMinima: 120,
    duracionMaxima: 240,
    aforoHabitual: 30,
    numeroEventos: 5
  }
];