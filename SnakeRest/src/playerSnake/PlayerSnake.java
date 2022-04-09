package playerSnake;

import java.util.LinkedList;
import java.util.Queue;
import utils.*;






//BORRAR
import serverSnake.Serpiente;


public class PlayerSnake {
	
	public static void main(String[] args) {
		
		String link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inicioDelServidor";
		System.out.println(Utils.peticion(link, Utils.GET));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/crearSala?nJugadores=18";
		String sala = Utils.peticion(link, Utils.POST);
		System.out.println(sala);
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=a";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=b";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=c";
		System.out.println(Utils.peticion(link, Utils.POST));
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=d";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=e";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=f";
		System.out.println(Utils.peticion(link, Utils.POST));
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=g";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=h";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=i";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=j";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=k";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=l";
		System.out.println(Utils.peticion(link, Utils.POST));
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=m";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=n";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=o";
		System.out.println(Utils.peticion(link, Utils.POST));
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=p";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=q";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+sala+"&nombre=r";
		System.out.println(Utils.peticion(link, Utils.POST));
		
		
		
		link = "http://localhost:8080/SnakeRest/SnakeMRV/game/verTablero?sala="+sala;
		System.out.println(Utils.factoryTablero(Utils.peticion(link, Utils.GET)));
		
		
	}
	
}
