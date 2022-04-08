package serverSnake;

import java.util.*;

import utils.*;

public class Tablero 
{
	private String[][] casillas;
	private List<Serpiente> serpientes;
	private List<Serpiente> serpientesFicticias;
	private Integer[] comida;
	public Tablero()
	{
		comida = new Integer[2];
		casillas = new String[Utils.ALTO][Utils.ANCHO];
		serpientes = new ArrayList<>();
		serpientesFicticias = new ArrayList<>();
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(String[] f: casillas) {
			for(String c:f) sb.append(c).append(Utils.SEPARADOR1);
			sb.append(Utils.SEPARADOR2);
		}//end of for
		return sb.toString();
	}
	
	public void construir(int nj, List<String> nombres)
	{
		for(int f=0;f<Utils.ALTO;f++) 
			for(int c=0;c<Utils.ANCHO;c++)
				casillas[f][c] = Utils.CASILLA_VACIA;
		
		int fila = this.generarFila();
		int columna = this.generarColumna();
		
		casillas[fila][columna] = nombres.get(0).toUpperCase();
		if(fila>Utils.ALTO/2){	
			casillas[fila+1][columna] = nombres.get(0);
			serpientes.add(new Serpiente(fila,columna,Direccion.ARRIBA));
		}
		else {
			casillas[fila-1][columna] = nombres.get(0);
			serpientes.add(new Serpiente(fila,columna,Direccion.ABAJO));
		}
		
		
		
		//SEUDO BACKTRACK TAMBIEN LLAMADO ENSAYO-ERROR
		//jugadores
		for(int i=1; i<nj; i++) {
			while(true) {
				fila = generarFila();
				columna = generarColumna();
				if(comprobarCabeza(fila, columna)) break;
			}//end of while
			casillas[fila][columna] = nombres.get(i).toUpperCase();
			if(fila>Utils.ALTO/2)
				casillas[fila+1][columna] = nombres.get(i);
			else 
				casillas[fila-1][columna] = nombres.get(i);		
		}//end of for
		//comida
		comida = this.generarComida();
		casillas[comida[Utils.EJE_Y]][Utils.EJE_X] = Utils.CASILLA_COMIDA;
		
		this.comida[Utils.EJE_Y] = fila;
		this.comida[Utils.EJE_X] = columna;
		//END OF SEUDO BACKTRACK TAMBIEN LLAMADO ENSAYO-ERROR		
	}
	
	private int generarColumna()
	{
		int columna;
		Random r = new Random();
		columna = r.nextInt(Utils.ANCHO);
		columna=(columna==Utils.ANCHO-1)?columna-1:(columna==0)?1:columna; //EVITAR LOS BORDES IZQUIERDO Y DERECHO
		return columna;
	}//end of generarColumna

	private int generarFila() 
	{
		int fila;
		Random r = new Random();
		fila = r.nextInt(Utils.ALTO);
		fila=(fila==Utils.ALTO-1)?fila-1:(fila==0)?1:fila; //EVITAR LOS BORDES SUPERIOR E INFERIOR
		return fila;
	}//end of generarFila


	private boolean comprobarCabeza(int fila,int columna) 
	{
		List<Integer[]> posicionesCerca = new ArrayList<>();
		
		Integer[] dummy = new Integer[2];
		dummy[0] =fila;
		dummy[1]=columna;
		posicionesCerca.add(dummy); //CENTRO
		dummy = new Integer[2];
		dummy[0] = fila-1;
		dummy[1]=columna;
		posicionesCerca.add(dummy); //ARRIBA
		dummy = new Integer[2];
		dummy[0] = fila-1;
		dummy[1]=columna-1;
		posicionesCerca.add(dummy); //ARRIBA IZQ
		dummy = new Integer[2];
		dummy[0] = fila-1;
		dummy[1]=columna+1;
		posicionesCerca.add(dummy); //ARRIBA DER
		
		dummy = new Integer[2];
		dummy[0] = fila;
		dummy[1]=columna-1;
		posicionesCerca.add(dummy); //IZQ
		dummy = new Integer[2];
		dummy[0] = fila;
		dummy[1]=columna+1;
		posicionesCerca.add(dummy); //DER
		
		dummy = new Integer[2];
		dummy[0] = fila+1;
		dummy[1]=columna;
		posicionesCerca.add(dummy); //ABAJO
		dummy = new Integer[2];
		dummy[0] = fila+1;
		dummy[1]=columna-1;
		posicionesCerca.add(dummy); //ABAJO IZQ
		dummy = new Integer[2];
		dummy[0] = fila+1;
		dummy[1]=columna+1;
		posicionesCerca.add(dummy); //ABAJO DER	
		
		dummy = new Integer[2];
		dummy[0] = (fila-2<0)?fila:fila-2;
		dummy[1]=columna;
		posicionesCerca.add(dummy); //ARRIBA +2
		
		dummy = new Integer[2];
		dummy[0] = fila;
		dummy[1]=(columna-2<0)?columna:columna-2;
		posicionesCerca.add(dummy); //IZQ +2
		
		dummy = new Integer[2];
		dummy[0] = fila;
		dummy[1]=(columna+2>Utils.ANCHO-1)?columna:columna+2;
		posicionesCerca.add(dummy); //DER +2
		
		dummy = new Integer[2];
		dummy[0]=(fila+2>Utils.ALTO-1)?fila:fila+2;
		dummy[1]=columna;
		posicionesCerca.add(dummy); //ABAJO +2
		
		
		for(Integer[] i : posicionesCerca) 
				if(this.casillas[i[0]][i[1]] != Utils.CASILLA_VACIA) return false;
		
		return true;
	}//end of comprobarCabeza

	public void turnoFicticio() {
		
		boolean flagGenerarComida = true;
		//COPIAR LAS SERPIENTES REALES
		for(Serpiente s : this.serpientes) 
			this.serpientesFicticias.add(s.clone());
		
		//AVANZAMOS LAS SERPIENTES FICTICIAS
		for(Serpiente s: this.serpientesFicticias)
			s.avanzar();
		
		//COMPROBAR COLISIONES
		this.comprobarColisiones();
		
		
		//COMPROBAR COMIDA
		for(Serpiente s:this.serpientesFicticias)
			if(s.cabeza().equals(comida) && s.isViva()) {
				s.comer();
				flagGenerarComida = true;
				//comida = this.generarComida();
			}
		
		
		
	}

	private void comprobarColisiones() {
		
		
		
		Map<Integer[],String> ocupadas = new HashMap<>();
		for(Serpiente s: this.serpientesFicticias) {
			ocupadas.putIfAbsent(s.getPosiciones()., Utils.CABEZA);
			for(Integer[] i : s.getPosiciones()) {
				
			}	
		}
	}

	private Integer[] generarComida() {
		List<Integer[]> casillasVacias = new ArrayList<>();
		for(int i=0;i<Utils.ALTO;i++)
			for(int j=0; j<Utils.ANCHO;j++)
				if(casillas[i][j]!=Utils.CASILLA_VACIA) {
					Integer[] dummy = new Integer[2];
					dummy[Utils.EJE_Y]=i;
					dummy[Utils.EJE_X]=j;
					casillasVacias.add(dummy);
				}
		Random r = new Random();
		Integer[] posComida = casillasVacias.get(r.nextInt(casillasVacias.size()));
		casillas[posComida[Utils.EJE_Y]][posComida[Utils.EJE_X]]=Utils.CASILLA_COMIDA;
		return posComida;
	}
}
