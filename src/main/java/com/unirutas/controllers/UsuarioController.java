package com.unirutas.controllers;

import com.unirutas.models.*;
import com.unirutas.repository.GenericDTO;

public class UsuarioController {

    private final GenericDTO<Estudiante> estudianteDTO = new GenericDTO<>(Estudiante.class, "Student");
    private final GenericDTO<Administrativo> administrativoDTO = new GenericDTO<>(Administrativo.class, "Administrative");

    public void crearEstudiante(Estudiante estudiante) {
        estudianteDTO.insert(estudiante);
    }

    public void crearAdministrativo(Administrativo administrativo) {
        administrativoDTO.insert(administrativo);
    }

    public void eliminarEstudiante(Estudiante estudiante) {
        estudianteDTO.delete(estudiante);
    }

    public void eliminarAdministrativo(Administrativo administrativo) {
        administrativoDTO.delete(administrativo);
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        estudianteDTO.update(estudiante);
    }

    public void actualizarAdministrativo(Administrativo administrativo) {
        administrativoDTO.update(administrativo);
    }

    public void marcarAlerta(Alerta alerta, Usuario usuario) {
        // TODO: Implementar la lógica para añadirlo en la DB
        if (usuario != null) {
            usuario.marcarAlertaLeida(alerta);
            System.out.println("Alerta marcada como leída por el usuario: " + usuario.getNombre());
        } else {
            System.out.println("Error: Usuario no encontrado.");
        }
    }

    public void suscribirServicio(Estudiante estudiante, Servicio servicio) {
        // TODO: Implementar la lógica para añadirlo en la DB
        if (estudiante != null && servicio != null) {
            estudiante.suscribirAServicio(servicio);
            System.out.println(estudiante.getNombre() + " se ha suscrito al servicio " + servicio);
        } else {
            System.out.println("Error: Estudiante o servicio no encontrados.");
        }
    }

}


