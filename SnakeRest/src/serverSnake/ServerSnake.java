package serverSnake;

import java.util.*;
import java.util.concurrent.Semaphore;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import utils.Utils;

@Singleton
@Path("game")
public class ServerSnake {

		private boolean partidaEnCurso = false;
		private String[][] tablero;
		private int numeroJugadores;
		private Semaphore esperarJugadores;
		private Semaphore dejarVisualizar;
		private Semaphore seccionCriticaEsperaJugador;
		private String salaActiva;
		private String[] palabras= {"Snake", "Serpiente", "Coti", "Usal", "Minimiconicas", "Chamoso", "Montadito" };
		
		/**
		 * Este metodo sirver para comprobar que existe el servidor
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/estadoDelServidor
		 * @return
		 */
		@GET 
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("estadoDelServidor") 
		public String saludo() {
			return "Servidor disponible para jugar";
		}//end of saludo
		
		/**
		 * Este metodo inicializa el servidor para dar paso a una nueva carrera
		 * @param nj
		 * @return
		 */
		@POST
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("crearSala")
		public String crearSala(@QueryParam(value = "nJugadores") int nj) {
			Random r = new Random();
			StringBuilder sb = new StringBuilder();
			sb.append(this.palabras[r.nextInt(palabras.length)]).append(r.nextInt(10)).append(r.nextInt(10)).append(r.nextInt(10));
			this.salaActiva = sb.toString();
			this.numeroJugadores = nj;
			this.esperarJugadores = new Semaphore(0);
			this.dejarVisualizar = new Semaphore(0);
			this.seccionCriticaEsperaJugador = new Semaphore(1);
			tablero = null;
			crearTablero(nj);
			if(this.tablero == null)
				return "ERROR";
			return this.salaActiva;
		}
		
		private void crearTablero(int nj) {
			
			int fila, columna;
			String[] letras= {"a", "b", "c", "d", "e", "f" }; //SERGIO
			if(nj<=0 || nj >= Utils.ALTO*Utils.ANCHO/10 )
				return;
			
			tablero = new String[Utils.ALTO][Utils.ANCHO];
			for(int f=0;f<Utils.ALTO;f++) 
				for(int c=0;c<Utils.ANCHO;c++)
					tablero[f][c] = Utils.CASILLA_VACIA;
			
			fila = this.generarFila();
			columna = this.generarColumna();
			
			tablero[fila][columna] = letras[0].toUpperCase();
			if(fila>Utils.ALTO/2)
			{
				tablero[fila+1][columna] = letras[0];
			}else tablero[fila-1][columna] = letras[0];
			
			//SEUDO BACKTRACK TAMBIEN LLAMADO ENSAYO ERROR
			for(int i=1; i<nj; i++) {
				while(true) {
					fila = generarFila();
					columna = generarColumna();
					if(comprobarCabeza(fila, columna))
						break;
				}
				tablero[fila][columna] = letras[i].toUpperCase();
				if(fila>Utils.ALTO/2)
					tablero[fila+1][columna] = letras[i];
				else 
					tablero[fila-1][columna] = letras[i];		
			}//end of for
			//END OF SEUDO BACKTRACK TAMBIEN LLAMADO ENSAYO ERROR	
			
			return;
			
		}

		private boolean comprobarCabeza(int fila,int columna) {
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
			
			
			for(Integer[] i : posicionesCerca) {
				if(this.tablero[i[0]][i[1]] != Utils.CASILLA_VACIA)
					return false;
			}
			
			return true;
		}

		private int generarColumna() {
			int columna;
			Random r = new Random();
			columna = r.nextInt(Utils.ANCHO);
			columna=(columna==Utils.ANCHO-1)?columna-1:(columna==0)?1:columna; //EVITAR LOS BORDES SUPERIOR E INFERIOR
			return columna;
		}

		private int generarFila() {
			int fila;
			Random r = new Random();
			fila = r.nextInt(Utils.ALTO);
			fila=(fila==Utils.ALTO-1)?fila-1:(fila==0)?1:fila; //EVITAR LOS BORDES SUPERIOR E INFERIOR
			return fila;
		}

		
		/**
		 * Este meotodo muestra el tablero
		 * @param sala
		 * @return
		 */
		@GET
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("verTablero")
		public String verTablero(@QueryParam(value = "sala") String room) {
			
			StringBuilder sb = new StringBuilder();
			if(!room.equals(this.salaActiva))
				return "errorSala";
			
			for(String[] f: tablero)
				for(String c:f)
					sb.append(c).append(Utils.SEPARADOR);
			return sb.toString();
			
		}
		/**
		 * 
		 */
		private boolean partida() {
			
			while(this.partidaEnCurso) {
				
			}
			return true;
		}
}//end of class
