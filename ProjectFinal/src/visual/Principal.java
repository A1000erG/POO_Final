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
import java.awt.geom.RoundRectangle2D;
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
					Principal frame = new Principal(1,"");
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
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
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
		if(mode==0) {
			btnRegPaciente.setText("Registrar Paciente");
		}else {
			btnRegPaciente.setText("New button");
		}
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
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mode==0) {
					try {
						RegDoctor regDoctor = new RegDoctor(mode, idUser);
						regDoctor.setVisible(true);
					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}
		});
		btnRegDoctor.setForeground(Color.WHITE);
		btnRegDoctor.setFont(buttonFont);
		if(mode==0) {
			btnRegDoctor.setText("Registrar Doctor");
		}else {
			btnRegDoctor.setText("New button");
		}
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
		if(mode==0) {
			btnConsultas.setText("Registrar Cita");
		}else {
			btnConsultas.setText("New button");
		}
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
		if(mode==0) {
			btnReportes.setText("Reportes");
		}else {
			btnReportes.setText("New button");
		}
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
		
		JLabel lblRolUser = new JLabel("");
		lblRolUser.setForeground(Color.WHITE);
		lblRolUser.setFont(normalUse);
		if(mode==0) {
			lblRolUser.setText("Administrativo");
		}else {
			lblRolUser.setText("Doctor");
		}
		lblRolUser.setBounds(846, 62, 119, 14);
		infoUserPanel.add(lblRolUser);
		
		JLabel lblFotoUser = new JLabel("");
		lblFotoUser.setBounds(1020, 11, 90, 90);
		lblFotoUser.setIcon(new ImageIcon("Recursos/Imagenes/useResi.png"));
		infoUserPanel.add(lblFotoUser);
		
		ImageIcon doctoraIcon = new ImageIcon(getClass().getResource("/Imagenes/doctora.png"));
		Image doctoraRedim = doctoraIcon.getImage().getScaledInstance(230, 250, Image.SCALE_SMOOTH);
		JLabel lblDoctora = new JLabel(new ImageIcon(doctoraRedim));
		lblDoctora.setBounds(200,127,230,250);
		lblDoctora.setVisible(true);
		infoPanel.add(lblDoctora);
		
		JPanel welcomePanel = new JPanel(){
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
			    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
			}
		};
		welcomePanel.setBackground(Color.WHITE);
		welcomePanel.setBounds(57, 177, 1014, 200);
		if(mode==0) {
			welcomePanel.setVisible(false);
		}else {
			welcomePanel.setVisible(true);
		}
		welcomePanel.setLayout(null);
		//Aniadiendo imagenes del banner al panel
		ImageIcon bannerIcon = new ImageIcon(getClass().getResource("/Imagenes/doctorBanner.png"));
		//Escalando imagen
		Image bannerEscalado = bannerIcon.getImage().getScaledInstance(1041, 200, Image.SCALE_SMOOTH);
		JLabel lblBanner = new JLabel(bannerIcon) {
			 @Override
	            protected void paintComponent(Graphics g) {
	                Graphics2D redondeo = (Graphics2D) g.create();
	                redondeo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	                // Aca se define el radio del redondeo que se va a aplicar, 30px para este caso, este paso es opcional
	                int radio = 30; 
	                // Crear máscara de la forma que quiero que tome el JLabel
	                redondeo.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radio, radio));
	                // Pintar la imagen original dentro de la máscara hecha
	                super.paintComponent(redondeo);
	                redondeo.dispose();
	            }
		};
		lblBanner.setBounds(0, 0, 1014, 200);
		lblBanner.setIcon(new ImageIcon(bannerEscalado));
		welcomePanel.add(lblBanner);
		infoPanel.add(welcomePanel);
		
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
		if(mode==0) {
			cantEnfermPanel.setVisible(true);
		}else {
			cantEnfermPanel.setVisible(false);
		}
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
		if(mode==0) {
			cantVacunasPanel.setVisible(true);
		}else {
			cantVacunasPanel.setVisible(false);
		}
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
		if(mode==0) {
			cantCitasHoyPanel.setVisible(true);
		}else {
			cantCitasHoyPanel.setVisible(false);
		}
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
		if(mode==0) {
			barGraphSickPanel.setVisible(true);
		}else {
			barGraphSickPanel.setVisible(false);
		}
		infoPanel.add(barGraphSickPanel);
		barGraphSickPanel.setLayout(null);
	}
}
