package es.cic.curso25.back.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.cic.curso25.back.modelo.TipoEvento;
import es.cic.curso25.back.services.TipoEventoService;

@RestController
@RequestMapping("/api/tipo_evento")
public class TipoEventoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoEventoController.class);

    @Autowired
    private TipoEventoService tipoEventoService;

    // metodo para obtener todos los tipos de evento
    @GetMapping
    public List<TipoEvento> getAllTipoEventos() {
        LOGGER.info("Obteniendo todos los tipos de evento");
        return tipoEventoService.findAll();
    }

    @GetMapping(params = "nombre")
    public List<TipoEvento> getTipoEventosByNombre(@RequestParam String nombre) {
        LOGGER.info("Obteniendo tipos de evento por nombre: {}", nombre);
        return tipoEventoService.findByNombre(nombre);
    }

    @PostMapping
    public TipoEvento createTipoEvento(@RequestBody TipoEvento tipoEvento) {
        LOGGER.info("Creando un nuevo tipo de evento");
        return tipoEventoService.create(tipoEvento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEvento> getTipoEventoById(@PathVariable Long id) {
        LOGGER.info("Obteniendo tipo de evento con id: {}", id);
        return tipoEventoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public TipoEvento updateTipoEvento(@PathVariable Long id, @RequestBody TipoEvento tipoEventoDetails) {
        LOGGER.info("Actualizando tipo de evento con id: {}", id);
        TipoEvento updatedTipoEvento = tipoEventoService.update(tipoEventoDetails);
        return updatedTipoEvento;
    }

    @DeleteMapping("/{id}")
    public void deleteTipoEvento(@PathVariable Long id) {
        LOGGER.info("Eliminando tipo de evento con id: {}", id);
        tipoEventoService.delete(id);
    }
}
