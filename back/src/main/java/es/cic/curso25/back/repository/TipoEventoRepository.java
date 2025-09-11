package es.cic.curso25.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.cic.curso25.back.modelo.TipoEvento;


public interface TipoEventoRepository extends JpaRepository<TipoEvento, Long> {

    List<TipoEvento> findByNombreContainingIgnoreCase(String nombre);

    TipoEvento findByNombreIgnoreCase(String nombre);
    
}
