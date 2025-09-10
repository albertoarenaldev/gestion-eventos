package es.cic.curso25.back.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class TipoEvento {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false, unique = true)
private String nombre;

private int duracionTipica;
private int duracionMinima;
private int duracionMaxima;

private String descripcion;

private int aforoHabitual;

@OneToMany(mappedBy = "tipoEvento")
private List<Evento> eventos;

@Transient
private int numeroEventos;

public int getNumeroEventos() {
    return numeroEventos;
}

public void setNumeroEventos(int numeroEventos) {
    this.numeroEventos = numeroEventos;
}

public String getDescripcion() {
    return descripcion;
}

public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getNombre() {
    return nombre;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

public int getDuracionTipica() {
    return duracionTipica;
}

public void setDuracionTipica(int duracionTipica) {
    this.duracionTipica = duracionTipica;
}

public int getDuracionMinima() {
    return duracionMinima;
}

public void setDuracionMinima(int duracionMinima) {
    this.duracionMinima = duracionMinima;
}

public int getDuracionMaxima() {
    return duracionMaxima;
}

public void setDuracionMaxima(int duracionMaxima) {
    this.duracionMaxima = duracionMaxima;
}

public int getAforoHabitual() {
    return aforoHabitual;
}

public void setAforoHabitual(int aforoHabitual) {
    this.aforoHabitual = aforoHabitual;
}

public List<Evento> getEventos() {
    return eventos;
}

public void setEventos(List<Evento> eventos) {
    this.eventos = eventos;
}

@Override
public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
}

@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    TipoEvento other = (TipoEvento) obj;
    if (id == null) {
        if (other.id != null)
            return false;
    } else if (!id.equals(other.id))
        return false;
    return true;
}

@Override
public String toString() {
    return "TipoEvento [id=" + id + ", nombre=" + nombre + ", duracionTipica=" + duracionTipica + ", duracionMinima="
            + duracionMinima + ", duracionMaxima=" + duracionMaxima + ", aforoHabitual=" + aforoHabitual + ", eventos="
            + eventos + "]";
}



}
