package playerSnake;

import java.awt.Font;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import utils.Direccion;
import utils.Utils;

public class PantallaJuego extends JDialog implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	
	private JButton botonA, botonS, botonD, botonW, abandonar;
	private JTextArea juegoTA;
	private JPanel jpAbajo, jpDirecs, jpTA;
	private String nombre, ip;
	private Timer t;
	
	public PantallaJuego(String nombre, String ip)
	{
		this.nombre=nombre;
		this.ip=ip;
		

		setBounds(400,400,700,700);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		jpTA = new JPanel();
		jpTA.setLayout(new BoxLayout(jpTA, BoxLayout.Y_AXIS));
		jpTA.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		juegoTA=new JTextArea();
		juegoTA.setEditable(false);
		juegoTA.setFont(new Font(Font.MONOSPACED, Font.PLAIN,12));
		juegoTA.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		jpAbajo=new JPanel();
		jpAbajo.setLayout(new BoxLayout(jpAbajo, BoxLayout.X_AXIS));
		 	jpDirecs = new JPanel();
		 	jpDirecs.setLayout(new GridLayout(3,3));
		 	jpDirecs.setMaximumSize(new Dimension(90,90));
			 	botonA=new JButton("<"); 	botonA.addActionListener(this); botonA.setMaximumSize(new Dimension(30,30));
			 	botonS=new JButton("V"); 	botonS.addActionListener(this); botonS.setMaximumSize(new Dimension(30,30));
			 	botonD=new JButton(">"); 	botonD.addActionListener(this); botonD.setMaximumSize(new Dimension(30,30));
			 	botonW=new JButton("/\\");	botonW.addActionListener(this); botonW.setMaximumSize(new Dimension(30,30));
			 	jpDirecs.add(new JButton());
			 	jpDirecs.add(botonW);
			 	jpDirecs.add(new JButton());
			 	jpDirecs.add(botonA);
			 	jpDirecs.add(new JButton(nombre.toUpperCase()));
			 	jpDirecs.add(botonD);
			 	jpDirecs.add(new JButton());
			 	jpDirecs.add(botonS);
			 	jpDirecs.add(new JButton());
		 	abandonar=new JButton("Abandonar");
		 	abandonar.addActionListener(this);
		 jpAbajo.add(jpDirecs);
		 jpAbajo.add(new JSeparator());
		 jpAbajo.add(abandonar);
		 jpTA.add(juegoTA);
		 add(jpTA);
		 add(jpAbajo);
		 setVisible(true);
		String link = "http://"+ip+":8080/SnakeRest/SnakeMRV/game/comenzarPartida";
		try{
			Utils.peticion_throws(link, Utils.GET);
		}catch(Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(
					this, 
					"Error comenzando la partida", 
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		t=new Timer(1000,this);
		t.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object fuente = e.getSource();
		int opcion = (fuente==botonA)?1:(fuente==botonS)?2:(fuente==botonD)?3:(fuente==botonW)?4:(fuente==abandonar)?5:0;
		switch(opcion)
		{
			case 1: mandarDireccion(Direccion.IZQUIERDA); break;
			case 2: mandarDireccion(Direccion.ABAJO); break;
			case 3: mandarDireccion(Direccion.DERECHA); break;
			case 4: mandarDireccion(Direccion.ARRIBA); break;
			case 5: abandonar(); break;
			case 0: pintarTablero(); break;
		}
	}
	
	private void pintarTablero()
	{
		String link = "http://"+ip+":8080/SnakeRest/SnakeMRV/game/verTablero";
		String respuesta=Utils.peticion(link, Utils.GET);
		juegoTA.setText(Utils.factoryTablero(respuesta));
		t=new Timer(Utils.TIEMPO_ENTRE_TURNOS,this);
		t.start();
	}

	private void abandonar()
	{
		String link = "http://"+ip+":8080/SnakeRest/SnakeMRV/game/abandonar";
		Utils.peticion(link, Utils.POST);
		this.dispose();
	}

	private void mandarDireccion(Direccion d)
	{
		String link = "http://"+ip+":8080/SnakeRest/SnakeMRV/game/cambioDireccion?nombre="+nombre+"&direccion="+d.toString().toUpperCase();
		Utils.peticion(link, Utils.POST);
	}
	
}
