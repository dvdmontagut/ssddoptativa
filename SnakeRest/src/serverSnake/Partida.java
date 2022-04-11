package serverSnake;

/**
 * 
 * Hilo que nos permite hacer partidas asincronas
 * @author David Montagut Pamo, Sergio Rollan Moralejo, Anibal Vaquero Blanco.
 *
 */
public class Partida extends Thread{
	
	private ServerSnake ss;
	public Partida(ServerSnake ss)
	{
		this.ss=ss;
	}

	@Override
	public void run()
	{
		ss.partida();
	}
}
