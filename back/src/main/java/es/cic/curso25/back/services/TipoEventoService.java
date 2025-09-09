package es.cic.curso25.back.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso25.back.controlleradvice.TipoEventoEmptyNameException;
import es.cic.curso25.back.modelo.TipoEvento;
import es.cic.curso25.back.repository.TipoEventoRepository;

@Service
@Transactional
public class TipoEventoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoEventoService.class);

    @Autowired
    private TipoEventoRepository tipoEventoRepository;

    @Transactional(readOnly = true)
    public List<TipoEvento> findAll() {

        LOGGER.info("Buscando todos los tipos de eventos");
        return tipoEventoRepository.findAll();
    }

    public TipoEvento create(TipoEvento tipoEvento) {

        LOGGER.info("Creando un nuevo tipo de evento");

        comprobacionNombre(tipoEvento);
        return tipoEventoRepository.save(tipoEvento);
    }

    

    public TipoEvento update(TipoEvento tipoEvento) {

        LOGGER.info("Actualizando el tipo de evento con id: {}", tipoEvento.getId());
        return tipoEventoRepository.save(tipoEvento);
    }

    public void delete(Long id) {

        LOGGER.info("Eliminando el tipo de evento con id: {}", id);
        tipoEventoRepository.deleteById(id);
    }

    private void comprobacionNombre(TipoEvento tipoEvento) {
        
        if (tipoEvento.getNombre() == null || tipoEvento.getNombre().isEmpty()) {
            throw new TipoEventoEmptyNameException("El nombre no puede ser nulo o vacío");
        } else {
            // comprobar que no existe otro tipo de evento con el mismo nombre
            List<TipoEvento> tipos = tipoEventoRepository.findByNombre(tipoEvento.getNombre());
            if (!tipos.isEmpty()) {
                throw new IllegalArgumentException("Ya existe un tipo de evento con el nombre: " + tipoEvento.getNombre());
            }
        }
    }
}
