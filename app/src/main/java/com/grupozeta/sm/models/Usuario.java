package com.grupozeta.sm.models;


import org.apache.commons.text.WordUtils;

public class Usuario {
    public int getId_usuario() {
        return id_usuario;
    }

    public int getId_estatususr() {
        return id_estatususr;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAppaterno() {
        return appaterno;
    }

    public String getApmaterno() {
        return apmaterno;
    }

    public int getNomina() {
        return nomina;
    }

    public int getId_rol() {
        return id_rol;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    int id_usuario;
    int id_estatususr;
    String nombre;
    String appaterno;
    String apmaterno;
    int nomina;
    int id_rol;
    String login;
    String password;



    public Usuario(String id_usuario, String login, String password, String id_estatususr, String nombre, String appaterno, String apmaterno, String nomina, String id_rol) {
        this.id_usuario = Integer.parseInt(id_usuario);
        this.login = login;
        this.password = password;
        this.id_estatususr = Integer.parseInt(id_estatususr);
        this.nombre = WordUtils.capitalize(nombre);
        this.appaterno = WordUtils.capitalize(appaterno);
        this.apmaterno = WordUtils.capitalize(apmaterno);
        this.nomina = Integer.parseInt(nomina);
        this.id_rol = Integer.parseInt(id_rol);
    }

    public String getString()
    {
       return id_usuario + "\n" +
                login + "\n" +
                password + "\n" +
                id_estatususr + "\n" +
                nombre + "\n" +
                appaterno + "\n" +
                apmaterno + "\n" +
                nomina + "\n" +
                id_rol + "\n";
    }

    public void setId_rol(String id_rol) {
        this.id_rol = Integer.parseInt(id_rol);
    }

    public String getNombreCompleto()
    {
        String nombre = this.nombre + " " + appaterno + " " + apmaterno;
        nombre = nombre.toLowerCase();
        nombre = WordUtils.capitalize(nombre);
        return nombre;
    }
}
