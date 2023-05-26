package com.grupozeta.sm.models;

public class Calle {
    int num_calle;
    String nombre_calle;

    public Calle(String calle, String nombre_calle)
    {
        this.num_calle = Integer.parseInt(calle);
        this.nombre_calle = nombre_calle.replace('%', 'Ñ');
    }

    public String getNumCalle()
    {
        return String.valueOf(num_calle);
    }

    public String getNombreCalle()
    {
        return nombre_calle;
    }
}
