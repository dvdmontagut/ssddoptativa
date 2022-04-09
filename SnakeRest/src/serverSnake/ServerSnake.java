package serverSnake;

import java.util.*;
import java.util.concurrent.Semaphore;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import utils.*;

@Singleton
@Path("game")
public class ServerSnake {

		private boolean partidaEnCurso;
		private Tablero tablero;
		private List<Serpiente> snakes;
		private int numeroJugadores;
		private int numeroInscritos;
		private Semaphore esperarJugadores;
		private Semaphore dejarVisualizar;
		private Semaphore seccionCriticaEsperaJugador;
		private String salaActiva;
		private List<String> nombres;
		private String[] letras;
		
		/**
		 * Este metodo sirve para comprobar que existe el servidor
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/estadoDelServidor
		 * @return
		 */
		@GET 
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("inicioDelServidor") 
		public String saludo() {
			tablero=new Tablero();
			partidaEnCurso=false;
			nombres = new ArrayList<String>();
			return "Servidor disponible para jugar";
		}//end of saludo
		
		/**
		 * Este metodo inicializa el servidor para dar paso a una nueva partida
		 * @param nj
		 * @return
		 */
		@POST
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("crearSala")
		public String crearSala(@QueryParam(value = "nJugadores") int nj) {
			if(nj < 1 ) return "ERROR. NUMERO DE JUGADORES INFERIOR A 1";
			if(nj > ((Utils.ALTO * Utils.ANCHO)/10)) return "ERROR. NUMERO DE JUGADORES DEMASIADO ALTO";
			Random r = new Random();
			StringBuilder sb = new StringBuilder();
			sb.append(Utils.PALABRAS[r.nextInt(Utils.PALABRAS.length)]).append(r.nextInt(10)).append(r.nextInt(10)).append(r.nextInt(10));
			this.salaActiva = sb.toString();
			this.numeroJugadores = nj;
			this.numeroInscritos=0;
			this.esperarJugadores = new Semaphore(0);
			this.dejarVisualizar = new Semaphore(0);
			this.seccionCriticaEsperaJugador = new Semaphore(1);
			tablero=new Tablero();
			return this.salaActiva;
		}
		
		@POST
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("inscribirse")
		public String inscribirse(@QueryParam(value = "sala") String sala, @QueryParam(value = "nombre") String nombre) {
			if(!sala.equals(this.salaActiva)) return "ERROR. LA SALA NO ES CORRECTA";
			if(nombre.length()!=1) return "ERROR. NOMBRE MUY LARGO.";
			if(numeroJugadores==numeroInscritos) return "SORRY. LA SALA YA ESTÁ LLENA";
			for(String s: nombres)
				if(s.equals(nombre))
					return "SORRY. YA HAY ALGUIEN INSCRITO CON ESE NOMBRE";
			numeroInscritos++;
			tablero = null;
			nombres.add(nombre.toLowerCase());
			tablero = new Tablero();
			tablero.construir(numeroInscritos, nombres);
			return "Ok";
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
			 partida();
			/*if(!room.equals(this.salaActiva))
				return "ERROR. LA SALA NO ES CORRECTA";
			
			return tablero.toString();*/
			 return"hola^^";
			
		}//end of verTablero
		
		/**
		 * 
		 */
		private boolean partida() {
			
			while(this.partidaEnCurso) {
				//DAR UNOS SEGUNDOS PARA RECIBIR ORDENES
				tablero.turno();
				System.out.println(tablero.toString());
				/*	tablero.comprobarColisionesBorde();
					tablero.comprobarColisionesSerpiente();
					tablero.matar();
				tablero.turnoReal();
					tablero.generarComida();*/
			}
			return true;
		}
}//end of class
