package com.unirutas.models;

import java.util.Date;

public class Alert {
    private final Date date;
    private final String description;
    private final Service service_id;

    public Alert(Date date, String description, Service service) {
        this.date = date;
        this.description = description;
        this.service_id = service;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Service getService() {
        return service_id;
    }

 //  public void enviarAlerta(List<User> usuarios) {
 //     for (User usuario : usuarios) {
 //          if (usuario instanceof Student student) {
 //             if (student.estaSuscrito(service_id)) {
 //                 student.recibirAlerta(this);
 //                 System.out.println("Alerta enviada a " + student.getNombre() + ": " + this.description);
 //             }
 //        } else if (usuario instanceof Administrative) {
 //              usuario.recibirAlerta(this);
 //             System.out.println("Alerta enviada a " + usuario.getNombre() + ": " + this.description);
 //         } else {
 //              System.out.println("Error al enviar la alerta.");
 //          }
 //      }
 //  }
}
