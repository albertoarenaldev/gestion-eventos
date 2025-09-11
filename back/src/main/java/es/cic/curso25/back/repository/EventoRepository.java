package es.cic.curso25.back.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.cic.curso25.back.modelo.Evento;


public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Evento> findByNombreContainingIgnoreCase(String nombre);

    List<Evento> findByLugar(String lugar);

}
