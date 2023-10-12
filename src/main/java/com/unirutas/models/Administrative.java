package com.unirutas.models;

public class Administrativo extends Usuario {

    public Administrativo(String nombre, String codigo, String nombreUsuario, String contrasena) {
        super(nombre, codigo, nombreUsuario, contrasena);
    }

    @Override
    public void insert(User user) {
        GenericDTO<Administrative> administrativeDTO = new GenericDTO<>(Administrative.class, "Administrative", List.of("code"));
        administrativeDTO.insert((Administrative) user);
    }

    @Override
    public void update(User user) {
        GenericDTO<Administrative> administrativeDTO = new GenericDTO<>(Administrative.class, "Administrative", List.of("code"));
        administrativeDTO.update((Administrative) user);
    }
}

