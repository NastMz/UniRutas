package com.unirutas.models;

import java.util.Date;
import java.util.List;

public class Alerta {
    private final Date fecha;
    private final String descripcion;
    private final Servicio servicio;

    public Alerta(Date fecha, String descripcion, Servicio servicio) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.servicio = servicio;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void enviarAlerta(List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Estudiante estudiante) {
                if (estudiante.estaSuscrito(servicio)) {
                    estudiante.recibirAlerta(this);
                    System.out.println("Alerta enviada a " + estudiante.getNombre() + ": " + this.descripcion);
                }
            } else if (usuario instanceof Administrativo) {
                usuario.recibirAlerta(this);
                System.out.println("Alerta enviada a " + usuario.getNombre() + ": " + this.descripcion);
            } else {
                System.out.println("Error al enviar la alerta.");
            }
        }
    }
}
