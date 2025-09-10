package es.cic.curso25.back.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import es.cic.curso25.back.modelo.Evento;
import es.cic.curso25.back.services.EventoService;

@RestController
@RequestMapping("/api/evento")
public class EventoController {

      private static final Logger LOGGER = LoggerFactory.getLogger(EventoController.class);

      @Autowired
      private EventoService eventoService;

      @GetMapping
      public List<Evento> getAllEventos() {
            LOGGER.info("Obteniendo todos los eventos");
            return eventoService.findAll();
      }

      @GetMapping("/{id}")
      public Evento getEventoById(@PathVariable Long id) {
            LOGGER.info("Obteniendo evento con id: {}", id);
            return eventoService.findById(id);
      }

      @GetMapping("/hoy")
      public List<Evento> getEventosDeHoy() {
            LOGGER.info("Obteniendo eventos de hoy");
            return eventoService.findEventosDeHoy();
      }

      @PostMapping
      public Evento createEvento(@RequestBody Evento evento) {
            LOGGER.info("Creando un nuevo evento");
            return eventoService.create(evento);
      }

      @PutMapping("/{id}")
      public Evento updateEvento(@PathVariable Long id, @RequestBody Evento detallesEvento) {
            LOGGER.info("Actualizando evento con id: {}", id);
            Evento evento = eventoService.findById(id);
            if (evento == null) {
                  return null;
            }
            detallesEvento.setId(id);
            return eventoService.update(detallesEvento);
      }

      @DeleteMapping("/{id}")
      public void deleteEvento(@PathVariable Long id) {
            LOGGER.info("Eliminando evento con id: {}", id);
            Evento evento = eventoService.findById(id);
            if (evento == null) {
                  return;
            }
            eventoService.delete(id);
            return;
      }

}
