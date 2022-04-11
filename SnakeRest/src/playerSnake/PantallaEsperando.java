package playerSnake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import utils.Utils;


/**
 * 
 *
 * @author David Montagut Pamo, Sergio Rollan Moralejo, Anibal Vaquero Blanco.
 *
 */
public class PantallaEsperando extends JDialog implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	
	private JLabel esperaL;
	private boolean bien;
	Object owo;
	
	public PantallaEsperando(String ip, Object owo)
	{
		this.owo=owo;
		bien=false;
		setBounds(400,400,500,150);
		setResizable(false);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		esperaL = new JLabel("Esperando a que se unan los dem�s jugadores...");
		add(esperaL);
		setVisible(true);

		String link = "http://"+ip+":8080/SnakeRest/SnakeMRV/game/esperarComienzo";
		String respuesta="";
		try{
			respuesta = Utils.peticion_throws(link);
		}catch(Exception ex)
		{
			JOptionPane.showMessageDialog(
					this, 
					"Error de servidor", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		if(respuesta.contains("ERROR"))
		{
			JOptionPane.showMessageDialog(
					this, 
					respuesta, 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		bien=true;
		Avisador a = new Avisador(owo, this);
		a.start();
	}
	
	public boolean esCancelado() {return !bien;}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		owo.notifyAll();
		this.dispose();
	}

}
