package serverSnake;

import java.util.*;
import utils.*;

/**
 * 
 * Representa una serpiente del juego SNAKE
 * @author David Montagut Pamo, Sergio Rollan Moralejo, Anibal Vaquero Blanco.
 *
 */
public class Serpiente 
{
	
	private String nombre;
	/**
	 * Usamos una LinkedList a modo de cola porque es la estructura de datos
	 * que mejor se asemeja al funcionamiento del Snake ya que nos permite
	 * a�adir por el principio y eliminar por el final para asi recrear el movimiento
	 * de la serpiente.
	 */
	private LinkedList<Casilla> posiciones;
	private Direccion direccion;
	private Direccion ultimaDireccion;
	private boolean deboCrecer;
	private boolean viva;
	
	/**
	 * Constructor usado en el metodo de clonacion necesita de todos los atributos
	 * @param nombre
	 * @param posiciones
	 * @param direccion
	 * @param deboCrecer
	 * @param viva
	 */
	private Serpiente(String nombre, LinkedList<Casilla> posiciones, 
			Direccion direccion, boolean deboCrecer, boolean viva) {
		this.nombre = nombre;
		this.posiciones = posiciones;
		this.direccion = direccion;
		this.ultimaDireccion=direccion;
		this.deboCrecer = deboCrecer;
		this.viva = viva;
	}//End of builder
	
	/**
	 * Constructor para crear una serpiente "recien nacida"
	 * @param nombre Letra que definiran el dibujo de la serpiente 
	 * @param x Coordenada x de la cabeza
	 * @param y Coordenada y de la cabeza
	 * @param d Direccion inicial, el cuerpo se situara al contrario que esta direccion
	 */
	public Serpiente(String nombre, int x, int y, Direccion d) {
		
		this.nombre = nombre;
		posiciones = new LinkedList<Casilla>();
		
		//CUERPO
		switch(d) {
			case ARRIBA: posiciones.addFirst(new Casilla(x, y+1)); break;
			case ABAJO: posiciones.addFirst(new Casilla(x, y-1)); break;
			case DERECHA: posiciones.addFirst(new Casilla(x+1, y)); break;
			case IZQUIERDA: posiciones.addFirst(new Casilla(x-1, y)); break;
		}//End of switch
		
		//CABEZA
		posiciones.addFirst(new Casilla(x, y));
		
		this.direccion = d;
		this.viva = true;
		this.deboCrecer = false;
		
	}//End of builder
	
	/**
	 * Clona un objeto serpiente pero sin copiar las direcciones de memoria
	 * @return devuelve un objeto serpiente clonada de la serpiente que invoque el metodo
	 */
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
			posiciones.add(casilla);
		}//End of for
		
		return new Serpiente(nombre, posiciones, direccion, deboCrecer, viva);	
	}//End of clone
	
	
	public String getNombre() {
		return nombre;
	}
	
	
	public LinkedList<Casilla> getPosiciones(){
		return posiciones;
	}
	
	/**
	 * Devuelve la Casilla donde se situa la cabeza de la serpiente que invoca el metodo
	 * @return Objeto Casilla
	 */
	public Casilla cabeza() {
		return this.posiciones.peekFirst();
	}
	
	
	
	
	/**
	 * Metodo para indicar que la serpiente ha crecido y en el siguiente turno logico debe aumentar su tama�o
	 */
	public void comer() {
		this.deboCrecer=true;
	}
	
	/**
	 * Metodo que cambia la direccion de la serpiente siempre que no sea un giro de 180 grados
	 * @param d
	 */
	public void setDireccion(Direccion d) 
	{
		if(d==Direccion.ABAJO && this.ultimaDireccion!=Direccion.ARRIBA) this.direccion = d;
		if(d==Direccion.ARRIBA && this.ultimaDireccion!=Direccion.ABAJO) this.direccion = d;
		if(d==Direccion.IZQUIERDA && this.ultimaDireccion!=Direccion.DERECHA) this.direccion = d;
		if(d==Direccion.DERECHA && this.ultimaDireccion!=Direccion.IZQUIERDA) this.direccion = d;
	}

	/**
	 * Equivale a isViva()
	 */
	public boolean estaViva() {
		return viva;
	}

	/**
	 * Establece el estado de la serpiente a muerta
	 */
	public void matar() {
		this.viva = false;
	}//End of matar
	
	
	
	/**
	 * Metodo que permite avanzar la serpiente DE FORMA EFICIENTE YA QUE USAMOS UNA LINKED LIST una casilla y en
	 * caso de que deba crecer, le aumenta una casilla
	 */
	public void avanzar() {
		Casilla cabeza = posiciones.peekFirst(); //Cogemos la cabeza
		Casilla nuevaCabeza = new Casilla(cabeza.getEjeX(),cabeza.getEjeY()); //Clonamos los valores de la cabeza
		
		//le damos la siguiente posicion
		switch (direccion) {
			case ARRIBA: 
				nuevaCabeza.setEjeY(nuevaCabeza.getEjeY()-1); 
				this.ultimaDireccion=Direccion.ARRIBA; 
				break;
			case ABAJO: 
				nuevaCabeza.setEjeY(nuevaCabeza.getEjeY()+1); 
				this.ultimaDireccion=Direccion.ABAJO; 
				break;
			case DERECHA: 
				nuevaCabeza.setEjeX(nuevaCabeza.getEjeX()+1); 
				this.ultimaDireccion=Direccion.DERECHA; 
				break;
			case IZQUIERDA: 
				nuevaCabeza.setEjeX(nuevaCabeza.getEjeX()-1); 
				this.ultimaDireccion=Direccion.IZQUIERDA; 
				break;
		}//End of switch
		posiciones.addFirst(nuevaCabeza);//addherimos como cabeza la nueva coordenada
		if(this.deboCrecer)
			this.deboCrecer=false;
		else
			posiciones.removeLast(); //en caso de no crecer removemos la ultima
		
	}//End of avanzar
	
	
	
}
