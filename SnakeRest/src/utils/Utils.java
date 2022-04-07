package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;

public class Utils {

	public static final String SEPARADOR=";";
	public static final int ANCHO = 20;
	public static final int ALTO = 15;
	public static final String CASILLA_VACIA="·";
	public static final String GET = "GET";
	public static final String POST = "POST";

	/*
	 * Usar junto a: if (output == null || output.contains("error")) {
	 * System.out.println(output == null ? "Error" : output); return false; } // End
	 * of if
	 */
	public static String peticion(String link, String method) {
				
		try {
			URL url;
			String output;
			//System.out.println(LocalDateTime.now()+": "+link);
			url = new URL(link);
			StringBuilder sb = new StringBuilder();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			conn.disconnect();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
			Thread.sleep((long)(dormir*100));} catch (Exception e) {e.printStackTrace();
		}
	}//End of dormir
	
	public static String FactoryTablero(String tablero) {
		int linea = 0;
		if(tablero.contains("error"))
		return "error";
		
		String[] tokens = tablero.split(Utils.SEPARADOR);
		StringBuilder sb = new StringBuilder();
		for(String s : tokens) {
			sb.append(s);
			if(++linea == Utils.ANCHO) {
				sb.append("\n");
				linea = 0;
			}
		}
		return sb.toString();
			
		
	}

	
}//End of class
