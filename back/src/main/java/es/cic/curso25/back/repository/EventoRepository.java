package es.cic.curso25.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.cic.curso25.back.modelo.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

}
