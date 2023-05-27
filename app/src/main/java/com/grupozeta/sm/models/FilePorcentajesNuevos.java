package com.grupozeta.sm.models;

public class FilePorcentajesNuevos {
    int id_tanque;
    int porcentaje_nuevo;


    public int getId_tanque() {
        return id_tanque;
    }

    public void setId_tanque(int id_tanque) {
        this.id_tanque = id_tanque;
    }

    public FilePorcentajesNuevos(int id_tanque, int porcentaje_nuevo) {
        this.id_tanque = id_tanque;
        this.porcentaje_nuevo = porcentaje_nuevo;
    }


    public int getPorcentaje_nuevo() {
        return porcentaje_nuevo;
    }

    public void setPorcentaje_nuevo(int porcentaje_nuevo) {
        this.porcentaje_nuevo = porcentaje_nuevo;
    }
}
