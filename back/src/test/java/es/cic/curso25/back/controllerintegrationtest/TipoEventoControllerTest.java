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
import es.cic.curso25.back.repository.TipoEventoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class TipoEventoControllerTest {

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
        tipoEventoRepository.deleteAll();

        tipoEvento1 = new TipoEvento();
        tipoEvento1.setNombre("Conferencia");
        tipoEvento1 = tipoEventoRepository.save(tipoEvento1);

        tipoEvento2 = new TipoEvento();
        tipoEvento2.setNombre("Taller");
        tipoEvento2 = tipoEventoRepository.save(tipoEvento2);

        tipoEvento3 = new TipoEvento();
        tipoEvento3.setNombre("Seminario");
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

        mockMvc.perform(post("/api/tipo_evento")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevoTipoEvento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Webinar"));
    }


}
