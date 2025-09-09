package es.cic.curso25.back.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping
    public TipoEvento createTipoEvento(@RequestBody TipoEvento tipoEvento) {
        LOGGER.info("Creando un nuevo tipo de evento");
        return tipoEventoService.create(tipoEvento);
    }
}
    

