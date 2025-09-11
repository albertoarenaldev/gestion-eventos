package es.cic.curso25.back.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso25.back.controlleradvice.ConflictoSolapamientoException;
import es.cic.curso25.back.controlleradvice.DuracionEventoInvalidaException;
import es.cic.curso25.back.modelo.Evento;
import es.cic.curso25.back.modelo.TipoEvento;
import es.cic.curso25.back.repository.EventoRepository;

@Service
@Transactional
public class EventoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventoService.class);

    @Autowired
    private EventoRepository eventoRepository;

    @Transactional(readOnly = true)
    public List<Evento> findAll() {
        LOGGER.info("Buscando todos los eventos");
        return eventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Evento findById(Long id) {
        LOGGER.info("Buscando evento con id: {}", id);
        return eventoRepository.findById(id).orElse(null);
    }

     @Transactional(readOnly = true)
    public List<Evento> findByNombre(String nombre) {
        LOGGER.info("Buscando eventos por nombre: {}", nombre);
        return eventoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    

    public Evento create(Evento evento) {
        LOGGER.info("Creando un nuevo evento");
        validarEvento(evento);
        return eventoRepository.save(evento);
    }

    public Evento update(Evento evento) {
        LOGGER.info("Actualizando el evento con id: {}", evento.getId());
        validarEvento(evento);
        return eventoRepository.save(evento);
    }

    public void delete(Long id) {
        LOGGER.info("Eliminando el evento con id: {}", id);
        eventoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Evento> findEventosDeHoy() {
        LOGGER.info("Buscando eventos para el día de hoy");
        LocalDate hoy = LocalDate.now();
        return eventoRepository.findByFechaHoraBetween(hoy.atStartOfDay(), hoy.atTime(LocalTime.MAX));
    }

    private void validarEvento(Evento evento) {
        // 1. Validar coherencia de duración
        TipoEvento tipo = evento.getTipoEvento();
        if (tipo != null) {
            if (evento.getDuracionEspecifica() < tipo.getDuracionMinima() || evento.getDuracionEspecifica() > tipo.getDuracionMaxima()) {
                throw new DuracionEventoInvalidaException(
                    "La duración del evento (" + evento.getDuracionEspecifica() + " min) está fuera del rango permitido por el tipo de evento '" + tipo.getNombre() + "' (" + tipo.getDuracionMinima() + " - " + tipo.getDuracionMaxima() + " min)."
                );
            }
        }

        // 2. Validar solapamiento de lugar y tiempo
        List<Evento> eventosEnMismoLugar = eventoRepository.findByLugar(evento.getLugar());
        
        LocalDateTime inicioNuevo = evento.getFechaHora();
        LocalDateTime finNuevo = inicioNuevo.plusMinutes(evento.getDuracionEspecifica());

        for (Evento eventoExistente : eventosEnMismoLugar) {
            // Ignorar el mismo evento si es una actualización
            if (evento.getId() != null && evento.getId().equals(eventoExistente.getId())) {
                continue;
            }

            LocalDateTime inicioExistente = eventoExistente.getFechaHora();
            LocalDateTime finExistente = inicioExistente.plusMinutes(eventoExistente.getDuracionEspecifica());

            // Comprobar solapamiento: (InicioA < FinB) y (FinA > InicioB)
            if (inicioNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente)) {
                throw new ConflictoSolapamientoException(
                    "Conflicto de horario: El evento se solapa con '" + eventoExistente.getNombre() + "' (" + inicioExistente.toLocalTime() + " - " + finExistente.toLocalTime() + ") en el mismo lugar."
                );
            }
        }
    }
}
