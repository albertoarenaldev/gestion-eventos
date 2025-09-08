package es.cic.curso25.back.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return tipoEventoRepository.findAll();
    }

}
