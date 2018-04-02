package com.mimundo.adaza.dto;

public class Director extends Empleado{

	public String telefono;
	
	public Director(String nombre) {
		super.setNombre(nombre);
		super.setDisponible(true);
		super.setNivel(1);
	}
	
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	
}
