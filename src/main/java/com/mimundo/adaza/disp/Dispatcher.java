package com.mimundo.adaza.disp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import com.mimundo.adaza.dto.Director;
import com.mimundo.adaza.dto.Empleado;
import com.mimundo.adaza.dto.Supervisor;

/**
 * Maneja las llamadas y estados de los empleados
 * @author dazaac
 *
 */
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
			while(!ejes.isTerminated()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}while(llamadas != 0);
		
	}

	@Override
	public void run() {
		dispatchCall();
		
	}

	/**
	 * Ejecuta el proceso de atención de llamadas
	 */
	public void dispatchCall(){
		// define la duración de la llamada
		Empleado empleadoDisponible ;
		// obtiene el empleado disponible
		synchronized (this) {
			empleadoDisponible = empleadoDisponible(); 
		}
		if(empleadoDisponible != null) {
			for(Empleado empleado : empleados) {
				// obtiene el empleado de la lista
				if(empleado.equals(empleadoDisponible)) {
					long duracionLlamada = ThreadLocalRandom.current().nextLong(min, max + 1);
					System.out.println(empleado.getNombre() + " atiende llamada de " + duracionLlamada/1000 + "s");
					try {
						sleep(duracionLlamada);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// habilita el empleado para recibir una llamada
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
	
	/**
	 * Método que permite obtener el empleado disponible
	 * @return
	 */
	private synchronized static Empleado empleadoDisponible() {
		for(Empleado empleado : empleados) {
			if(empleado.isDisponible()) {
				System.out.println(empleado.getNombre() +  " atenderá la llamada ");
				empleado.setDisponible(false);
				return empleado;
			}
			if(empleado instanceof Supervisor)
				System.out.println("No hay Operador disponible");
			if(empleado instanceof Director)
				System.out.println("No hay Supervisor disponible");
		}
		
		System.out.println("No hay empleados disponibles");
		return null;
	}
}
