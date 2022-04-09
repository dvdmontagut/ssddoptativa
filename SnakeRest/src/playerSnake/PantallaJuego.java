package playerSnake;

import java.awt.Font;
import java.awt.*;
import java.awt.event.*;
import java.beans.Visibility;

import javax.swing.*;

import utils.Direccion;

public class PantallaJuego extends JDialog implements ActionListener {

	private JButton botonA, botonS, botonD, botonW, abandonar;
	private JTextArea juegoTA;
	private JPanel jpAbajo, jpDirecs, jpTA;
	private String nombre;
	
	public PantallaJuego(String nombre)
	{
		setModal(true);
		this.nombre=nombre;

		setBounds(400,400,500,500);
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
			 	botonA=new JButton("A"); botonA.addActionListener(this); botonA.setMaximumSize(new Dimension(30,30));
			 	botonS=new JButton("S"); botonS.addActionListener(this); botonS.setMaximumSize(new Dimension(30,30));
			 	botonD=new JButton("D"); botonD.addActionListener(this); botonD.setMaximumSize(new Dimension(30,30));
			 	botonW=new JButton("W"); botonW.addActionListener(this); botonW.setMaximumSize(new Dimension(30,30));
			 	jpDirecs.add(new JButton());
			 	jpDirecs.add(botonW);
			 	jpDirecs.add(new JButton());
			 	jpDirecs.add(botonA);
			 	jpDirecs.add(new JButton());
			 	jpDirecs.add(botonD);
			 	jpDirecs.add(new JButton());
			 	jpDirecs.add(botonS);
			 	jpDirecs.add(new JButton());
		 	abandonar=new JButton("Abandonar");
		 jpAbajo.add(jpDirecs);
		 jpAbajo.add(new JSeparator());
		 jpAbajo.add(abandonar);
		 jpTA.add(juegoTA);
		 add(jpTA);
		 add(jpAbajo);
		 
		 setVisible(true);
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
		}
	}

	private void abandonar() {
		// TODO Auto-generated method stub
		
	}

	private void mandarDireccion(Direccion izquierda) {
		// TODO Auto-generated method stub
		
	}
	
}

