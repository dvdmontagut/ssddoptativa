package serverSnake;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import utils.*;

@Singleton
@Path("game")
public class ServerSnake {

		private boolean partidaEnCurso;
		private Tablero tablero;
		private int numeroJugadores;
		private int numeroInscritos;
		private CyclicBarrier barreraEmpezarPartida;
		private Partida p;
		private String salaActiva;
		private List<String> nombres;
		
		public ServerSnake()
		{
			partidaEnCurso=false;
			tablero=new Tablero();
			nombres = new ArrayList<String>();
		}
		
		/**
		 * Este metodo sirve para comprobar que existe el servidor
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/estadoDelServidor
		 * @return
		 */
		@GET 
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("inicioDelServidor") 
		public String saludo() {
			if(!this.partidaEnCurso)
				return "Servidor disponible para jugar";
			return "ERROR. AHORA ESTAN JUGANDO BRO.";
		}//end of saludo
		
		/**
		 * Este metodo sirve para esperar mientras todos dan a inscribirse
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/estadoDelServidor
		 * @return
		 */
		@GET 
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("esperarComienzo") 
		public String espera() {
			try{
				this.barreraEmpezarPartida.await();
			}catch(Exception e) {
				return "ERROR. Problema con la sincronizacion con los demas jugadores.";
			}
			return "Que comience la fiesta";
		}//end of saludo
		
		/**
		 * Este metodo inicializa el servidor para dar paso a una nueva partida
		 * @param nj
		 * @return
		 */
		@GET 
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("crearSala")
		public String crearSala(@QueryParam(value = "nJugadores") int nj) {
			if(!this.partidaEnCurso)
			{
				if(nj < 1 ) return "ERROR. NUMERO DE JUGADORES INFERIOR A 1";
				if(nj > ((Utils.ALTO * Utils.ANCHO)/10)) return "ERROR. NUMERO DE JUGADORES DEMASIADO ALTO";
				Random r = new Random();
				StringBuilder sb = new StringBuilder();
				sb.append(Utils.PALABRAS[r.nextInt(Utils.PALABRAS.length)]).append(r.nextInt(10)).append(r.nextInt(10)).append(r.nextInt(10));
				this.salaActiva = sb.toString();
				this.numeroJugadores = nj;
				this.barreraEmpezarPartida=new CyclicBarrier(numeroJugadores);
				this.numeroInscritos=0;
				nombres = new ArrayList<String>();
				tablero=new Tablero();
				return this.salaActiva;
			}
			return "ERROR. AHORA ESTAN EN PARTIDA BRO.";
		}

		@GET 
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("inscribirse")
		public String inscribirse(@QueryParam(value = "sala") String sala, @QueryParam(value = "nombre") String nombre) {
			if(!sala.equals(this.salaActiva)) return "ERROR. LA SALA NO ES CORRECTA";
			if(nombre.length()!=1) return "ERROR. NOMBRE MUY LARGO.";
			if(numeroJugadores==numeroInscritos) return "ERROR. SORRY, LA SALA YA ESTA LLENA";
			for(String s: nombres)
				if(s.equals(nombre))
					return "ERROR. SORRY, YA HAY ALGUIEN INSCRITO CON ESE NOMBRE";
			numeroInscritos++;
			tablero = null;
			nombres.add(nombre.toLowerCase());
			tablero = new Tablero();
			tablero.construir(numeroInscritos, nombres);
			return "Inscripcion realizada con exito";
		}

		@GET 
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("cambioDireccion")
		public void cambioDireccion(@QueryParam(value = "nombre") String nombre, @QueryParam(value="direccion") String d) 
		{
			Direccion direccion=Direccion.ABAJO;
			switch(d)
			{
				case "ARRIBA": direccion=Direccion.ARRIBA; break;
				case "ABAJO": direccion=Direccion.ABAJO; break;
				case "IZQUIERDA": direccion=Direccion.IZQUIERDA; break;
				case "DERECHA": direccion=Direccion.DERECHA; break;
			}
			for(String s: nombres)
				if(s.equals(nombre))
					{tablero.cambiarDireccion(nombre, direccion); break;}
		}

		@GET 
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("abandonar")
		public void abandonar(@QueryParam(value = "nombre") String nombre) 
		{
			for(String s: nombres)
				if(s.equals(nombre))
					{tablero.matar(nombre); break;}
		}


		
		/**
		 * Este método muestra el tablero
		 * @param sala
		 * @return
		 */
		@GET
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("verTablero")
		public String verTablero() {
			return tablero.toString();
		}//end of verTablero

		
		/**
		 * Este método muestra el tablero
		 * @param sala
		 * @return
		 */
		@GET
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("comenzarPartida")
		public void comenzarPartida() {
			if(!this.partidaEnCurso) 
			{
				 this.partidaEnCurso = true;
				 p = new Partida(this);
				 p.start();
			}
		}//end of comenzarPartida
		
		/**
		 * 
		 */
		boolean partida() {
			
			while(!tablero.partidaAcabada()) 
			{
				Utils.dormir(Utils.TIEMPO_ENTRE_TURNOS);
				tablero.turno();
				System.out.println(Utils.factoryTablero(tablero.toString()));
				System.out.flush();
			}
			this.partidaEnCurso = false;
			return true;
		}
}//end of class
