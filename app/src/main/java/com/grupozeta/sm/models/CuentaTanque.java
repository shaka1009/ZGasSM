package com.grupozeta.sm.models;

import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;

public class CuentaTanque {
	int id_cuenta;
	int cuenta;
	String nombre_calle;
	String nombre_colonia;
	int codigo_postal;
	String exterior1;
	String exterior2;
	String interior; 
	String descripcion;

	ArrayList <Calle> calles;

	
	public String getIdCuenta()
	{
		return String.valueOf(id_cuenta);
	}
	
	public CuentaTanque()
	{
		id_cuenta=0;
		cuenta=0;
		nombre_calle="";
		nombre_colonia="";
		codigo_postal=0;
		exterior1="";
		exterior2="";
		interior=""; 
		descripcion="";
		calles = new ArrayList<Calle>();
	}

	public int getId_cuenta() {
		return id_cuenta;
	}

	public String getCuenta() {
		return Integer.toString(cuenta);
	}

	public String getNombre_calle() {
		return nombre_calle.replace('%', 'Ñ');
	}

	public String getNombre_colonia() {
		return nombre_colonia.replace('%', 'Ñ');
	}

	public String getCodigo_postal() {
		return Integer.toString(codigo_postal);
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

	public String getDescripcion() {
		return descripcion.replace('%', 'Ñ');
	}

	public List<Calle> getCalles() {
		return calles;
	}

	public CuentaTanque(String id_cuenta, String cuenta, String nombre_calle, String nombre_colonia, String codigo_postal, String exterior1, String exterior2, String interior, String descripcion)
	{
		this.id_cuenta=Integer.parseInt(id_cuenta.trim());
		this.cuenta=Integer.parseInt(cuenta.trim());
		this.nombre_calle=nombre_calle.trim();
		this.nombre_colonia=nombre_colonia.trim();
		this.codigo_postal=Integer.parseInt(codigo_postal.trim());
		this.exterior1=exterior1.trim();
		this.exterior2=exterior2.trim();
		this.interior=interior.trim(); 
		this.descripcion=descripcion.trim();
		calles = new ArrayList<Calle>();

	}
	
	public void setCalles(ArrayList <Calle> calles)
	{
		this.calles = calles;
	}


	public void addCalle(Calle calle)
	{
		calles.add(calle);
	}


}
