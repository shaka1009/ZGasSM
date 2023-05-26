package com.grupozeta.sm.models;

import com.grupozeta.sm.R;

import java.io.Serializable;

public class CuentaCliente implements Serializable {
    int id_cliente;
    int cuenta;
    int num_calle;
    String nombre_calle;
    String exterior1;
    String exterior2;
    String interior;
    String id_estatus;
    String nombre_cliente;
    int lectura;
    String ultima_fecha;
    int dir;
    float promedio;

    int lectura_nueva;

    public int getLectura_nueva() {
        return lectura_nueva;
    }

    public void setLectura_nueva(int lectura_nueva) {
        this.lectura_nueva = lectura_nueva;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public int getCuenta() {
        return cuenta;
    }

    public int getNum_calle() {
        return num_calle;
    }
    public String getNombre_calle() {
        return nombre_calle;
    }

    public String getExterior1() {
        return exterior1;
    }

    public String getExterior2() {
        return exterior2;
    }

    public String getInterior() {
        return interior;
    }

    public String getId_estatus() {
        return id_estatus;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public int getLectura() {
        return lectura;
    }

    public String getInteriores()
    {
        return exterior1 + " - " + exterior2 + " - " + interior;
    }

    public CuentaCliente()
    {
        this.id_cliente=0;
        this.cuenta=0;
        this.num_calle=0;
        this.nombre_calle="";
        this.exterior1="";
        this.exterior2="";
        this.interior="";
        this.id_estatus="";
        this.nombre_cliente="";
        this.lectura = 0;
        this.ultima_fecha = "";
        this.promedio = 0;
        this.lectura_nueva = -1;
    }

    public String getUltima_fecha() {
        try
        {
            String[] parts = ultima_fecha.split("-");
            String year = parts[0]; // 123
            String mes = parts[1]; // 654321
            String dia = parts[2]; // 654321
            return dia + "-" + mes + "-" + year;
        }catch (Exception e)
        {
            return ultima_fecha;
        }
    }

    public int getDir() {
        return dir;
    }

    public float getPromedio() {
        return promedio;
    }

    public CuentaCliente(String id_cliente, String cuenta, String num_calle, String nombre_calle, String exterior1, String exterior2, String interior, String id_estatus, String nombre_cliente, String lectura, String ultima_fecha, String promedio)
    {
        this.id_cliente=Integer.parseInt(id_cliente);
        this.cuenta=Integer.parseInt(cuenta);
        this.num_calle=Integer.parseInt(num_calle);
        this.nombre_calle=nombre_calle.replace('%', 'Ñ').trim();
        this.exterior1=exterior1;
        this.exterior2=exterior2;
        this.interior=interior;
        this.id_estatus=id_estatus.trim();
        this.nombre_cliente=nombre_cliente.replace('%', 'Ñ').trim();
        this.lectura = Integer.parseInt(lectura);
        this.ultima_fecha = ultima_fecha.trim();
        this.promedio = Float.valueOf(promedio);

        this.lectura_nueva = -1;

        if(this.id_estatus.equals("C"))
        {
            dir = R.drawable.cancelado;
        }
        else
        {
            dir = R.drawable.interior;
            this.ultima_fecha = "";
        }
    }
}

