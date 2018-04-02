package com.mimundo.adaza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mimundo.adaza.disp.Dispatcher;
import com.mimundo.adaza.dto.Director;
import com.mimundo.adaza.dto.Empleado;
import com.mimundo.adaza.dto.Operador;
import com.mimundo.adaza.dto.Supervisor;

import junit.framework.TestCase;
import junit.framework.TestResult;

public class Prueba extends TestCase {

	public static void main(String[] args) {
		List<Empleado> empleados = new ArrayList<Empleado>();
		for (int i = 0; i < 3; i++) {
			empleados.add(new Supervisor("Supervisor " + i));
		}
		for (int i = 0; i < 5; i++) {
			empleados.add(new Operador("Operador " + i));
		}
		for (int i = 0; i < 2; i++) {
			empleados.add(new Director("Director " + i));
		}
		// se ordena la lista por nivel
		Comparator<Empleado> porNivel = (o1, o2) -> o2.getNivel().compareTo(o1.getNivel());
		Collections.sort(empleados, porNivel);
		
//        while(true) {        	
        	new Dispatcher(empleados, 15);        	
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//        }
	}
}
