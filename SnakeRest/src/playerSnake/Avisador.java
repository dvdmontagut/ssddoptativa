package playerSnake;

import utils.Utils;

public class Avisador extends Thread {
	private Object o;
	public Avisador(Object o)
	{
		this.o=o;
	}
	@Override
	public void run()
	{
		Utils.dormir(10);
		synchronized(o)
		{
			o.notifyAll();
		}
	}
}
