package com.grupozeta.sm.models;

public class Tanque {
	int id_tanque;
	String descripcion;
	int capacidad;
	int numero_sap;
	
	int porcentaje_actual;
	
	public Tanque() {
		this.id_tanque = -1;
		this.descripcion = "";
		this.capacidad = -1;
		this.numero_sap = -1;
	}
	


	public Tanque(String id_tanque, String descripcion, String capacidad, String numero_sap) {
		this.id_tanque = Integer.parseInt(id_tanque);
		this.descripcion = descripcion;
		this.capacidad = Integer.parseInt(capacidad);
		this.numero_sap = Integer.parseInt(numero_sap);
	}

	public Tanque(String id_tanque, String descripcion, String capacidad, String numero_sap, String porcentaje) {
		this.id_tanque = Integer.parseInt(id_tanque);
		this.descripcion = descripcion;
		this.capacidad = Integer.parseInt(capacidad);
		this.numero_sap = Integer.parseInt(numero_sap);
		this.porcentaje_actual = Integer.parseInt(porcentaje);
	}

	public int getPorcentaje_actual() {
		return porcentaje_actual;
	}
	public void setPorcentaje_actual(int porcentaje_actual) {
		this.porcentaje_actual = porcentaje_actual;
	}
	public int getId_tanque() {
		return id_tanque;
	}
	public void setId_tanque(int id_tanque) {
		this.id_tanque = id_tanque;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public int getNumero_sap() {
		return numero_sap;
	}
	public void setNumero_sap(int numero_sap) {
		this.numero_sap = numero_sap;
	}
}
