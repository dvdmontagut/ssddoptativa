package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 * Clase con metodos usados por todas las demas clases del programa. Utils es a nosotros lo que biblioteca.jar a Coti.
 * @author David Montagut Pamo, Sergio Rollan Moralejo, Anibal Vaquero Blanco.
 *
 */
public class Utils {

	static Client client = ClientBuilder.newClient();
	
	public static final String SEPARADOR1=",";
	public static final String SEPARADOR2=";";
	public static final int ANCHO = 20;
	public static final int ALTO = 15;
	public static final String CASILLA_VACIA="-";
	public static final String CASILLA_COMIDA="*";
	public static final int NO_VALIDO = -54;
	public static final int TIEMPO_ENTRE_TURNOS = 10; //decimas de segundo
	public static final int TIMEOUT = 4;  //segundos
	
	public static final String[] PALABRAS = {
				"Snake", "Serpiente", "Coti", "Usal", "Minimiconicas", "Montadito", "Nano", "Circo", "Reloj",
				"Vacia", "Konami", "Houston", "Remate", "Tablero", "Jugar", "Tiempo", "Distribuido", "PLAN",
				"Alejo", "Oficial", "Rey", "Reina", "Casilla", "Comida", "Video", "Agua", "Fuego", "Rayo",
				"Viento", "Parto", "Trueno", "Cloaca", "Borrador", "Nuevo", "Guardado", "Automata", "Redes",
				"Sonido", "Despertar", "Tarde", "Noche", "Abecedario", "Alfabeto", "Alabarda", "Patada",
				"Cetaceo", "Musica", "OWO", "UWU", "Cumulonimbo", "Elefante", "Sala", "Poker", "Manzana",
				"Comando", "Guardia", "Caligrafia", "Laboratorio", "Mayo", "Nesa", "Flor", "Banda", "Venda",
				"Carretera", "Bateria", "Helado", "Bicicleta", "Triangulo", "Rama", "Trinchera", "Version", "Taki",
				"Yaki", "Chicken", "Teriyaki", "Rosalia", "Jamon", "Ficha", "SHCE", "Rex", "WillyRex"
	};
	
		
	
	/**
	 * Hace una peticion de tipo GET.
	 * @param link cadena de texto con el link a usar incluidos parametros
	 * @return retorna la respuesta del servidor
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
	
	/**
	 * Hace una peticion de tipo GET.
	 * @param link cadena de texto con el link a usar incluidos parametros
	 * @return retorna la respuesta del servidor
	 * @throws Exception
	 */
	public static String peticion_throws(String link) throws Exception{

		java.net.URI uri = UriBuilder.fromUri(link).build();
		WebTarget target = client.target(uri);
		return target.request(MediaType.TEXT_PLAIN).get(String.class);
	} // End of method
	
	
	/**
	 * Hace un Thread.sleep
	 * @param dormir tiempo se espera recibir en decimas de segundo
	 */
	public static void dormir(int dormir) {
		try {
			Thread.sleep((long)(dormir*100));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//End of dormir
	
	/**
	 * Devuelve una cadena de texto con intros para mostrar el tablero
	 * de forma adecuada
	 * @param input Cadena proveniente del servidor
	 * @return
	 */
	public static String factoryTablero(String input) {
		if (input == null || input.contains("error")) {
			System.out.println(input == null ? "Error" : input);
			return "error";
		}// End of if
		
		String[] tokens = input.split(Utils.SEPARADOR2);
		StringBuilder sb = new StringBuilder();
		for(String s : tokens) {
			String[] tokens2 = s.split(Utils.SEPARADOR1);
			for(String ss: tokens2)	sb.append(ss);
				sb.append("\n");
		}//end of for
		return sb.toString();
	}//end of factoryTablero
	
	public static void crearLog(String sala) {
		String ruta = Paths.get(System.getProperty("user.home"),sala+".log").toString();
		File f = new File(ruta);
		if(f.exists()) f.delete();
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//REFERENCIA DE https://es.stackoverflow.com/questions/68526/agregar-contenido-a-un-archivo-sin-sobrescribir-el-contenido
	public static void escribirLog(String sala, String data){
		String ruta = Paths.get(System.getProperty("user.home"),sala+".log").toString();
		File f = new File(ruta);
		
		
		BufferedWriter bw = null;
	    FileWriter fw = null;
	    try {
	        // Si el archivo no existe, se crea!
	        if (!f.exists()) {
	            f.createNewFile();
	        }
	        // flag true, indica adjuntar información al archivo.
	        fw = new FileWriter(f, true);
	        bw = new BufferedWriter(fw);
	        bw.write(data);
	        bw.write("\n");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
        try {
                        //Cierra instancias de FileWriter y BufferedWriter
            if (bw != null)
                bw.close();
            if (fw != null)
                fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
}//End of class
