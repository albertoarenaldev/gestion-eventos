package es.cic.curso25.back.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.cic.curso25.back.modelo.Evento;
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
        return eventoRepository.save(evento);
    }

    public Evento update(Evento evento) {
        LOGGER.info("Actualizando el evento con id: {}", evento.getId());
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
}
