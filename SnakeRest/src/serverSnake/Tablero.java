package serverSnake;

import java.util.*;

import utils.*;

public class Tablero 
{
	private String[][] casillas;
	private List<Serpiente> serpientes;
	private List<Serpiente> serpientesFicticias;
	private Casilla comida;
	
	
	public Tablero()
	{
		comida = new Casilla();
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
	
	public boolean partidaAcabada() {
		return this.serpientes.size()==0;
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
			serpientes.add(new Serpiente(nombres.get(0), fila,columna,Direccion.ARRIBA));
		}
		else {
			casillas[fila-1][columna] = nombres.get(0);
			serpientes.add(new Serpiente(nombres.get(0), fila,columna,Direccion.ABAJO));
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
			if(fila>Utils.ALTO/2) {
				casillas[fila+1][columna] = nombres.get(i);
				serpientes.add(new Serpiente(nombres.get(i), fila,columna,Direccion.ARRIBA));
			}//End of if
				
			else {
				casillas[fila-1][columna] = nombres.get(i);	
				serpientes.add(new Serpiente(nombres.get(i), fila,columna,Direccion.ABAJO));
			}//End of ELSE
		}//end of for
		//comida
		//Comida que no se dibuja, solo sirve para que no salga excepcion al dibujar
		this.comida.setEjeX(1);
		this.comida.setEjeY(1);
		
		dibujar();
		comida = this.generarComida();
		dibujar();
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
		List<Casilla> posicionesCerca = new ArrayList<>();
		
		posicionesCerca.add(new Casilla (fila, columna)); //CENTRO
		posicionesCerca.add(new Casilla (fila-1, columna)); //ARRIBA
		posicionesCerca.add(new Casilla (fila-1, columna-1)); //ARRIBA IZQ
		posicionesCerca.add(new Casilla (fila-1, columna+1)); //ARRIBA DER
		posicionesCerca.add(new Casilla (fila, columna-1)); //IZQ
		posicionesCerca.add(new Casilla (fila, columna+1)); //DER
		posicionesCerca.add(new Casilla (fila+1, columna)); //ABAJO
		posicionesCerca.add(new Casilla (fila+1, columna-1)); //ABAJO IZQ
		posicionesCerca.add(new Casilla (fila+1, columna+1)); //ABAJO DER
		posicionesCerca.add(new Casilla ((fila-2<0)?fila:fila-2, columna)); //ARRIBA +2
		posicionesCerca.add(new Casilla (fila, (columna-2<0)?columna:columna-2)); //IZQ +2
		posicionesCerca.add(new Casilla (fila, (columna+2>Utils.ANCHO-1)?columna:columna+2)); //DER +2
		posicionesCerca.add(new Casilla ((fila+2>Utils.ALTO-1)?fila:fila+2, columna)); //ABAJO +2
		
		
		for(Casilla i : posicionesCerca) 
			if(this.casillas[i.getEjeY()][i.getEjeX()] != Utils.CASILLA_VACIA) return false;
		
		return true;
	}//end of comprobarCabeza

	public void turno() {
		
		boolean flagGenerarComida = false;
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
			if(s.isViva()&&
					s.cabeza().getEjeX()==this.comida.getEjeX() &&
					s.cabeza().getEjeY()==this.comida.getEjeY())
			{
				s.comer();
				flagGenerarComida = true;
				//comida = this.generarComida();
			}//End of if
		//Rescato
		this.serpientes = new ArrayList<>();
		for(Serpiente s: this.serpientesFicticias)
			if(s.isViva())
				this.serpientes.add(s.clone());
		
		//Generar la manzana
		if(flagGenerarComida) {
			flagGenerarComida = false;
			this.comida=this.generarComida();
		}
		//Dibujo
				this.dibujar();	
		
	}//End of turnoFicticio

	private void dibujar() {
		//Todo a vacio
		for(int f=0;f<Utils.ALTO;f++) 
			for(int c=0;c<Utils.ANCHO;c++)
				casillas[f][c] = Utils.CASILLA_VACIA;
		//Se rellena con cosas
		for(Serpiente s:this.serpientes) {
			for(Casilla i: s.getPosiciones()) {
				casillas[i.getEjeY()][i.getEjeX()] = s.getNombre().toLowerCase();
			}//End of for
			casillas[s.cabeza().getEjeY()][s.cabeza().getEjeX()] = s.getNombre().toUpperCase();
		}//End of for
		casillas[comida.getEjeY()][comida.getEjeX()] = Utils.CASILLA_COMIDA;
	}//End of dibujar

	private void comprobarColisiones() {
		
		boolean cabeza = true;
		Map<Casilla,String> cuerpos = new HashMap<>();
		Map<Casilla,Serpiente> serpientes = new HashMap<>();
		//Cojo todos los cuerpos
		for(Serpiente s: this.serpientesFicticias) {
			for(Casilla i : s.getPosiciones()) {
				if(cabeza) 
					cabeza = false;
				else 
					cuerpos.putIfAbsent(i, Utils.CUERPO);
			}//End of for
			cabeza = true;
		}//End of for
		//Me preocupo por si alguna cabeza colisiona con algun cuerpo
		for(Serpiente s: this.serpientesFicticias) {
			if(cuerpos.putIfAbsent(s.cabeza(), Utils.CABEZA)!=null){
System.err.println("SERPIENTE "+s.getNombre()+ " choca con un cuerpo");
				s.matar();
			}//End of if
		}//End of for
		//Choques de cabezas y salidas de mapa
		for(Serpiente s: this.serpientesFicticias) {
			if(serpientes.putIfAbsent(s.cabeza(), s)!=null){
				System.err.println("SERPIENTE "+s.getNombre()+ " choca con la cabeza de "+ serpientes.get(s.cabeza()).getNombre());
				s.matar();
				serpientes.get(s.cabeza()).matar();
			}//End of if
			
			if(s.cabeza().getEjeX()==Utils.NO_VALIDO || s.cabeza().getEjeY()==Utils.NO_VALIDO)
				s.matar();
		}//End of for
		
	}// ENd of comprobarColisiones

	private Casilla generarComida() {
		List<Casilla> casillasVacias = new ArrayList<>();
		for(int i=0;i<Utils.ALTO;i++)
			for(int j=0; j<Utils.ANCHO;j++)
				if(casillas[i][j]==Utils.CASILLA_VACIA) 
					casillasVacias.add(new Casilla(i,j));
		Random r = new Random();
		Casilla posComida = casillasVacias.get(r.nextInt(casillasVacias.size()));
		return posComida;
	}
}
