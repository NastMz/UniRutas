package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

import java.util.Date;
import java.util.UUID;

@Table(name="Alert")
public class Alert {
    @PrimaryKey(name = "id")
    private String id;
    @Column(name = "date")
    private final Date date;
    @Column(name = "description")
    private final String description;
    @Column(name = "image")
    private final String image;
    @Column(name = "service_id")
    private final String serviceId;

    public Alert(Date date, String description, String image, String serviceId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.date = date;
        this.description = description;
        this.image = image;
        this.serviceId = serviceId;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getService() {
        return serviceId;
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
