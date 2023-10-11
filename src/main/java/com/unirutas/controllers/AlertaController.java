package com.unirutas.controllers;

import com.unirutas.models.Alerta;
import com.unirutas.models.Usuario;

import java.util.List;

public class AlertaController {
    public void notificarAlerta(Alerta alerta, List<Usuario> usuarios) {
        // Llama al método enviarAlerta de la alerta para notificar a los usuarios
        alerta.enviarAlerta(usuarios);
        System.out.println("Alerta notificada a los usuarios.");
    }

    public void listarAlertas(List<Alerta> alertas) {
        System.out.println("Lista de Alertas:");
        for (Alerta alerta : alertas) {
            System.out.println("Fecha: " + alerta.getFecha());
            System.out.println("Descripción: " + alerta.getDescripcion());
            System.out.println("Servicio: " + alerta.getServicio());
            System.out.println("------------------------");
        }
    }
}
