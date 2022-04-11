package serverSnake;

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
