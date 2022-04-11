package serverSnake;

import utils.Utils;

/**
 * Representa una casilla del tablero
 * @author David Montagut Pamo, Sergio Rollán Moralejo, Aníbal Vaquero Blanco.
 *
 */
public class Casilla {
	private int ejeX;
	private int ejeY;
	
	/**
	 * constructor vacio, pone las coordenadas de las casillas a NO_VALIDO
	 */
	public Casilla() {
		this.ejeX = this.ejeY = Utils.NO_VALIDO;
	}//End of builder
	
	/**
	 * constructor para asignar los valores de las coordenadas de una casilla
	 * @param x
	 * @param y
	 */
	public Casilla(int x, int y) {
		this.ejeX = x;
		this.ejeY = y;
	}//End of builder
	
	public int getEjeX() {
		return ejeX;
	}//End of getter

	/**
	 * Setter pero en caso de no poner una coordenada valida, se pone en NO_VALIDO
	 * @param x
	 */
	public void setEjeX(int x) {
		if(x<0||x>=Utils.ANCHO) this.ejeX = Utils.NO_VALIDO;
		else this.ejeX = x;
	}//End of setter

	public int getEjeY() {
		return ejeY;
	}//End of getter

	/**
	 * Setter pero en caso de no poner una coordenada valida, se pone en NO_VALIDO
	 * @param y
	 */
	public void setEjeY(int y) {
		if(y<0 || y>=Utils.ALTO) this.ejeY = Utils.NO_VALIDO;
		else this.ejeY = y;
	}//End of setter
	
	//NO QUERIAMOS HACER OVERRIDE
	/**
	 * Se usa como comparador, compara las coordenadas de la casilla actual con la pasada como parametro.
	 * @param c
	 * @return true mismas coordenadas, false distintas coordenadas
	 */
	public boolean igualQue(Casilla c) {
		return this.ejeX == c.ejeX && this.ejeY ==c.ejeY;
	}//End of igualQue
	
	@Override
	public String toString() {
		return "["+String.valueOf(this.ejeX)+", "+String.valueOf(this.ejeY)+"]";
	}//End of toString

	
}//End of class
