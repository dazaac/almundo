package com.mimundo.adaza.dto;

public class Supervisor extends Empleado {


	public Supervisor(String nombre) {
		super.setNombre(nombre);
		super.setDisponible(true);
		super.setNivel(2);
	}
	
}
