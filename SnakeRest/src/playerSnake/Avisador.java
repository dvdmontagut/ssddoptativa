package playerSnake;

import javax.swing.Timer;

import utils.Utils;

public class Avisador extends Thread {
	
	private Object owo;
	PantallaEsperando pe;
	
	public Avisador(Object owo, PantallaEsperando pe)
	{
		this.owo=owo;
		this.pe=pe;
	}
	@Override
	public void run()
	{
		Utils.dormir(10);
		synchronized(owo)
		{
			owo.notifyAll();
		}
		synchronized(owo)
		{
			try {
				owo.wait();
			}catch(Exception ex)
			{
				return;
			}
		}

		new Timer(100, pe);
	}
}
