package playerSnake;

import java.awt.event.*;
import javax.swing.*;
import utils.*;

public class PantallaPrincipal extends JFrame implements ActionListener
{
	JPanel jp;
	JLabel titulo;
	JButton crear,unirse;
	public PantallaPrincipal()
	{

		setBounds(400,400,300,400);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		titulo = new JLabel("SnakeMRV");
		add(titulo);
		jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
			crear=new JButton("Crear sala");
			crear.addActionListener(this);
			unirse=new JButton("Unirse a sala");
			unirse.addActionListener(this);
			jp.add(crear);
			jp.add(unirse);
		add(jp);
		this.setVisible(true);
	}//End of PantallaPrincipal

	@Override
	public void actionPerformed(ActionEvent e) {
		Object fuente = e.getSource();
		int opcion = (fuente==crear)?1:(fuente==unirse)?2:0;
		switch(opcion)
		{
			case 1: crearNuevaSala(); break;
			case 2: unirseAUnaSala(); break;
		}
		
	}//End of actionPerformed

	private void unirseAUnaSala() {
		this.setVisible(false);
		PantallaInscripcion pi = new PantallaInscripcion();
		if(pi.esCancelado())
		{
			this.setVisible(true);
			return;
		}
		String ipTarget = pi.getIpTarget();
		String nombreSala = pi.getNombreSala();
		String nombre = pi.getNombreJug();
		nombre=nombre.toLowerCase();

		String link = "http://"+ipTarget+":8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+nombreSala+"&nombre="+nombre;
		String respuesta="";
		try {
			respuesta=Utils.peticion_throws(link, Utils.GET);
		}catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					this, 
					"Error de servidor", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.setVisible(true);
			return;
		}
		if(respuesta.contains("ERROR"))
		{
			JOptionPane.showMessageDialog(
					this, 
					respuesta, 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.setVisible(true);
			return;
		}
		JOptionPane.showMessageDialog(
				this, 
				respuesta, 
				"",
				JOptionPane.INFORMATION_MESSAGE);
		PantallaEsperando pe = new PantallaEsperando();
		//Espera a barrera
		PantallaJuego pjg = new PantallaJuego(nombre);
		this.setVisible(true);
	}//End of unirseAUnaSala

	private void crearNuevaSala() {
		
		this.setVisible(false);
		
		String ipNueva = (String)JOptionPane.showInputDialog(this,"IP del servidor: ",
				"", JOptionPane.PLAIN_MESSAGE, null, null, "localhost");
		String link = "http://"+ipNueva+":8080/SnakeRest/SnakeMRV/game/inicioDelServidor";
		String respuesta="";
		try {
			respuesta=Utils.peticion_throws(link, Utils.GET);
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(
					this, 
					"No se ha encontrado el servidor", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.setVisible(true);
			return;
		}
		if(!respuesta.equals("Servidor disponible para jugar"))
		{
			JOptionPane.showMessageDialog(
					this, 
					"Error desconocido contactando con el servidor", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.setVisible(true);
			return;
		}
		JOptionPane.showMessageDialog(
				this, 
				respuesta, 
				"",
				JOptionPane.INFORMATION_MESSAGE);
			
		String nJugs = (String)JOptionPane.showInputDialog(this,"Número de jugadores: ",
				"", JOptionPane.PLAIN_MESSAGE, null, null, "localhost");
		int nj = 0;
		try
		{
			nj=Integer.parseInt(nJugs);
			if(nj<1)
			{
				JOptionPane.showMessageDialog(
						this, 
						"El número tiene que ser positivo", 
						"WARNING",
						JOptionPane.WARNING_MESSAGE);
				this.setVisible(true);
				return;
			}
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(
					this, 
					"Tiene que ser un número", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.setVisible(true);
			return;
		}
		
		link = "http://"+ipNueva+":8080/SnakeRest/SnakeMRV/game/crearSala?nJugadores="+nj;
		try {
			respuesta=Utils.peticion_throws(link, Utils.GET);
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(
					this, 
					"No se ha encontrado el servidor", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.setVisible(true);
			return;
		}
		if(respuesta.contains("ERROR"))
		{
			JOptionPane.showMessageDialog(
					this, 
					respuesta, 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.setVisible(true);
			return;
		}

		JOptionPane.showMessageDialog(
				this, 
				"El nombre de la sala creada es: "+respuesta, 
				"",
				JOptionPane.INFORMATION_MESSAGE);
		
		this.setVisible(true);
	}//End of crearNuevaSala
	
}//End of class
