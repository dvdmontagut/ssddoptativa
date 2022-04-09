package playerSnake;

import javax.swing.*;

import utils.Utils;


public class PantallaEsperando extends JDialog {
	
	private JLabel esperaL;
	private int turno;
	public PantallaEsperando()
	{
		setModal(true);
		setBounds(400,400,500,130);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		esperaL = new JLabel("Esperando");
		turno=0;
		cambioLabel();
	}
	private void cambioLabel() {
		while(true)
		{
			Utils.dormir(10);
			switch(turno)
			{
				case 0: turno=1; 	esperaL.setText("Esperando.");	break;
				case 1: turno=2;   esperaL.setText("Esperando.."); 	break;
				case 2: turno=3;  esperaL.setText("Esperando...");	break;
				case 3: turno=0;     esperaL.setText("Esperando"); 	break;
			}
			//Peticion rest pa preguntar si ya hay que empezar
			//if ya hay que empezar this.dispose()
		}
	}

}
