package com.grupozeta.sm.models;

public class FilePorcentajesNuevos {
    int id_cliente;
    int porcentaje_nuevo;

    public FilePorcentajesNuevos(int id_cliente, int porcentaje_nuevo) {
        this.id_cliente = id_cliente;
        this.porcentaje_nuevo = porcentaje_nuevo;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getPorcentaje_nuevo() {
        return porcentaje_nuevo;
    }

    public void setPorcentaje_nuevo(int porcentaje_nuevo) {
        this.porcentaje_nuevo = porcentaje_nuevo;
    }
}
