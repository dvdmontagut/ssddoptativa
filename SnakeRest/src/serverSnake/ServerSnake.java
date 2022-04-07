package serverSnake;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("game")
public class ServerSnake {

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
}//end of class
