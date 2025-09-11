package es.cic.curso25.back.controllerintegrationtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import es.cic.curso25.back.modelo.TipoEvento;
import es.cic.curso25.back.repository.EventoRepository;
import es.cic.curso25.back.repository.TipoEventoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class TipoEventoControllerTest {

    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    TipoEventoRepository tipoEventoRepository;

    @Autowired
    TipoEventoController tipoEventoController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    TipoEvento tipoEvento1;
    TipoEvento tipoEvento2;
    TipoEvento tipoEvento3;

    @BeforeEach
    void setUp() {
        eventoRepository.deleteAll(); 
        tipoEventoRepository.deleteAll();
        tipoEventoRepository.deleteAll();

        tipoEvento1 = new TipoEvento();
        tipoEvento1.setNombre("Conferencia");
        tipoEvento1.setDuracionMinima(1);
        tipoEvento1.setDuracionTipica(60);
        tipoEvento1.setDuracionMaxima(120);
        tipoEvento1.setAforoHabitual(100);
        tipoEvento1 = tipoEventoRepository.save(tipoEvento1);

        tipoEvento2 = new TipoEvento();
        tipoEvento2.setNombre("Taller");
        tipoEvento2.setDuracionMinima(1);
        tipoEvento2.setDuracionTipica(120);
        tipoEvento2.setDuracionMaxima(240);
        tipoEvento2.setAforoHabitual(50);
        tipoEvento2 = tipoEventoRepository.save(tipoEvento2);

        tipoEvento3 = new TipoEvento();
        tipoEvento3.setNombre("Seminario");
        tipoEvento3.setDuracionMinima(1);
        tipoEvento3.setDuracionTipica(90);
        tipoEvento3.setDuracionMaxima(180);
        tipoEvento3.setAforoHabitual(80);
        tipoEvento3 = tipoEventoRepository.save(tipoEvento3);
    }

    @Test
    void testGetAll() throws Exception {

        mockMvc.perform(get("/api/tipo_evento"))

                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<TipoEvento> tipoEventos = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<List<TipoEvento>>() {
                            });
                    assertEquals(3, tipoEventos.size());
                });
    }

    @Test
    void testCreate() throws Exception {
        TipoEvento nuevoTipoEvento = new TipoEvento();
        nuevoTipoEvento.setNombre("Webinar");
        nuevoTipoEvento.setDuracionMinima(10);
        nuevoTipoEvento.setDuracionTipica(60);
        nuevoTipoEvento.setDuracionMaxima(120);
        nuevoTipoEvento.setAforoHabitual(50);

        mockMvc.perform(post("/api/tipo_evento")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevoTipoEvento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Webinar"));
    }

}
