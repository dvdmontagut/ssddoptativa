package serverSnake;

import java.util.*;
import utils.*;

public class Serpiente implements Cloneable 
{
	
	private String nombre;
	private LinkedList<Integer[]> posiciones;
	private Direccion direccion;
	private boolean deboCrecer;
	private boolean viva;
	
	
	private Serpiente(String nombre, LinkedList<Integer[]> posiciones, 
			Direccion direccion, boolean deboCrecer, boolean viva) {
		this.nombre = nombre;
		this.posiciones = posiciones;
		this.direccion = direccion;
		this.deboCrecer = deboCrecer;
		this.viva = viva;
	}//End of builder
	
	public Serpiente(String nombre, int y, int x, Direccion d) {
		Integer[] dummy; 
		this.nombre = nombre;
		posiciones = new LinkedList<>();
		
		//CUERPO
		if(d == Direccion.ARRIBA) {
			dummy = new Integer[2];
			dummy[Utils.EJE_Y] = y+1;
			dummy[Utils.EJE_X] = x;
			posiciones.addFirst(dummy);
		}else {
			dummy = new Integer[2];
			dummy[Utils.EJE_Y] = y-1;
			dummy[Utils.EJE_X] = x;
			posiciones.addFirst(dummy);
		}
		
		//CABEZA
		dummy = new Integer[2];
		dummy[Utils.EJE_Y] = y;
		dummy[Utils.EJE_X] = x;
		posiciones.addFirst(dummy);
		
		this.direccion = d;
		this.viva = true;
		this.deboCrecer = false;
		
	}
	
	public Serpiente clone() {
		
		String nombre = this.nombre;
		Direccion direccion = this.direccion;
		boolean deboCrecer = this.deboCrecer;
		boolean viva = this.viva;
		LinkedList<Integer[]> posiciones = new LinkedList<>();
		for(int i = 0; i< this.posiciones.size();i++) {
			int componenteX = this.posiciones.get(i)[Utils.EJE_X].intValue();
			int componenteY = this.posiciones.get(i)[Utils.EJE_Y].intValue();
			Integer [] casilla = new Integer [2];
			casilla[Utils.EJE_X] = new Integer(componenteX);
			casilla[Utils.EJE_Y] = new Integer(componenteY);
			posiciones.add(casilla);
		}//End of for
		
		return new Serpiente(nombre, posiciones, direccion, deboCrecer, viva);	
	}//End of clone
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public LinkedList<Integer[]> getPosiciones(){
		return posiciones;
	}
	
	public Integer[] cabeza() {
		return this.posiciones.peekFirst();
	}
	
	
	
	
	
	public void comer() {
		this.deboCrecer=true;
	}
	
	public boolean setDireccion(Direccion d) {
		this.direccion = d;
		return true;
	}
	
	public boolean avanzar() {
		Integer [] cabeza = posiciones.peekFirst();
		Integer [] nuevaCabeza = new Integer[2];
		nuevaCabeza[Utils.EJE_Y] = (int) cabeza[Utils.EJE_Y];
		nuevaCabeza[Utils.EJE_X] = (int) cabeza[Utils.EJE_X];
		
		switch (direccion) {
			case ARRIBA: nuevaCabeza[Utils.EJE_Y]--; break;
			case ABAJO: nuevaCabeza[Utils.EJE_Y]++; break;
			case DERECHA: nuevaCabeza[Utils.EJE_X]++; break;
			case IZQUIERDA: nuevaCabeza[Utils.EJE_X]--; break;	
		}//End of switch
		posiciones.addFirst(nuevaCabeza);
		if(this.deboCrecer)
			this.deboCrecer=false;
		else
			posiciones.removeLast();
		return true;
	}//End of avanzar
	

	public boolean isDeboCrecer() {
		return deboCrecer;
	}

	public void setDeboCrecer(boolean deboCrecer) {
		this.deboCrecer = deboCrecer;
	}

	public boolean isViva() {
		return viva;
	}

	public void matar() {
		this.viva = false;
	}//End of matar
	
	
	
}
