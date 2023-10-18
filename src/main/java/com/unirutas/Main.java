package com.unirutas;

import com.unirutas.controllers.BusController;
import com.unirutas.controllers.JourneyController;
import com.unirutas.controllers.ServiceController;
import com.unirutas.controllers.UserController;
import com.unirutas.core.database.connection.interfaces.IConnectionPool;
import com.unirutas.core.providers.ConnectionPoolFactoryProvider;
import com.unirutas.models.*;
import com.unirutas.services.implementation.AdministrativeServices;
import com.unirutas.services.implementation.StudentServices;
import com.unirutas.services.interfaces.UserServices;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        IConnectionPool<?> connectionPool = ConnectionPoolFactoryProvider.getFactory().createConnectionPool();

        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.ALL); // e.g. or Log.WARNING, etc.


        // Crear controladores
        UserServices<Student> studentServices = new StudentServices();
        UserServices<Administrative> administrativeServices = new AdministrativeServices();

        UserController studentController = new UserController(studentServices);
        UserController adminController = new UserController(administrativeServices);

        BusController busController = new BusController();
        JourneyController journeyController = new JourneyController();

        // Crear journey y stops
        Coordinate coordinate1 = new Coordinate(1.0, 2.0);
        Coordinate coordinate2 = new Coordinate(3.0, 4.0);

        Stop stop1 = new Stop("Paradero1", "Descripción del paradero 1", coordinate1);
        Stop stop2 = new Stop("Paradero2", "Descripción del paradero 2", coordinate2);

        List<Stop> stops = new ArrayList<>(Arrays.asList(stop1, stop2));
        Journey journey = new Journey(stops, new ArrayList<>());

        // Crear servicio y suscribir estudiante
        Route route = new Route("Ruta1", journey);
        Direction direction = new Direction("Sentido1", journey);
        ServiceController serviceController = new ServiceController(route, direction);

        // Agregar horarios y buses al servicio
        Schedule schedule1 = new Schedule(LocalTime.of(8, 0));
        Schedule schedule2 = new Schedule(LocalTime.of(10, 0));

        Bus bus1 = new Bus(30, "AB123");
        Bus bus2 = new Bus(25, "CD456");

        serviceController.assignSchedule(schedule1);
        serviceController.assignSchedule(schedule2);

        serviceController.assignBus(bus1);
        serviceController.assignBus(bus2);

        // Asignar conductor a un bus
        Driver driver1 = new Driver("Conductor1", "C001");
        bus1.assignDriver(driver1);

        // Obtener información de un bus
        busController.getBusInfo(bus1);

        // Crear un estudiante
        Student student = new Student("Pablo Bobadillo", "160004331", "psbobadilla", "123");
        studentController.createUser(student);

        // Crear un administrativo
        Administrative administrative = new Administrative("Kevin Martinez", "160004314", "ksmartinez", "321");
        adminController.createUser(administrative);

        // Actualizar un estudiante (por ejemplo, cambiar su contraseña)
        student.changePassword("555");
        studentController.updateUser(student);

        // Transacción incorrecta
        student.changePassword(null);
        studentController.updateUser(student);

        // Actualizar un administrativo (por ejemplo, cambiar su nombre de usuario)
        administrative.changePassword("123");
        adminController.updateUser(administrative);

        // Eliminar un estudiante
        // userController.eliminarUsuario(estudiante);

        // Eliminar un administrativo
        // userController.eliminarUsuario(administrativo);

        connectionPool.closeAllConnections();
    }
}
