package serverSnake;

import utils.Utils;

public class Casilla {
	private int ejeX;
	private int ejeY;
	
	public Casilla() {
		this.ejeX = this.ejeY = Utils.NO_VALIDO;
	}//End of builder
	
	public Casilla(int x, int y) {
		this.ejeX = x;
		this.ejeY = y;
	}//End of builder
	
	public int getEjeX() {
		return ejeX;
	}//End of getter

	public void setEjeX(int x) {
		if(x<0||x>=Utils.ALTO) this.ejeX = Utils.NO_VALIDO;
		else this.ejeX = x;
	}//End of setter

	public int getEjeY() {
		return ejeY;
	}//End of getter

	public void setEjeY(int y) {
		if(y<0 || y>=Utils.ANCHO) this.ejeY = Utils.NO_VALIDO;
		else this.ejeY = y;
	}//End of setter
	
	public boolean igualQue(Casilla c) {
		return this.ejeX == c.ejeX && this.ejeY ==c.ejeY;
	}//End of igualQue
	
	@Override
	public String toString() {
		return "["+String.valueOf(this.ejeX)+", "+String.valueOf(this.ejeY)+"]";
	}//End of toString

	
}//End of class
