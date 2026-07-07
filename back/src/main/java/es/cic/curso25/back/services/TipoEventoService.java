package es.cic.curso25.back.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso25.back.controlleradvice.DuracionIncoherenteException;
import es.cic.curso25.back.controlleradvice.TipoEventoConEventosException;
import es.cic.curso25.back.controlleradvice.TipoEventoEmptyNameException;
import es.cic.curso25.back.controlleradvice.TipoEventoExistenteException;
import es.cic.curso25.back.controlleradvice.TipoEventoNotFoundException;
import es.cic.curso25.back.modelo.TipoEvento;
import es.cic.curso25.back.repository.TipoEventoRepository;

@Service
@Transactional
public class TipoEventoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoEventoService.class);

    @Autowired
    private TipoEventoRepository tipoEventoRepository;

    // Note: not @Transactional(readOnly = true) on purpose — this method mutates
    // managed entities (sets numeroEventos), so Hibernate's dirty checking would
    // conflict with a read-only transaction.
    @Transactional
    public List<TipoEvento> findAll() {

        LOGGER.info("Buscando todos los tipos de eventos");
        List<TipoEvento> tipos = tipoEventoRepository.findAll();
        // en esta operación de lectura no escribo en bd, pero sí modifico el objeto
        // para añadir el número de eventos asociados a cada tipo de evento
        for (TipoEvento tipo : tipos) {
            tipo.setNumeroEventos(tipo.getEventos().size());
        }
        return tipos;
    }

     @Transactional(readOnly = true)
    public List<TipoEvento> findByNombre(String nombre) {
        LOGGER.info("Buscando tipos de evento por nombre: {}", nombre);
        return tipoEventoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional(readOnly = true)
    public Optional<TipoEvento> findById(Long id) {
        LOGGER.info("Buscando tipo de evento con id: {}", id);
        return tipoEventoRepository.findById(id);
    }
    public TipoEvento create(TipoEvento tipoEvento) {

        LOGGER.info("Creando un nuevo tipo de evento");

        comprobacionNombre(tipoEvento);
        comprobarCoherenciaDuraciones(tipoEvento);
        return tipoEventoRepository.save(tipoEvento);
    }

    

    public TipoEvento update(TipoEvento tipoEvento) {

        LOGGER.info("Actualizando el tipo de evento con id: {}", tipoEvento.getId());
        comprobarCoherenciaDuraciones(tipoEvento);
        return tipoEventoRepository.save(tipoEvento);
    }

    public void delete(Long id) {

        LOGGER.info("Intentando eliminar el tipo de evento con id: {}", id);
        TipoEvento tipoEvento = tipoEventoRepository.findById(id)
                .orElseThrow(() -> new TipoEventoNotFoundException("Tipo de evento no encontrado con id: " + id));

        if (tipoEvento.getEventos() != null && !tipoEvento.getEventos().isEmpty()) {
            throw new TipoEventoConEventosException("No se puede eliminar el tipo de evento porque tiene " + tipoEvento.getEventos().size() + " eventos asociados.");
        }

        tipoEventoRepository.deleteById(id);
        LOGGER.info("Tipo de evento con id: {} eliminado correctamente", id);
    }

    private void comprobacionNombre(TipoEvento tipoEvento) {
        
        if (tipoEvento.getNombre() == null || tipoEvento.getNombre().isEmpty()) {
            throw new TipoEventoEmptyNameException("El nombre no puede ser nulo o vacío");
        } else {
            // comprobar que no existe otro tipo de evento con el mismo nombre
            TipoEvento tipo = tipoEventoRepository.findByNombreIgnoreCase(tipoEvento.getNombre());
            if (tipo != null) {
                throw new TipoEventoExistenteException("Ya existe un tipo de evento con el nombre: " + tipoEvento.getNombre());
            }
        }
    }

    private void comprobarCoherenciaDuraciones(TipoEvento tipoEvento) {
        if (tipoEvento.getDuracionMinima() > tipoEvento.getDuracionTipica() || tipoEvento.getDuracionTipica() > tipoEvento.getDuracionMaxima()) {
            throw new DuracionIncoherenteException("Las duraciones no son coherentes. Se debe cumplir: Mínima <= Típica <= Máxima.");
        }
    }
}
