package playerSnake;

import java.util.LinkedList;
import java.util.Queue;

import utils.*;

public class PlayerSnake {
	
	public static void main(String[] args) {
		
		String link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inicioDelServidor";
		System.out.println(Utils.peticion(link, Utils.GET));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/crearSala?nJugadores=3";
		String sala = Utils.peticion(link, Utils.POST);
		System.out.println(sala);
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=s";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=e";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=x";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/verTablero?sala="+sala;
		System.out.println(Utils.factoryTablero(Utils.peticion(link, Utils.GET)));
		
		
		LinkedList<String> l =  new LinkedList<>();
		l.add("hola");
		l.add("adios");
		l.add("pene");
		System.out.println(l.peek());
		System.out.println(l.peekFirst());
		System.out.println(l.peekLast());
		
		
		
		
	}
	
}
