package es.cic.curso25.back.controllerintegrationtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.back.controller.TipoEventoController;
import es.cic.curso25.back.modelo.Evento;
import es.cic.curso25.back.modelo.TipoEvento;
import es.cic.curso25.back.repository.EventoRepository;
import es.cic.curso25.back.repository.TipoEventoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class EventoControllerTest {

    @Autowired
    TipoEventoRepository tipoEventoRepository;

    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    TipoEventoController tipoEventoController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Evento Evento1;
    Evento Evento2;
    Evento Evento3;

    TipoEvento tipoEvento1;
    TipoEvento tipoEvento2;
    TipoEvento tipoEvento3;

    @BeforeEach
    void setUp() {

        tipoEventoRepository.deleteAll();

        eventoRepository.deleteAll();

        tipoEvento1 = new TipoEvento();
        tipoEvento1.setNombre("Conferencia");
        tipoEvento1 = tipoEventoRepository.save(tipoEvento1);

        tipoEvento2 = new TipoEvento();
        tipoEvento2.setNombre("Taller");
        tipoEvento2 = tipoEventoRepository.save(tipoEvento2);

        tipoEvento3 = new TipoEvento();
        tipoEvento3.setNombre("Seminario");
        tipoEvento3 = tipoEventoRepository.save(tipoEvento3);

        Evento1 = new Evento();
        Evento1.setNombre("Conferencia cultural");
        Evento1.setTipoEvento(tipoEvento1);
        Evento1 = eventoRepository.save(Evento1);

        Evento2 = new Evento();
        Evento2.setNombre("Taller de java");
        Evento2.setTipoEvento(tipoEvento2);
        Evento2 = eventoRepository.save(Evento2);

        Evento3 = new Evento();
        Evento3.setNombre("Seminario rural");
        Evento3.setTipoEvento(tipoEvento3);
        Evento3 = eventoRepository.save(Evento3);
    }

    @Test
    void testGetAllEventos() throws Exception {
        mockMvc.perform(get("/api/evento"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Evento> eventos = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<List<Evento>>() {
                            });
                    assertEquals(3, eventos.size());

                });
    }

}
