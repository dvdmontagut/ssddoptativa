package serverSnake;

import java.util.*;

import utils.*;
/**
 * 
 * Representa un tablero que contiene casillas, serpientes y simulaciones de serpientes para calcular sus futuras acciones
 * @author David Montagut Pamo, Sergio Rollan Moralejo, Anibal Vaquero Blanco.
 *
 */
public class Tablero 
{
	/**
	 * Matriz en la que cada posicion representa una casilla del tablero
	 */
	private String[][] casillas;
	private List<Serpiente> serpientes;
	/**
	 * Representacion de una serpiente para poder hacer calculos violando las normas del snake
	 */
	private List<Serpiente> serpientesFicticias;
	private Casilla comida;
	
	/**
	 * Constructor que inicializa las casillas, pero no implenta serpientes
	 */
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
	
	/**
	 * 
	 * @return false si la partida sigue en curso, true si la partida ha acabado
	 */
	public boolean partidaAcabada() {
		return this.serpientes.size()==0;
	}
	
	/**
	 * Metodo que construye un tablero incial ALEATORIO con serpientes de 1x2 en funcion de la lista de jugadores
	 * @param nj Numero de jugadores
	 * @param nombres Lista de letras que define a cada jugador
	 */
	public void construir(int nj, List<String> nombres)
	{
		//PONEMOS EL TABLERO VACIO
		for(int f=0;f<Utils.ALTO;f++) 
			for(int c=0;c<Utils.ANCHO;c++)
				casillas[f][c] = Utils.CASILLA_VACIA;
		
		int fila = this.generarFila();
		int columna = this.generarColumna();
		
		//PONEMOS LA PRIMERA SERPIENTE DENTRO DE LOS LIMITES DEL TABLERO
		casillas[fila][columna] = nombres.get(0).toUpperCase();
		if(fila>Utils.ALTO/2){	
			casillas[fila+1][columna] = nombres.get(0);
			serpientes.add(new Serpiente(nombres.get(0), columna,fila,Direccion.ARRIBA));
		}
		else {
			casillas[fila-1][columna] = nombres.get(0);
			serpientes.add(new Serpiente(nombres.get(0), columna,fila,Direccion.ABAJO));
		}
		
		
		
		//SEUDO BACKTRACK TAMBIEN LLAMADO ENSAYO-ERROR PARA PONER EL RESTO DE SERPIENTES
		//DE FORMA ALEATORIA DENTRO DEL TABLERO Y DIBUJARLAS
		//ESTE FOR REFERENCIA A POSICIONAR LAS SERPIENTES
		for(int i=1; i<nj; i++) {
			while(true) {
				fila = generarFila();
				columna = generarColumna();
				if(comprobarCabeza(fila, columna)) break;
			}//end of while
			casillas[fila][columna] = nombres.get(i).toUpperCase();
			if(fila>Utils.ALTO/2) {	//LAS SERPIENTES SITUADAS EN LA MITAD INFERIOR DEL TABLERO MIRAN HACIA ARRIBA Y LAS SITUADAS EN LA PARTE SUPERIOR MIRAN HACIA ABAJO
				casillas[fila+1][columna] = nombres.get(i);
				serpientes.add(new Serpiente(nombres.get(i), columna,fila,Direccion.ARRIBA));
			}//End of if
				
			else {
				casillas[fila-1][columna] = nombres.get(i);	
				serpientes.add(new Serpiente(nombres.get(i), columna,fila,Direccion.ABAJO));
			}//End of else
		}//end of for
		
		//REFERENCIA A POSICIONAR UNA COMIDA FALSA YA QUE POR MOTIVOS DE IMPLMENTACION DEBEMOS GENERAR PRIMERO UNA
		//PARA EVITAR NULL POINTERS A LA HORA DE DIBUJAR LAS SERPIENTES QUE SON NECESARIAS PARA PODER DIBUJAR UNA COMIDA
		this.comida.setEjeX(1);
		this.comida.setEjeY(1);
		
		dibujar();
		comida = this.generarComida(); //AQUI SE GENERA LA COMIDA QUE SE MOSTRARA DE VERDAD
		dibujar();
		//END OF SEUDO BACKTRACK TAMBIEN LLAMADO ENSAYO-ERROR		
	}
	
