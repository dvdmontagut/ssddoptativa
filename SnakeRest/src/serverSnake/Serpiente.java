package serverSnake;

import java.util.*;
import utils.*;

public class Serpiente implements Cloneable 
{
	
	private String nombre;
	private LinkedList<Casilla> posiciones;
	private Direccion direccion;
	private boolean deboCrecer;
	private boolean viva;
	
	
	private Serpiente(String nombre, LinkedList<Casilla> posiciones, 
			Direccion direccion, boolean deboCrecer, boolean viva) {
		this.nombre = nombre;
		this.posiciones = posiciones;
		this.direccion = direccion;
		this.deboCrecer = deboCrecer;
		this.viva = viva;
	}//End of builder
	
	public Serpiente(String nombre, int y, int x, Direccion d) {
		
		this.nombre = nombre;
		posiciones = new LinkedList<>();
		
		//CUERPO
		if(d == Direccion.ARRIBA) 
			posiciones.addFirst(new Casilla(x, y+1));
		else 
			posiciones.addFirst(new Casilla(x, y-1));
		
		//CABEZA
		posiciones.addFirst(new Casilla(x, y));
		
		this.direccion = d;
		this.viva = true;
		this.deboCrecer = false;
		
	}//End of builder
	
	public Serpiente clone() {
		
		String nombre = this.nombre;
		Direccion direccion = this.direccion;
		boolean deboCrecer = this.deboCrecer;
		boolean viva = this.viva;
		LinkedList<Casilla> posiciones = new LinkedList<>();
		for(int i = 0; i< this.posiciones.size();i++) {
			int componenteX = this.posiciones.get(i).getEjeX();
			int componenteY = this.posiciones.get(i).getEjeY();
			Casilla casilla = new Casilla(componenteX, componenteY);
			casilla.setEjeX(componenteX);
			casilla.setEjeY(componenteY);
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
	
	public LinkedList<Casilla> getPosiciones(){
		return posiciones;
	}
	
	public Casilla cabeza() {
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
		Casilla cabeza = posiciones.peekFirst();
		Casilla nuevaCabeza = new Casilla(cabeza.getEjeX(),cabeza.getEjeY());
		
		switch (direccion) {
			case ARRIBA: nuevaCabeza.setEjeY(nuevaCabeza.getEjeY()-1);break;
			case ABAJO: nuevaCabeza.setEjeY(nuevaCabeza.getEjeY()+1);break;
			case DERECHA: nuevaCabeza.setEjeX(nuevaCabeza.getEjeX()+1);break;
			case IZQUIERDA: nuevaCabeza.setEjeX(nuevaCabeza.getEjeX()-1);break;
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
