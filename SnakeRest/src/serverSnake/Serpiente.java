package serverSnake;

import java.util.*;
import utils.*;

public class Serpiente implements Cloneable 
{
	private Queue<Integer[]> posiciones;
	private Direccion direccion;
	private boolean deboCrecer;
	private boolean viva;
	
	public Serpiente clone() {
		try {
			return (Serpiente) super.clone();
		} catch (Exception e) {
			return null;
		}
	}
	
	public <Integer[]> posiciones(){
		return posiciones;
	}
	
	public Integer[] cabeza() {
		return this.posiciones.peek();
	}
	
	public Serpiente(int y, int x, Direccion d) {
		Integer[] dummy; 
		
		posiciones = new LinkedList<>();
		
		//CUERPO
		if(d == Direccion.ARRIBA) {
			dummy = new Integer[2];
			dummy[Utils.EJE_Y] = y+1;
			dummy[Utils.EJE_X] = x;
			posiciones.add(dummy);
		}else {
			dummy = new Integer[2];
			dummy[Utils.EJE_Y] = y-1;
			dummy[Utils.EJE_X] = x;
			posiciones.add(dummy);
		}
		
		//CABEZA
		dummy = new Integer[2];
		dummy[Utils.EJE_Y] = y;
		dummy[Utils.EJE_X] = x;
		posiciones.add(dummy);
		
		this.direccion = d;
		this.viva = true;
		this.deboCrecer = false;
		
	}
	
	public void comer() {
		this.deboCrecer=true;
	}
	
	public boolean setDireccion(Direccion d) {
		this.direccion = d;
		return true;
	}
	
	public boolean avanzar() {
		Integer[] nuevaCabeza = posiciones.peek();
		switch (direccion) {
			case ARRIBA: nuevaCabeza[Utils.EJE_Y]--; break;
			case ABAJO: nuevaCabeza[Utils.EJE_Y]++; break;
			case DERECHA: nuevaCabeza[Utils.EJE_X]++; break;
			case IZQUIERDA: nuevaCabeza[Utils.EJE_X]--; break;	
		}
		posiciones.add(nuevaCabeza);
		if(this.deboCrecer)
		{
			posiciones.remove();
			this.deboCrecer=false;
		}
		return true;
	}
	
}
