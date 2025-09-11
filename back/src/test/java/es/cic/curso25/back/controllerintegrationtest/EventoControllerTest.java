package es.cic.curso25.back.controllerintegrationtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        eventoRepository.deleteAll();
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

        Evento1 = new Evento();
        Evento1.setNombre("Conferencia cultural");
        Evento1.setTipoEvento(tipoEvento1);
        Evento1.setLugar("Sala A");
        Evento1.setFechaHora(LocalDateTime.now().plusDays(1).withHour(10));
        Evento1.setDuracionEspecifica(60);
        Evento1.setAforoEspecifico(50);
        Evento1 = eventoRepository.save(Evento1);

        Evento2 = new Evento();
        Evento2.setNombre("Taller de java");
        Evento2.setTipoEvento(tipoEvento2);
        Evento2.setLugar("Aula B");
        Evento2.setFechaHora(LocalDateTime.now().plusDays(2).withHour(14));
        Evento2.setDuracionEspecifica(120);
        Evento2.setAforoEspecifico(20);
        Evento2 = eventoRepository.save(Evento2);

        Evento3 = new Evento();
        Evento3.setNombre("Seminario rural");
        Evento3.setTipoEvento(tipoEvento3);
        Evento3.setLugar("Granja C");
        Evento3.setFechaHora(LocalDateTime.now().plusDays(3).withHour(9));
        Evento3.setDuracionEspecifica(90);
        Evento3.setAforoEspecifico(30);
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

    @Test
    void testGetEventosDeHoy() throws Exception {

        // evento con fecha de hoy
        Evento eventoHoy = new Evento();
        eventoHoy.setNombre("Evento de hoy");
        eventoHoy.setTipoEvento(tipoEvento1);
        eventoHoy.setFechaHora(LocalDateTime.now().plusMinutes(1));
        eventoHoy.setLugar("Sala de Hoy");
        eventoHoy.setDuracionEspecifica(60);
        eventoHoy.setAforoEspecifico(50);
        eventoRepository.save(eventoHoy);

        // evento con fecha de pasado mañana
        Evento eventoAyer = new Evento();
        eventoAyer.setNombre("Evento de ayer");
        eventoAyer.setTipoEvento(tipoEvento2);
        eventoAyer.setFechaHora(LocalDateTime.now().plusDays(2).withHour(12));
        eventoAyer.setLugar("Sala de Pasado Mañana");
        eventoAyer.setDuracionEspecifica(120);
        eventoAyer.setAforoEspecifico(100);
        eventoRepository.save(eventoAyer);

        // evento con fecha de mañana
        Evento eventoManana = new Evento();
        eventoManana.setNombre("Evento de mañana");
        eventoManana.setTipoEvento(tipoEvento3);
        eventoManana.setFechaHora(LocalDateTime.now().plusDays(1).withHour(9));
        eventoManana.setLugar("Sala de Mañana");
        eventoManana.setDuracionEspecifica(90);
        eventoManana.setAforoEspecifico(80);
        eventoRepository.save(eventoManana);

        mockMvc.perform(get("/api/evento/hoy"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Evento> eventos = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<List<Evento>>() {
                            });
                    assertEquals(1, eventos.size());
                    assertEquals("Evento de hoy", eventos.get(0).getNombre());
                });
    }

    @Test
    void testCreateEvento() throws Exception {
        Evento nuevoEvento = new Evento();
        nuevoEvento.setNombre("Concierto Maldita nerea");
        nuevoEvento.setTipoEvento(tipoEvento1);
        nuevoEvento.setFechaHora(LocalDateTime.now().plusDays(1).withHour(20));
        nuevoEvento.setLugar("Sala de Conciertos");
        nuevoEvento.setDuracionEspecifica(120);
        nuevoEvento.setAforoEspecifico(300);

        mockMvc.perform(post("/api/evento")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevoEvento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Concierto Maldita nerea"))
                .andExpect(jsonPath("$.tipoEvento.id").value(tipoEvento1.getId()));
    }

    @Test
    void testUpdateEvento() throws Exception {
        Evento evento = new Evento();
        evento.setNombre("Concierto Maldita nerea");
        evento.setTipoEvento(tipoEvento1);
        evento.setFechaHora(LocalDateTime.now().plusMinutes(1));
        evento.setLugar("Lugar Original");
        evento.setDuracionEspecifica(60);
        evento.setAforoEspecifico(100);
        evento = eventoRepository.save(evento);

        Evento actualizado = new Evento();
        actualizado.setNombre("Taller Sueño");
        actualizado.setTipoEvento(tipoEvento2);
        actualizado.setFechaHora(LocalDateTime.now().plusDays(1).withHour(10));
        actualizado.setLugar("Aula Magna");
        actualizado.setDuracionEspecifica(90);
        actualizado.setAforoEspecifico(80);

        mockMvc.perform(put("/api/evento/" + evento.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(evento.getId()))
                .andExpect(jsonPath("$.nombre").value("Taller Sueño"))
                .andExpect(jsonPath("$.tipoEvento.id").value(tipoEvento2.getId()));
    }

    @Test
    void testDeleteEvento() throws Exception {
        Evento evento = new Evento();
        evento.setNombre("Evento a borrar");
        evento.setTipoEvento(tipoEvento1);
        evento.setFechaHora(LocalDateTime.now().plusDays(1).withHour(10));
        evento.setLugar("Sala de Borrado");
        evento.setDuracionEspecifica(30);
        evento.setAforoEspecifico(10);

        evento = eventoRepository.save(evento);

        mockMvc.perform(delete("/api/evento/" + evento.getId()))
                .andExpect(status().isOk());

        // verifica si existe el evento en la bd
        boolean exists = eventoRepository.findById(evento.getId()).isPresent();
        assertFalse(exists);
    }

    @Test
    void testUpdateEventoNoExistente() throws Exception {
        Evento actualizado = new Evento();
        actualizado.setNombre("Evento que no existe");
        actualizado.setTipoEvento(tipoEvento1);
        actualizado.setFechaHora(LocalDateTime.now().plusDays(1).withHour(10));
        actualizado.setLugar("Lugar Inexistente");
        actualizado.setDuracionEspecifica(45);
        actualizado.setAforoEspecifico(20);

        mockMvc.perform(put("/api/evento/9999")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertTrue(response.isEmpty());
                });
    }

}
