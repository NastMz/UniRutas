package com.unirutas;

import com.unirutas.controllers.BusController;
import com.unirutas.controllers.RecorridoController;
import com.unirutas.controllers.ServicioController;
import com.unirutas.controllers.UsuarioController;
import com.unirutas.models.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Crear controladores
        UsuarioController usuarioController = new UsuarioController();
        BusController busController = new BusController();
        RecorridoController recorridoController = new RecorridoController();

        // Crear recorrido y paraderos
        Coordenada coordenada1 = new Coordenada(1.0, 2.0);
        Coordenada coordenada2 = new Coordenada(3.0, 4.0);

        Paradero paradero1 = new Paradero("Paradero1", "Descripción del paradero 1", coordenada1);
        Paradero paradero2 = new Paradero("Paradero2", "Descripción del paradero 2", coordenada2);

        List<Paradero> paraderos = new ArrayList<>(Arrays.asList(paradero1, paradero2));
        Recorrido recorrido = new Recorrido(paraderos, new ArrayList<>());

        // Crear servicio y suscribir estudiante
        Ruta ruta = new Ruta("Ruta1", recorrido);
        Sentido sentido = new Sentido("Sentido1", recorrido);
        ServicioController servicioController = new ServicioController(ruta, sentido);

        // Agregar horarios y buses al servicio
        Horario horario1 = new Horario(LocalTime.of(8, 0));
        Horario horario2 = new Horario(LocalTime.of(10, 0));

        Bus bus1 = new Bus(30, "AB123");
        Bus bus2 = new Bus(25, "CD456");

        servicioController.asignarHorario(horario1);
        servicioController.asignarHorario(horario2);

        servicioController.asignarBus(bus1);
        servicioController.asignarBus(bus2);

        // Asignar conductor a un bus
        Conductor conductor1 = new Conductor("Conductor1", "C001");
        bus1.asignarConductor(conductor1);

        // Obtener información de un bus
        busController.getBusInfo(bus1);

        // Crear un estudiante
        Estudiante estudiante = new Estudiante("Pablo Bobadillo", "160004331", "psbobadilla", "123");
        usuarioController.crearEstudiante(estudiante);

        // Crear un administrativo
        Administrativo administrativo = new Administrativo("Kevin Martinez", "160004314", "ksmartinez", "321");
        usuarioController.crearAdministrativo(administrativo);

        // Actualizar un estudiante (por ejemplo, cambiar su contraseña)
        estudiante.cambiarContrasena("555");
        usuarioController.actualizarEstudiante(estudiante);

        // Transacción incorrecta
        estudiante.cambiarContrasena(null);
        usuarioController.actualizarEstudiante(estudiante);

        // Actualizar un administrativo (por ejemplo, cambiar su nombre de usuario)
        administrativo.cambiarContrasena("999");
        usuarioController.actualizarAdministrativo(administrativo);

        // Eliminar un estudiante
        // usuarioController.eliminarUsuario(estudiante);

        // Eliminar un administrativo
        // usuarioController.eliminarUsuario(administrativo);

    }
}
