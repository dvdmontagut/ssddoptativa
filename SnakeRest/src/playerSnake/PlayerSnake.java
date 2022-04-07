package playerSnake;

import utils.Utils;

public class PlayerSnake {
	
	public static void main(String[] args) {
		
		String link = "http://localhost:8080/SnakeRest/SnakeMRV/game/estadoDelServidor";
		System.out.println(Utils.peticion(link, Utils.GET));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/crearSala?nJugadores=3";
		String sala = Utils.peticion(link, Utils.POST);
		System.out.println(sala);
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/verTablero?sala="+sala;
		System.out.println(Utils.FactoryTablero(Utils.peticion(link, Utils.GET)));
		
	}
	
}
