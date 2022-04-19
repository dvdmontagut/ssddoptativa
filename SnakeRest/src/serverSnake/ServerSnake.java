package serverSnake;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import utils.*;

/**
 * 
 *
 * @author David Montagut Pamo, Sergio Rollan Moralejo, Anibal Vaquero Blanco.
 *
 */
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
		private Object seccionCritica;
		private Semaphore comprobarJugadores;
		private Set<String> jugadoresActivos;
		
		
		/**
		 * Constructor del server
		 */
		public ServerSnake()
		{
			partidaEnCurso=false;
			tablero=new Tablero();
			nombres = new ArrayList<String>();
			seccionCritica=new Object();
			jugadoresActivos = new HashSet<>();
		}
		
		/**
		 * Este metodo sirve para comprobar si se puede jugar una nueva partida
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/inicioDelServidor
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
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/esperarComienzo
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
		 * @param nj Numero de jugadores que obligatoriamente asistiran a la partida
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/crearSala?nJugadores=3
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
				//GENERAR IDENTIFICADOR DE LA SALA
				sb.append(Utils.PALABRAS[r.nextInt(Utils.PALABRAS.length)]).append(r.nextInt(10)).append(r.nextInt(10)).append(r.nextInt(10));
				this.salaActiva = sb.toString();
				this.numeroJugadores = nj;
				//DECLARAMOS LA BARRERA PARA QUE TODOS LOS JUGADORES SE ESPEREN
				this.barreraEmpezarPartida=new CyclicBarrier(numeroJugadores);
				this.numeroInscritos=0;
				nombres = new ArrayList<String>();
				tablero=new Tablero();
				this.comprobarJugadores = new Semaphore(0);
				return this.salaActiva;
			}
			return "ERROR. AHORA ESTAN EN PARTIDA BRO.";
		}

		/**
		 * Metodo que sirve para inscribirse a una partida
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala=Yaki054&nombre=T
		 * @param sala nombre de la sala
		 * @param nombre letra con la que se definira tu serpiente
		 */
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
			synchronized(seccionCritica) 
			{
				numeroInscritos++;
				nombres.add(nombre.toLowerCase());
			}
			return "Inscripcion realizada con exito";
		}

		/**
		 * Metodo que sirve para indicar un cambio de direccion de tu serpiente
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/cambioDireccion?nombre=T&direccion=ABAJO
		 * @param nombre
		 * @param d
		 */
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
			tablero.cambiarDireccion(nombre, direccion);
			System.out.println("CAMBIA LA " + nombre );
		}

		/**
		 * Metodo para abandonar una partida
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/abandonar?nombre=T
		 * @param nombre
		 */
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
		 * Este metodo muestra el tablero
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/verTablero
		 * @param sala
		 */
		@GET
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("verTablero")
		public String verTablero(@QueryParam(value = "nombre") String nombre) {
			if(!this.nombres.contains(nombre)) 
				return "Error, se te ha dado de baja";
			this.comprobarJugadores.release();
			try {
				this.comprobarJugadores.acquire();
			}catch(Exception e) {
				e.printStackTrace();
			}
			this.jugadoresActivos.add(nombre);
			System.out.println("pene");
			return tablero.toString();
		}//end of verTablero

		
		/**
		 * Este meotodo comienza una partida y te devuelve el codigo de la sala
		 * http://localhost:8080/SnakeRest/SnakeMRV/game/comenzarPartida
		 * @param sala
		 */
		@GET
		@Produces(MediaType.TEXT_PLAIN) 
		@Path("comenzarPartida")
		public void comenzarPartida() {
			synchronized(seccionCritica) 
			{
				if(!this.partidaEnCurso) 
				{
					 this.partidaEnCurso = true;
					 this.tablero.construir(numeroJugadores, nombres); //GENERAMOS EL TABLERO CON TODOS LOS JUGADORES
					 p = new Partida(this);
					 p.start();
				}
			}
			
		}//end of comenzarPartida
		
		/**
		 * Este metodo es el bucle donde se juega la partida
		 */
		boolean partida() {
			int numeroActivos = this.numeroJugadores;
			int turno = 0;
			while(!tablero.partidaAcabada()) 
			{
				tablero.turno();
				turno ++;
				System.out.println(Utils.factoryTablero(tablero.toString())); //CAMBIAR A EL LOG
				System.out.flush();
				try {
					if(!this.comprobarJugadores.tryAcquire(numeroActivos,Utils.TIMEOUT, TimeUnit.SECONDS)) {
						System.out.println("AVANZO POR INCOMPADECENCIA " + turno);
						numeroActivos = this.comprobarJugadores.availablePermits();
						this.comprobarJugadores.acquire(numeroActivos);
						this.nombres = new ArrayList<>();
						for(String s : this.jugadoresActivos)
							nombres.add(s);
					}else {

						System.out.println("AVANZO BIEN "+ turno);
						this.comprobarJugadores.release(numeroActivos);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Utils.dormir(Utils.TIEMPO_ENTRE_TURNOS);
				
			}
			this.partidaEnCurso = false;
			return true;
		}
}//end of class
