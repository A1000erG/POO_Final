package visual;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Utilidades.FuenteUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Principal extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private static Color paleteBlue = new Color(55,65,81);
	private static Color paleteDarkGreen = new Color(22,163, 74);
	private static Color paleteRareWhite = new Color(247, 250, 252);
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal(0,"");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana. Por favor, reinicie la aplicación", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param tipo 
	 */

	public Principal(int mode, String idUser) {
		getToolkit().getScreenSize();
		setResizable(false);

		setTitle("Ventana principal");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBounds(0, 0, 240, 701);
		optionPanel.setBackground(paleteBlue);
		contentPane.add(optionPanel);
		optionPanel.setLayout(null);
		
		JLabel lbLogo = new JLabel("");
		lbLogo.setBounds(82, 26, 76, 76);
		lbLogo.setIcon(new ImageIcon("Recursos/Imagenes/logoP.png"));
		optionPanel.add(lbLogo);
		
		JButton btnRegPaciente = new JButton("New button");	
		//Efecto de cambio de de color de botones
		btnRegPaciente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRegPaciente.setBackground(paleteDarkGreen);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRegPaciente.setBackground(paleteBlue);
			}
		});
		btnRegPaciente.setForeground(Color.WHITE);
		btnRegPaciente.setBounds(0, 177, 240, 47);
		btnRegPaciente.setBackground(paleteBlue);
		btnRegPaciente.setBorderPainted(false);
		btnRegPaciente.setFocusPainted(false);
		optionPanel.add(btnRegPaciente);
		
		JButton btnRegDoctor = new JButton("New button");
		//Efecto de cambio de de color de botones
		btnRegDoctor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRegDoctor.setBackground(paleteDarkGreen);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRegDoctor.setBackground(paleteBlue);
			}
		});
		btnRegDoctor.setForeground(Color.WHITE);
		btnRegDoctor.setBounds(0, 224, 240, 47);
		btnRegDoctor.setBackground(paleteBlue);
		btnRegDoctor.setBorderPainted(false);
		btnRegDoctor.setFocusPainted(false);
		optionPanel.add(btnRegDoctor);
		
		JButton btnConsultas = new JButton("New button");
		//Efecto de cambio de de color de botones
		btnConsultas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnConsultas.setBackground(paleteDarkGreen);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnConsultas.setBackground(paleteBlue);
			}
		});
		btnConsultas.setForeground(Color.WHITE);
		btnConsultas.setBounds(0, 271, 240, 47);
		btnConsultas.setBackground(paleteBlue);
		btnConsultas.setBorderPainted(false);
		btnConsultas.setFocusPainted(false);
		optionPanel.add(btnConsultas);
		
		JButton btnReportes = new JButton("New button");
		//Efecto de cambio de de color de botones
		btnReportes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnReportes.setBackground(paleteDarkGreen);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnReportes.setBackground(paleteBlue);
			}
		});
		btnReportes.setForeground(Color.WHITE);
		btnReportes.setBounds(0, 318, 240, 47);
		btnReportes.setBackground(paleteBlue); //las variables palete corresponden a instancias 
		//de clase Color con los colores de la paleta utilizada para el programa
		btnReportes.setBorderPainted(false);
		btnReportes.setFocusPainted(false);
		optionPanel.add(btnReportes);
		
		JPanel bkgPanel = new JPanel();
		bkgPanel.setBackground(Color.LIGHT_GRAY);
		bkgPanel.setBounds(239, 0, 1131, 701);
		contentPane.add(bkgPanel);
		bkgPanel.setLayout(null);
		
		JPanel infoUserPanel = new JPanel(); 
		infoUserPanel.setBackground(paleteRareWhite);
		infoUserPanel.setBounds(0, 0, 1131, 120);
		bkgPanel.add(infoUserPanel);
		infoUserPanel.setLayout(null);
		JLabel lblNombreUser = new JLabel("Nombre Usuario");
		/*if(mode==0) {
			lblNombreUser.setText(Clinica.getInstance().getDoctorPorUsuario(idUser).getNombre());
		}*/
		lblNombreUser.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 13f));
		lblNombreUser.setBounds(846, 37, 153, 14);
		infoUserPanel.add(lblNombreUser);
		
		JLabel lblRolUser = new JLabel("Rol (doctor o admin)");
		lblRolUser.setBounds(846, 62, 119, 14);
		infoUserPanel.add(lblRolUser);
		
		JLabel lblFotoUser = new JLabel("");
		lblFotoUser.setBounds(1020, 11, 90, 90);
		lblFotoUser.setIcon(new ImageIcon("Recursos/Imagenes/useResi.png"));
		infoUserPanel.add(lblFotoUser);
		
		JPanel cantEnfermPanel = new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
			    Graphics2D g2 = (Graphics2D) g;
			    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
			                       RenderingHints.VALUE_ANTIALIAS_ON);
			    g2.setColor(getBackground());
			    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			}
		};
		cantEnfermPanel.setBackground(Color.WHITE);
		cantEnfermPanel.setBounds(57, 177, 300, 130);
		bkgPanel.add(cantEnfermPanel);
		cantEnfermPanel.setLayout(null);
		
		ImageIcon enfermedadIcon = new ImageIcon(getClass().getResource("/Imagenes/thermometer.png"));
		//Escalando imagen
		Image enfermedadEscalada = enfermedadIcon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
		JLabel lblEnfermedades = new JLabel(new ImageIcon(enfermedadEscalada));
		lblEnfermedades.setBounds(171, 11, 108, 108);
		cantEnfermPanel.add(lblEnfermedades);
		
		JLabel lblDescripEnf = new JLabel("Enfermedades Controladas");
		lblDescripEnf.setBounds(10, 105, 151, 14);
		cantEnfermPanel.add(lblDescripEnf);
		
		JLabel lblCountEnf = new JLabel("25");
		lblCountEnf.setBounds(10, 11, 80, 50);
		cantEnfermPanel.add(lblCountEnf);
		
		JPanel cantVacunasPanel = new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
			    Graphics2D g2 = (Graphics2D) g;
			    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
			                       RenderingHints.VALUE_ANTIALIAS_ON);
			    g2.setColor(getBackground());
			    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			}
		};
		cantVacunasPanel.setBackground(Color.WHITE);
		cantVacunasPanel.setBounds(414, 177, 300, 130);
		bkgPanel.add(cantVacunasPanel);
		cantVacunasPanel.setLayout(null);
		
		ImageIcon vacunaIcon = new ImageIcon(getClass().getResource("/Imagenes/syringe.png"));
		//Escalando imagen
		Image vacunaEscalada = vacunaIcon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
		JLabel lblVacuna = new JLabel(new ImageIcon(vacunaEscalada));
		lblVacuna.setBounds(171, 11, 108, 108);
		cantVacunasPanel.add(lblVacuna);
		
		JLabel lblNewLabel = new JLabel("Vacunas Existenetes");
		lblNewLabel.setBounds(10, 105, 151, 14);
		cantVacunasPanel.add(lblNewLabel);
		
		JLabel lblCountVac = new JLabel("13");
		lblCountVac.setBounds(10, 11, 80, 50);
		cantVacunasPanel.add(lblCountVac);
		
		JPanel cantCitasHoyPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
			    Graphics2D g2 = (Graphics2D) g;
			    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
			                       RenderingHints.VALUE_ANTIALIAS_ON);
			    g2.setColor(getBackground());
			    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			}
		};
		cantCitasHoyPanel.setBackground(Color.WHITE);
		cantCitasHoyPanel.setBounds(771, 177, 300, 130);
		
		bkgPanel.add(cantCitasHoyPanel);
		cantCitasHoyPanel.setLayout(null);
		
		ImageIcon citasHoyIcon = new ImageIcon(getClass().getResource("/Imagenes/stethoscope.png"));
		//Escalando imagen
		Image citasEscalada = citasHoyIcon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
		JLabel lblCitas = new JLabel(new ImageIcon(citasEscalada));
		lblCitas.setBounds(171, 11, 108, 108);
		cantCitasHoyPanel.add(lblCitas);
		
		JLabel lblNewLabel_1 = new JLabel("Citas para hoy");
		lblNewLabel_1.setBounds(10, 105, 151, 14);
		cantCitasHoyPanel.add(lblNewLabel_1);
		
		JLabel lblCountCitas = new JLabel("34");
		lblCountCitas.setBounds(10, 11, 80, 50);
		cantCitasHoyPanel.add(lblCountCitas);
		
		JPanel barGraphSickPanel = new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
			    Graphics2D g2 = (Graphics2D) g;
			    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
			                       RenderingHints.VALUE_ANTIALIAS_ON);
			    g2.setColor(getBackground());
			    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			}
		};
		barGraphSickPanel.setBackground(Color.WHITE);
		barGraphSickPanel.setBounds(57, 355, 1014, 318);
		bkgPanel.add(barGraphSickPanel);
		barGraphSickPanel.setLayout(null);
	}
}
