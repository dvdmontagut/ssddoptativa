package playerSnake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import utils.*;

public class PantallaInscripcion extends JDialog implements ActionListener
{
	private JTextField ipTF, salaTF, nbTF;
	private JLabel ipL, salaL, nbL;
	private JButton ok, volver;
	private JPanel jp1,jp2,jp11,jp12,jp13,jp21,jp22;
	private boolean bien;
	String nombreSala, ipTarget, nombreJug;

	public PantallaInscripcion()
	{
		setModal(true);
		bien=false;
		setBounds(400,400,500,130);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		

		jp1 = new JPanel();
		jp1.setLayout(new GridLayout(0,2));
		jp1.setMaximumSize(new Dimension(500,40));
		jp1.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			jp11 = new JPanel();
			jp11.setLayout(new BoxLayout(jp11, BoxLayout.Y_AXIS));
				salaL = new JLabel("ID del nuevo proceso: ");
				ipTF = new JTextField();
				jp11.add(salaL);
				jp11.add(ipTF);
			jp12 = new JPanel();
			jp12.setLayout(new BoxLayout(jp12, BoxLayout.Y_AXIS));
				ipL = new JLabel ("Seleccione la máquina: ");
				salaTF = new JTextField();
				jp12.add(ipL);
				jp12.add(salaTF);
			jp13 = new JPanel();
			jp13.setLayout(new BoxLayout(jp13, BoxLayout.Y_AXIS));
			 	nbL = new JLabel("Escriba su nombre (1 letra): ");
			 	nbTF = new JTextField();
			 	jp13.add(nbL);
			 	jp13.add(nbTF);
			jp1.add(jp11);
			jp1.add(jp12);
			jp1.add(jp13);
		add(jp1);
		
		jp2 = new JPanel();
		jp2.setLayout(new BoxLayout(jp2, BoxLayout.X_AXIS));
		jp2.setMaximumSize(new Dimension(300,30));
			jp21 = new JPanel();
				ok = new JButton("OK");
				ok.addActionListener(this);
				jp21.add(ok);
			jp22 = new JPanel();
				volver = new JButton("Volver");
				volver.addActionListener(this);
				jp22.add(volver);
			jp2.add(jp21);
			jp2.add(jp22);
		add(jp2);

		
		
		
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object fuente = e.getSource();
		int opcion = (fuente==ok)?1:(fuente==volver)?2:0;
		switch(opcion)
		{
			case 1: 
				bien =  true;
				nombreSala = salaTF.getText().toString();
				ipTarget=ipTF.getText().toString();
				nombreJug=nbTF.getText().toString();
				this.dispose(); 
				break;
			case 2: 
				this.dispose(); 
				break;
		}
		
	}
	public String getNombreSala() {
		return nombreSala;
	}
	public String getIpTarget() {
		return ipTarget;
	}
	public String getNombreJug() {
		return nombreJug;
	}
	public boolean esCancelado() {return !bien;}

}
