package com.unirutas;

import com.unirutas.controllers.*;
import com.unirutas.core.builder.query.interfaces.ICustomQueryBuilder;
import com.unirutas.core.builder.query.types.Tuple;
import com.unirutas.core.database.connection.interfaces.IConnectionPool;
import com.unirutas.core.database.manager.interfaces.IDatabaseManager;
import com.unirutas.core.dependency.injector.implementation.DependencyInjector;
import com.unirutas.core.dependency.injector.interfaces.IDependencyInjector;
import com.unirutas.core.providers.ConnectionPoolFactoryProvider;
import com.unirutas.core.providers.CustomQueryBuilderProvider;
import com.unirutas.core.providers.DatabaseManagerFactoryProvider;
import com.unirutas.helpers.notification.implementation.AdministrativeAlertNotification;
import com.unirutas.helpers.notification.implementation.StudentAlertNotification;
import com.unirutas.helpers.notification.template.AlertNotification;
import com.unirutas.models.*;
import com.unirutas.services.implementation.AdministrativeServices;
import com.unirutas.services.implementation.StudentServices;
import com.unirutas.services.interfaces.UserServices;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        IDependencyInjector dependencyInjector = new DependencyInjector();

        IConnectionPool<?> connectionPool = ConnectionPoolFactoryProvider.getFactory().createConnectionPool();

        IDatabaseManager databaseManager = DatabaseManagerFactoryProvider.getFactory().createDatabaseManager();

        databaseManager.getDate();
        databaseManager.getHour();

        // Crear controladores
        UserServices<Student> studentServices = new StudentServices();
        UserServices<Administrative> administrativeServices = new AdministrativeServices();

        dependencyInjector.injectDependencies(studentServices);
        dependencyInjector.injectDependencies(administrativeServices);

        UserController studentController = new UserController(studentServices);
        UserController adminController = new UserController(administrativeServices);

        BusController busController = new BusController();

        JourneyController journeyController = new JourneyController();
        dependencyInjector.injectDependencies(journeyController);

        AlertController alertController = new AlertController();
        dependencyInjector.injectDependencies(alertController);

        StudentAlertController studentAlertController = new StudentAlertController();
        dependencyInjector.injectDependencies(studentAlertController);

        // Crear journey y stops
        Coordinate coordinate1 = new Coordinate(1.0, 2.0);
        Coordinate coordinate2 = new Coordinate(3.0, 4.0);

        Stop stop1 = new Stop("Paradero1", "Descripción del paradero 1", coordinate1.getId());
        Stop stop2 = new Stop("Paradero2", "Descripción del paradero 2", coordinate2.getId());

        List<Stop> stops = new ArrayList<>(Arrays.asList(stop1, stop2));
        Direction direction = new Direction("Sentido1");

        DirectionController directionController = new DirectionController();
        dependencyInjector.injectDependencies(directionController);

        directionController.createDirection(direction);

        Journey journey = new Journey(direction.getId());
        journeyController.createJourney(journey);

        // Crear servicio y suscribir estudiante
        Route route = new Route("Ruta1", journey.getId());
        ServiceController serviceController = new ServiceController(route);

        dependencyInjector.injectDependencies(serviceController);

        RouteController routeController = new RouteController();
        dependencyInjector.injectDependencies(routeController);

        routeController.createRoute(route);

        // Crear Servicio
        Service service = new Service(route.getId());
        serviceController.addService(service);

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
        bus1.addDriver(driver1);

        // Obtener información de un bus
        busController.getBusInfo(bus1);

        // Crear un estudiante
        Student student = new Student("Pablo Bobadillo", "160004331", "psbobadilla", "123");
        studentController.createUser(student);

        // Crear un administrativo
        Administrative administrative = new Administrative("Kevin Martinez", "160004321", "ksmartinez", "321");
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

        // Print all users
        List<User> users = studentController.findAllUsers();
        users.addAll(adminController.findAllUsers());

        System.out.println("Usuarios:");
        for (User user : users) {
            System.out.println(user.getName() + " " + user.getCode() + " " + user.getUsername());
        }

        Student student2 = new Student("Mateo Granada", "160004314", "mgranada", "123");
        studentController.createUser(student2);

        // Crear alerta

        Alert alert = new Alert(new java.sql.Date(new Date().getTime()), "Alerta de prueba", "image", service.getId());

        alertController.createAlert(alert);

        // Crear alerta para estudiante
        StudentAlert studentAlert = new StudentAlert(true, student2.getCode(), alert.getId());

        studentAlertController.create(studentAlert);

        // Custom query builder example (select all alerts from a student) using the query builder to perform an inner join.
        // Also, the query builder is used to perform a join with the Alert and Service tables to get the description, date and route id.
        ICustomQueryBuilder queryBuilder = CustomQueryBuilderProvider.getFactory().createCustomQueryBuilder(Student.class);
        List<List<Tuple<String, Object>>> results = queryBuilder.select()
                .fields("name", "code", "username")
                .join("code", StudentAlert.class, "student_code")
                .join(StudentAlert.class, "alert_id", Alert.class, "id")
                .join(Alert.class, "service_id", Service.class, "id")
                .joinFields("date", "description", "is_read", "alert_id", "service_id", "route_id")
                .where("code", "160004314")
                .and("username", "mgranada")
                .execute();

        System.out.println("Alertas del estudiante con código 160004314 y nombre de usuario mgranada:");
        int i = 1;
        for (List<Tuple<String, Object>> result : results) {
            System.out.println(i++);
            for (Tuple<String, Object> tuple : result) {
                if (tuple.getKey().equals("Alert")){
                    Alert alert1 = (Alert) tuple.getValue();
                    System.out.println("Descripcion: " + alert1.getDescription() + ", Fecha: " + alert1.getDate());
                } else if (tuple.getKey().equals("StudentAlert")){
                    StudentAlert studentAlert1 = (StudentAlert) tuple.getValue();
                    System.out.println("Alerta: " + studentAlert1.getAlertId());
                    System.out.println("Leida: " + studentAlert1.isRead());
                } else if (tuple.getKey().equals("Service")){
                    Service service1 = (Service) tuple.getValue();
                    System.out.println("Servicio: " + service1.getId());
                    System.out.println("Ruta: " + service1.getRouteId());
                }
            }
            System.out.println();
        }

        // Template pattern usage example (sending notifications to students and administrative users) using the AlertNotification class.
        // The AlertNotification class is an abstract class that defines the template for sending notifications to users.
        // The StudentAlertNotification and AdministrativeAlertNotification classes are concrete implementations of the AlertNotification class.
        // The StudentAlertNotification class validates if a student is subscribed to a service and sends a notification to the student.
        // The AdministrativeAlertNotification class sends a notification to the administrative user without validating anything.

        // Create a student subscription to a service.
        StudentSubscription studentSubscription = new StudentSubscription(student2.getCode(), service.getId());

        StudentSubscriptionController studentSubscriptionController = new StudentSubscriptionController();
        dependencyInjector.injectDependencies(studentSubscriptionController);

        studentSubscriptionController.create(studentSubscription);

        // Create another administrative to send the notification.
        Administrative administrative2 = new Administrative("Administrative 2", "160004322", "administrative2", "123");

        AlertNotification studentAlertNotification = new StudentAlertNotification();
        dependencyInjector.injectDependencies(studentAlertNotification);

        AlertNotification administrativeAlertNotification = new AdministrativeAlertNotification();

        // Send notifications to students and administrative users.
        studentAlertNotification.notifyUsers(Arrays.asList(student, student2), alert);
        administrativeAlertNotification.notifyUsers(Arrays.asList(administrative, administrative2), alert);

        connectionPool.closeAllConnections();
    }
}
