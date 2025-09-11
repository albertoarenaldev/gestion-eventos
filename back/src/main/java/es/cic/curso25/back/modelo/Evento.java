package es.cic.curso25.back.modelo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Evento {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@NotEmpty(message = "El nombre no puede estar vacío")
private String nombre;

@NotNull(message = "La fecha y hora no pueden ser nulas")
@Future(message = "La fecha del evento debe ser en el futuro")
private LocalDateTime fechaHora;

@Positive(message = "La duración debe ser un número positivo")
private int duracionEspecifica;

@Positive(message = "El aforo debe ser un número positivo")
private int aforoEspecifico;

private String descripcion;

@NotEmpty(message = "El lugar no puede estar vacío")
private String lugar;

@ManyToOne(optional = false)
@JoinColumn(name = "tipo_evento_id", nullable = false)
@JsonIgnoreProperties("eventos")
@NotNull(message = "El tipo de evento no puede ser nulo")
private TipoEvento tipoEvento;

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

public LocalDateTime getFechaHora() {
    return fechaHora;
}

public void setFechaHora(LocalDateTime fechaHora) {
    this.fechaHora = fechaHora;
}

public int getDuracionEspecifica() {
    return duracionEspecifica;
}

public void setDuracionEspecifica(int duracionEspecifica) {
    this.duracionEspecifica = duracionEspecifica;
}

public int getAforoEspecifico() {
    return aforoEspecifico;
}

public void setAforoEspecifico(int aforoEspecifico) {
    this.aforoEspecifico = aforoEspecifico;
}

public String getDescripcion() {
    return descripcion;
}

public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
}

public TipoEvento getTipoEvento() {
    return tipoEvento;
}

public void setTipoEvento(TipoEvento tipoEvento) {
    this.tipoEvento = tipoEvento;
}

public String getLugar() {
    return lugar;
}

public void setLugar(String lugar) {
    this.lugar = lugar;
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
    Evento other = (Evento) obj;
    if (id == null) {
        if (other.id != null)
            return false;
    } else if (!id.equals(other.id))
        return false;
    return true;
}

@Override
public String toString() {
    return "Evento [id=" + id + ", nombre=" + nombre + ", fechaHora=" + fechaHora + ", duracionEspecifica="
            + duracionEspecifica + ", aforoEspecifico=" + aforoEspecifico + ", descripcion=" + descripcion + ", lugar="
            + lugar + ", tipoEvento=" + tipoEvento + "]";
}




}
