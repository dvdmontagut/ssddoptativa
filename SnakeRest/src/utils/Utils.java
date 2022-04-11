package utils;

import java.util.concurrent.Semaphore;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class Utils {

	static Client client = ClientBuilder.newClient();
	
	public static final String SEPARADOR1=",";
	public static final String SEPARADOR2=";";
	public static final int ANCHO = 20;
	public static final int ALTO = 15;
	public static final String CASILLA_VACIA="-";
	public static final String CASILLA_COMIDA="*";
	public static final int NO_VALIDO = -54;
	public static final int TIEMPO_ENTRE_TURNOS = 10;
	
	public static final String[] PALABRAS = {
				"Snake", "Serpiente", "Coti", "Usal", "Minimiconicas", "Montadito", "Nano", "Circo", "Reloj",
				"Vacia", "Konami", "Houston", "Remate", "Tablero", "Jugar", "Tiempo", "Distribuido", "PLAN",
				"Alejo", "Oficial", "Rey", "Reina", "Casilla", "Comida", "Video", "Agua", "Fuego", "Rayo",
				"Viento", "Parto", "Trueno", "Cloaca", "Borrador", "Nuevo", "Guardado", "Automata", "Redes",
				"Sonido", "Despertar", "Tarde", "Noche", "Abecedario", "Alfabeto", "Alabarda", "Patada",
				"Cetaceo", "Musica", "OWO", "UWU", "Cumulonimbo", "Elefante", "Sala", "Poker", "Manzana",
				"Comando", "Guardia", "Caligrafia", "Laboratorio", "Mayo", "Nesa", "Flor", "Banda", "Venda",
				"Carretera", "Bateria", "Helado", "Bicicleta", "Triangulo", "Rama", "Trinchera", "Version"
	};
	
		
	
	/*
	 * Usar junto a: 
	 * if (output == null || output.contains("error")) {
	 * 		System.out.println(output == null ? "Error" : output);
	 * 		return false; 
	 * }// End of if
	 */
	public static String peticion(String link) {

		java.net.URI uri = UriBuilder.fromUri(link).build();
		WebTarget target = client.target(uri);
		String respuesta="";
		try {
			respuesta = target.request(MediaType.TEXT_PLAIN).get(String.class);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return respuesta;
	} // End of method
	public static String peticion_throws(String link) throws Exception{

		java.net.URI uri = UriBuilder.fromUri(link).build();
		WebTarget target = client.target(uri);
		return target.request(MediaType.TEXT_PLAIN).get(String.class);
	} // End of method
	
	public static Boolean waitSem(Semaphore s, int n) {
		try {
			s.acquire(n);
		} catch (InterruptedException e) {e.printStackTrace();return false;}
		return true;
	}//End of accederSeccionCritica
	
	public static Boolean signalSem(Semaphore s, int n) {
		try {
			s.release(n);
		} catch (Exception e) {e.printStackTrace();return false;}
		return true;
	}//End of salirSeccionCritica

	public static void dormir(int dormir) {
		try {
			Thread.sleep((long)(dormir*100));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//End of dormir
	
	public static String factoryTablero(String output) {
		if (output == null || output.contains("error")) {
			System.out.println(output == null ? "Error" : output);
			return "error";
		}// End of if
		
		String[] tokens = output.split(Utils.SEPARADOR2);
		StringBuilder sb = new StringBuilder();
		for(String s : tokens) {
			String[] tokens2 = s.split(Utils.SEPARADOR1);
			for(String ss: tokens2)	sb.append(ss);
			sb.append("\n");
		}//end of for
		return sb.toString();
	}//end of factoryTablero

	
	
}//End of class
