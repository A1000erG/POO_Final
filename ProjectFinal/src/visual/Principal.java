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
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Font;

public class Principal extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private static Color paleteBlue = new Color(55,65,81);
	private static Color paleteGreen = new Color(22, 163, 74);
	private static Color paleteDarkGreen = new Color(18, 140, 64);
	private static Color paleteRareWhite = new Color(247, 250, 252);
	private static Font indicativeNumber = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 20f);
	private static Font normalUse = FuenteUtil.cargarFuente("/Fuentes/Roboto-Light.ttf", 11f);
	private static Font nameUser = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 13f);
	private static Font buttonFont = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 12f);
	private static JPanel infoPanel;
	private JPanel bkgPanel;
	
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

		setTitle("Compile Salud");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);		
		
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(820, 3100));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		bkgPanel = new JPanel();
		bkgPanel.setBackground(Color.LIGHT_GRAY);
		contentPane.add(bkgPanel, BorderLayout.CENTER);
		bkgPanel.setLayout(null);
		
		infoPanel = new JPanel();
		infoPanel.setSize(1121, 738);
		infoPanel.setLocation(240, 0);
		infoPanel.setOpaque(false);
		infoPanel.setLayout(null);
		bkgPanel.add(infoPanel);
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBackground(paleteGreen);
		optionPanel.setLayout(null);
		optionPanel.setBounds(0, 0, 240, 738);
		bkgPanel.add(optionPanel);
		//bkgPanel.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Bold.ttf", 16f));
		
		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
		//Escalando imagen
		Image logoEscalado = logoIcon.getImage().getScaledInstance(76, 76, Image.SCALE_SMOOTH);
		JLabel lbLogo = new JLabel(new ImageIcon(logoEscalado));
		lbLogo.setBounds(82, 26, 76, 76);
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
				btnRegPaciente.setBackground(paleteGreen);
			}
		});
		btnRegPaciente.setForeground(Color.WHITE);
		btnRegPaciente.setFont(buttonFont);
		btnRegPaciente.setBounds(0, 177, 240, 47);
		btnRegPaciente.setBackground(paleteGreen);
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
				btnRegDoctor.setBackground(paleteGreen);
			}
		});
		btnRegDoctor.setForeground(Color.WHITE);
		btnRegDoctor.setFont(buttonFont);
		btnRegDoctor.setBounds(0, 224, 240, 47);
		btnRegDoctor.setBackground(paleteGreen);
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
				btnConsultas.setBackground(paleteGreen);
			}
		});
		btnConsultas.setForeground(Color.WHITE);
		btnConsultas.setFont(buttonFont);
		btnConsultas.setBounds(0, 271, 240, 47);
		btnConsultas.setBackground(paleteGreen);
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
				btnReportes.setBackground(paleteGreen);
			}
		});
		btnReportes.setForeground(Color.WHITE);
		btnReportes.setFont(buttonFont);
		btnReportes.setBounds(0, 318, 240, 47);
		btnReportes.setBackground(paleteGreen); //las variables palete corresponden a instancias 
		//de clase Color con los colores de la paleta utilizada para el programa
		btnReportes.setBorderPainted(false);
		btnReportes.setFocusPainted(false);
		optionPanel.add(btnReportes);
		
		JPanel infoUserPanel = new JPanel(); 
		infoUserPanel.setBackground(paleteDarkGreen);
		infoUserPanel.setBounds(0, 0, 1131, 120);
		infoPanel.add(infoUserPanel);
		infoUserPanel.setLayout(null);
		JLabel lblNombreUser = new JLabel("Nombre Usuario");
		lblNombreUser.setForeground(Color.WHITE);
		/*if(mode==0) {
			lblNombreUser.setText(Clinica.getInstance().getDoctorPorUsuario(idUser).getNombre());
		}*/
		lblNombreUser.setFont(nameUser);
		lblNombreUser.setBounds(846, 37, 153, 14);
		infoUserPanel.add(lblNombreUser);
		
		JLabel lblRolUser = new JLabel("Rol (doctor o admin)");
		lblRolUser.setForeground(Color.WHITE);
		lblRolUser.setFont(normalUse);
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
		infoPanel.add(cantEnfermPanel);
		cantEnfermPanel.setLayout(null);
		
		ImageIcon enfermedadIcon = new ImageIcon(getClass().getResource("/Imagenes/thermometer.png"));
		//Escalando imagen
		Image enfermedadEscalada = enfermedadIcon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
		JLabel lblEnfermedades = new JLabel(new ImageIcon(enfermedadEscalada));
		lblEnfermedades.setBounds(171, 11, 108, 108);
		cantEnfermPanel.add(lblEnfermedades);
		
		JLabel lblDescripEnf = new JLabel("Enfermedades Controladas");
		lblDescripEnf.setFont(normalUse);
		lblDescripEnf.setBounds(10, 105, 151, 14);
		lblDescripEnf.setFont(normalUse);
		cantEnfermPanel.add(lblDescripEnf);
		
		JLabel lblCountEnf = new JLabel("25");
		lblCountEnf.setFont(indicativeNumber);
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
		infoPanel.add(cantVacunasPanel);
		cantVacunasPanel.setLayout(null);
		
		ImageIcon vacunaIcon = new ImageIcon(getClass().getResource("/Imagenes/syringe.png"));
		//Escalando imagen
		Image vacunaEscalada = vacunaIcon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
		JLabel lblVacuna = new JLabel(new ImageIcon(vacunaEscalada));
		lblVacuna.setBounds(171, 11, 108, 108);
		cantVacunasPanel.add(lblVacuna);
		
		JLabel lblDescripVacuna = new JLabel("Vacunas Existenetes");
		lblDescripVacuna.setFont(normalUse);
		lblDescripVacuna.setBounds(10, 105, 151, 14);
		cantVacunasPanel.add(lblDescripVacuna);
		
		JLabel lblCountVac = new JLabel("13");
		lblCountVac.setFont(indicativeNumber);
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
		
		infoPanel.add(cantCitasHoyPanel);
		cantCitasHoyPanel.setLayout(null);
		
		ImageIcon citasHoyIcon = new ImageIcon(getClass().getResource("/Imagenes/stethoscope.png"));
		//Escalando imagen
		Image citasEscalada = citasHoyIcon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
		JLabel lblCitas = new JLabel(new ImageIcon(citasEscalada));
		lblCitas.setBounds(171, 11, 108, 108);
		cantCitasHoyPanel.add(lblCitas);
		
		JLabel lblDescripCitas = new JLabel("Citas para hoy");
		lblDescripCitas.setFont(normalUse);
		lblDescripCitas.setBounds(10, 105, 151, 14);
		cantCitasHoyPanel.add(lblDescripCitas);
		
		JLabel lblCountCitas = new JLabel("34");
		lblCountCitas.setFont(indicativeNumber);
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
		infoPanel.add(barGraphSickPanel);
		barGraphSickPanel.setLayout(null);
	}
}
