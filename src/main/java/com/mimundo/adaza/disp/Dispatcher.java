package com.mimundo.adaza.disp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import com.mimundo.adaza.dto.Director;
import com.mimundo.adaza.dto.Empleado;
import com.mimundo.adaza.dto.Operador;
import com.mimundo.adaza.dto.Supervisor;

public class Dispatcher extends Thread {
	
	private static List<Empleado> empleados;
	private static final int min = 5000;
	private static final int max = 10000;
	private static final int limiteLlamadas = 10;
	
	public Dispatcher() {
		
	}
	
	public Dispatcher(List<Empleado> empleados, int llamadas) {
		System.out.println("Recibe " + llamadas + " llamadas");
		Dispatcher.empleados = empleados;
		// Se calcula el límite de llamadas que puede ejecutar a la vez
		do {
			int llamadasAtender = llamadas < limiteLlamadas ? llamadas : limiteLlamadas;
			ExecutorService ejes = Executors.newFixedThreadPool(llamadasAtender);
			for(int i = 0 ; i < llamadasAtender ; i++) {
				ejes.execute(new Dispatcher());
			}
			ejes.shutdown();			
			llamadas = llamadas - limiteLlamadas > 0 ? llamadas -limiteLlamadas :0;
		}while(llamadas != 0);
		
	}

	@Override
	public void run() {
		dispatchCall();
		
	}

	public void dispatchCall(){
		// define la duración de la llamada
		Empleado empleadoDisponible ;
		synchronized (this) {
			empleadoDisponible = empleadoDisponible(); 
		}
		if(empleadoDisponible != null) {
			for(Empleado empleado : empleados) {
				if(empleado.equals(empleadoDisponible)) {
					long duracionLlamada = ThreadLocalRandom.current().nextLong(min, max + 1);
					System.out.println(empleado.getNombre() + " atiende llamada de " + duracionLlamada/1000 + "s");
					try {
						sleep(duracionLlamada);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					empleado.setDisponible(true);
					System.out.println(empleado.getNombre() + " está disponible");
				}
			}
		}else {
			// Si no hay empleados disponibles, espera 1 segundo para volver a consultar
			try {
				Thread.sleep(1000);
				dispatchCall();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	private synchronized static Empleado empleadoDisponible() {
		for(Empleado empleado : empleados) {
			if(empleado instanceof Operador && empleado.isDisponible()) {
				empleado.setDisponible(false);
				return empleado;
			}
		}
		System.out.println("No hay Operador disponible");
		for(Empleado empleado : empleados) {
			if(empleado instanceof Supervisor && empleado.isDisponible()) {
				empleado.setDisponible(false);
				return empleado;
			}
		}
		System.out.println("No hay Supervisor disponible");
		for(Empleado empleado : empleados) {
			if(empleado instanceof Director && empleado.isDisponible()) {
				empleado.setDisponible(false);
				return empleado;
			}
		}
		
//		for(Empleado empleado : empleados) {
//			if(empleado.isDisponible()) {
//				empleado.setDisponible(false);
//				return empleado;
//			}
//			if(empleado instanceof Supervisor)
//				System.out.println("No hay Operador disponible");
//			if(empleado instanceof Director)
//				System.out.println("No hay Supervisor disponible");
//		}
		
		System.out.println("No hay empleados disponibles");
		return null;
	}
}
