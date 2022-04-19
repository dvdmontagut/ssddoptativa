package playerSnake;

import java.awt.event.*;
import javax.swing.*;
import utils.*;

/**
 * 
 * Pantalla principal del juego desde el lado del cliente
 * @author David Montagut Pamo, Sergio Rollan Moralejo, Anibal Vaquero Blanco.
 *
 */
public class PantallaPrincipal extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	JPanel jp;
	JLabel titulo;
	JButton crear,unirse;
	
	/**
	 * Constructor, dibuja lo que se va a ver en la pantalla
	 */
	public PantallaPrincipal()
	{

		setBounds(400,400,300,150);
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

	/**
	 * Hace la peticion de inscribirse y comprueba erores
	 */
	private void unirseAUnaSala() {
		PantallaInscripcion pi = new PantallaInscripcion();
		if(pi.esCancelado()) return;
		
		String ipTarget = pi.getIpTarget();
		String nombreSala = pi.getNombreSala();
		String nombre = pi.getNombreJug();
		nombre=nombre.toLowerCase();
		if(ipTarget.contains(" ") ||nombreSala.contains(" ") ||nombre.contains(" "))
		{
			JOptionPane.showMessageDialog(
					this, 
					"No debe haber espacios", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}//End of if
		
		String link = "http://"+ipTarget+":8080/SnakeRest/SnakeMRV/game/inscribirse?sala="+nombreSala+"&nombre="+nombre;
		String respuesta="";
		try {
			respuesta=Utils.peticion_throws(link);
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(
					this, 
					"Error de servidor", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(respuesta.contains("ERROR"))
		{
			JOptionPane.showMessageDialog(
					this, 
					respuesta, 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(
				this, 
				respuesta, 
				"",
				JOptionPane.INFORMATION_MESSAGE);
		Object o = new Object();
		PantallaEsperando pe = new PantallaEsperando(ipTarget, o);
		try 
		{
			synchronized(o)
			{
				o.wait();
			}
		} catch (Exception e) 
		{
			JOptionPane.showMessageDialog(
					this, 
					"Error esperando al inicio de la partida", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(pe.esCancelado()) return;
		new PantallaJuego(nombre, ipTarget);
	}//End of unirseAUnaSala

	/**
	 * Hace la peticion de inicio del servidor, crearSala y comprueba errores para luego mostrar una respuesta
	 */
	private void crearNuevaSala() {
		
		this.setVisible(false);
		
		String ipNueva = (String)JOptionPane.showInputDialog(this,"IP del servidor: ",
				"", JOptionPane.PLAIN_MESSAGE, null, null, "localhost");
		if(ipNueva==null) return;
		String link = "http://"+ipNueva+":8080/SnakeRest/SnakeMRV/game/inicioDelServidor";
		String respuesta="";
		try {
			respuesta=Utils.peticion_throws(link);
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
			
		String nJugs = (String)JOptionPane.showInputDialog(this,"N�mero de jugadores: ",
				"", JOptionPane.PLAIN_MESSAGE, null, null, "");
		if(nJugs==null) return;
		int nj = 0;
		try
		{
			nj=Integer.parseInt(nJugs);
			if(nj<1)
			{
				JOptionPane.showMessageDialog(
						this, 
						"El n�mero tiene que ser positivo", 
						"WARNING",
						JOptionPane.WARNING_MESSAGE);
				this.setVisible(true);
				return;
			}
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(
					this, 
					"Tiene que ser un n�mero", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.setVisible(true);
			return;
		}
		//SE LLAMA A CREAR SALA Y SE COMPRUEBAN ERRORES
		link = "http://"+ipNueva+":8080/SnakeRest/SnakeMRV/game/crearSala?nJugadores="+nj;
		try {
			respuesta=Utils.peticion_throws(link);
		}catch(Exception e)
		{
			System.out.println(e);
			System.out.flush();
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
