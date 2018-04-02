package com.mimundo.adaza.dto;

public class Operador extends Empleado {

	public String dni;
	
	public Operador(String nombre) {
		super.setNombre(nombre);
		super.setDisponible(true);
		super.setNivel(3);
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	
	
}