	/**
	 * Genera una cordenada de columna dentro de los limites -1
	 */
	private int generarColumna()
	{
		int columna;
		Random r = new Random();
		columna = r.nextInt(Utils.ANCHO-2)+1; //EVITAR LOS BORDES IZQUIERDO Y DERECHO
		return columna;
	}//end of generarColumna

	/**
	 * Genera una cordenada de fila dentro de los limites -1
	 */
	private int generarFila() 
	{
		int fila;
		Random r = new Random();
		fila = r.nextInt(Utils.ALTO-2)+1; //EVITAR LOS BORDES SUPERIOR E INFERIOR
		return fila;
	}//end of generarFila


	/**
	 * Metodo usado para generar serpientes sin que sus cabezas esten a menos de una casilla de distancia
	 * @param fila coordenada y de la serpiente que se esta generando
	 * @param columna coordenada x de la serpiente que se esta generando
	 * @return true cuando la posicion de la cabeza es valida, false cuando la posicion de la cabeza es invalida
	 */
	private boolean comprobarCabeza(int fila,int columna) 
	{
		List<Casilla> posicionesCerca = new ArrayList<>();
		

		posicionesCerca.add(new Casilla (columna-1, fila-1)); //ARRIBA IZQUIERDA
		posicionesCerca.add(new Casilla (columna, fila-1)); //ARRIBA
		posicionesCerca.add(new Casilla (columna+1, fila-1)); //ARRIBA DERECHA
		posicionesCerca.add(new Casilla (columna-1, fila)); //IZQUIERDA
		posicionesCerca.add(new Casilla (columna, fila)); //CENTRO
		posicionesCerca.add(new Casilla (columna+1, fila)); //DERECHA
		posicionesCerca.add(new Casilla (columna+1, fila+1)); //ARRIBA IZQUIERDA
		posicionesCerca.add(new Casilla (columna, fila+1)); //ABAJO
		posicionesCerca.add(new Casilla (columna-1, fila+1)); //ABAJO DERECHA
		
		posicionesCerca.add(new Casilla ((columna-2<0)?columna:columna-2, fila)); //IZQ +2
		posicionesCerca.add(new Casilla (columna, (fila-2<0)?fila:fila-2)); //ARR +2
		posicionesCerca.add(new Casilla (columna, (fila+2>Utils.ALTO-1)?fila:fila+2)); //AB +2
		posicionesCerca.add(new Casilla ((columna+2>Utils.ANCHO-1)?columna:columna+2, fila)); //DER +2		


		for(Casilla i : posicionesCerca) 
			if(this.casillas[i.getEjeY()][i.getEjeX()] != Utils.CASILLA_VACIA) return false;
		
		return true;
	}//end of comprobarCabeza

	/**
	 * Lleva la logica del turno del snake. En el codigo se ve bien comentada esa logica
	 */
	public void turno() {
		
		boolean flagGenerarComida = false;
		//COPIAR LAS SERPIENTES REALES
		this.serpientesFicticias = new ArrayList<>();
		for(Serpiente s : this.serpientes) 
			if(s.estaViva()) 
				this.serpientesFicticias.add(s.clone());
		
		//AVANZAMOS LAS SERPIENTES FICTICIAS
		for(Serpiente s: this.serpientesFicticias)
			s.avanzar();
		
		//COMPROBAR COLISIONES
		this.comprobarColisiones();
		
		
		//COMPROBAR COMIDA
		for(Serpiente s:this.serpientesFicticias)
			if(s.estaViva()&&s.cabeza().igualQue(comida))
			{
				s.comer();
				flagGenerarComida = true;
			}//End of if
		
		//RESCATO
		this.serpientes = new ArrayList<>();
		for(Serpiente s: this.serpientesFicticias)
			if(s.estaViva())
				this.serpientes.add(s.clone());
		
		//GENERAR COMIDA SI FUESE NECESARIO
		if(flagGenerarComida) {
			flagGenerarComida = false;
			this.comida=this.generarComida();
		}
		
		//DIBUJO
		this.dibujar();	
		
	}//End of turno

