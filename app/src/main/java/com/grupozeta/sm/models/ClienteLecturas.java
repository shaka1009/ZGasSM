package com.grupozeta.sm.models;

public class ClienteLecturas {
    int id_cliente;
    int lectura_nueva;

    public int getId_cliente() {
        return id_cliente;
    }

    public int getLectura_nueva() {
        return lectura_nueva;
    }

    public ClienteLecturas(int id_cliente, int lectura_nueva) {
        this.id_cliente = id_cliente;
        this.lectura_nueva = lectura_nueva;
    }
}
