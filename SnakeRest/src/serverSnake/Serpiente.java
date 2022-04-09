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
	
	
	
	public Serpiente clone() {
		try {
			return (Serpiente) super.clone();
		} catch (Exception e) {
			return null;
		}
	}
	
	
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
		posiciones.addFirst(nuevaCabeza);
		if(this.deboCrecer)
		{
			posiciones.removeLast();
			this.deboCrecer=false;
		}
		return true;
	}

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