	/**
	 * Da valores a la matriz del tablero para poner una instancia de la partida
	 */
	private void dibujar() {
		//INICIALIZAMOS EL TABLERO VACIO
		for(int f=0;f<Utils.ALTO;f++) 
			for(int c=0;c<Utils.ANCHO;c++)
				casillas[f][c] = Utils.CASILLA_VACIA;
		//COMPLETAMOS CON LAS SERPIENTES Y LA COMIDA
		for(Serpiente s:this.serpientes) {
			for(Casilla i: s.getPosiciones()) {
				casillas[i.getEjeY()][i.getEjeX()] = s.getNombre().toLowerCase();
			}//End of for
			casillas[s.cabeza().getEjeY()][s.cabeza().getEjeX()] = s.getNombre().toUpperCase();
		}//End of for
		casillas[comida.getEjeY()][comida.getEjeX()] = Utils.CASILLA_COMIDA;
	}//End of dibujar

	/**
	 * Buen metodo, se encarga de comprobar que las serpientes sigan vivas segun distintas reglas
	 * Sigan dentro del tablero
	 * En caso de chocar dos cabezas mueren ambas
	 * En caso de chocar cuerpo con cabeza muere la segunda serpiente
	 */
	private void comprobarColisiones() {
		
		boolean cabeza = true;
		Map<Casilla,String> cuerpos = new HashMap<>();			//USAMOS MAPAS PORQUE ES UNA FORMA EFICIENTE DE COMPROBAR VALORES ...
		Map<Casilla,Serpiente> serpientes = new HashMap<>(); 	//... REPETIDOS ALMACENADOS EN UNA LISTA
		//COJO TODOS LOS CUERPOS DE LAS SERPIENTES PARA PODER SABER SU LOCALIZACION
		for(Serpiente s: this.serpientesFicticias) {
			for(Casilla i : s.getPosiciones()) {
				if(cabeza) 
					cabeza = false;
				else 
					cuerpos.putIfAbsent(i, "54");
			}//End of for
			cabeza = true;
		}//End of for
		
		//ME PREOCUPO POR SI ALGUNA CABEZA COLISIONA CON ALGUN CUERPO
		for(Serpiente s: this.serpientesFicticias) 
					for(Casilla c: cuerpos.keySet())
						if(s.cabeza().igualQue(c)) s.matar();
		
		//CHOQUES DE CABEZAS Y SALIDAS DE MAPA
		for(Serpiente s: this.serpientesFicticias) 
		{
			for(Casilla c: serpientes.keySet())
			{
				if(s.cabeza().igualQue(c)) 
				{
					s.matar();
					serpientes.get(c).matar();
				}//End of if
			}
			if(s.cabeza().getEjeX()==Utils.NO_VALIDO || s.cabeza().getEjeY()==Utils.NO_VALIDO) s.matar();
			serpientes.putIfAbsent(s.cabeza(), s);
			
		}//End of for
		
	}// End of comprobarColisiones

	/**
	 * Se genera una comida en una casilla evitando las casillas ocupadas por serpientes
	 * @return objeto Casilla con las coordenadas de la comida
	 */
	private Casilla generarComida() {
		List<Casilla> casillasVacias = new ArrayList<>();
		for(int i=0;i<Utils.ALTO;i++)
			for(int j=0; j<Utils.ANCHO;j++)
				if(casillas[i][j]==Utils.CASILLA_VACIA) 
					casillasVacias.add(new Casilla(j,i));	//ALMACENAMOS TODAS LAS CASILLAS VACIAS
		Random r = new Random();
		Casilla posComida = casillasVacias.get(r.nextInt(casillasVacias.size()));	//PARA ASI PODER COGER ALEATORIAMIENTE UNA CASILLA ...
		return posComida;															//... DE ESTA MANERA SIEMPRE TENEMOS EFICIENCIA ALTO*ANCHO ...
																					//... YA QUE SI LO GENERASEMOS ALEATRIAMENTE Y HAY POCAS CASILLAS VACIAS ...
																					//... PODRIA QUEDARSE CALCULANDO UN TIEMPO INDETERMINADO
	}
	
	/**
	 * Cambia la direccion de la serpiente si fuese posible
	 * @param nombre letra que hacer referencia a la serpiente
	 * @param direccion direcion nueva
	 */
	public void cambiarDireccion(String nombre, Direccion direccion)
	{
		for(Serpiente s: this.serpientes)
			if(s.getNombre().equals(nombre)) {s.setDireccion(direccion); break;}
	}
	
	/**
	 * Cambia el estado de una serpiente a muerta
	 * @param nombre letra que hacer referencia a la serpiente
	 */
	public void matar(String nombre)
	{
		for(Serpiente s: this.serpientes)
			if(s.getNombre().equals(nombre)) {s.matar(); break;}
	}
}
