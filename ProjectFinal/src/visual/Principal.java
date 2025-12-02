package visual;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import Utilidades.FuenteUtil;
import logico.Cita;
import logico.Clinica;
import logico.Enfermedad;
import sun.util.resources.LocaleData;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Principal extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private static Color paleteGreen = new Color(22, 163, 74);
	private static Color paleteDarkGreen = new Color(18, 140, 64);
	private static Color paleteBeautyBlu = new Color(21, 129, 191);
	private static Font fuenteTituloGraph = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 22f);
	private static Font fuenteEjesGraph = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f);
	private static Font fuenteDatosGraph  = FuenteUtil.cargarFuente("/Fuentes/Roboto-Light.ttf", 12f);
	private static Font indicativeNumber = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 20f);
	private static Font normalUse = FuenteUtil.cargarFuente("/Fuentes/Roboto-Light.ttf", 11f);
	private static Font nameUser = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 13f);
	private static Font buttonFont = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 12f);
	private static JPanel infoPanel;
	private static JPanel infoListPanel; 
	private static JPanel cantEnfermPanel;
	private static JPanel cantVacunasPanel;
	private static JPanel cantCitasHoyPanel;
	private static JPanel barGraphSickPanel;
	private static JPanel doctorsListPanel;	
	private static JPanel adminListPanel;	
	private static JPanel vacunaListPanel;	
	private static JPanel pacientesListPanel;
	private static Clinica clinic = Clinica.getInstance();
	private JPanel bkgPanel;
	private JLabel lblDoctor;

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
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);		

		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas salir de la aplicación?", 
						"Confirmar Cierre", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(opcion == JOptionPane.YES_OPTION) {
					clinic.guardarDatosLocal();
					dispose();
					System.exit(0);
				}
			}
		});
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(820, 3100));
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		bkgPanel = new JPanel();
		bkgPanel.setBackground(new Color(211,211,211));
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

		JButton btnRegAdmin = new JButton("New button");	
		//Efecto de cambio de de color de botones
		btnRegAdmin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRegAdmin.setBackground(paleteDarkGreen);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRegAdmin.setBackground(paleteGreen);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mode==0) {
					try {
						RegAdmin regAdmin = new RegAdmin(mode, idUser);
						regAdmin.setVisible(true);
					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
					}
				}else {

				}
			}
		});
		btnRegAdmin.setForeground(Color.WHITE);
		btnRegAdmin.setFont(buttonFont);
		if(mode==0) {
			btnRegAdmin.setText("Registrar Admin");
		}else {
			btnRegAdmin.setText("Citas");
		}
		btnRegAdmin.setBounds(0, 177, 240, 47);
		btnRegAdmin.setBackground(paleteGreen);
		btnRegAdmin.setBorderPainted(false);
		btnRegAdmin.setFocusPainted(false);
		optionPanel.add(btnRegAdmin);

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
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mode==0) {
					try {
						RegCita regCita = new RegCita();
						regCita.setVisible(true);
					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
					}

				}
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
			btnReportes.setVisible(false);
			btnReportes.setText("New button");
		}
		btnReportes.setBounds(0, 318, 240, 47);
		btnReportes.setBackground(paleteGreen); //las variables palete corresponden a instancias 
		//de clase Color con los colores de la paleta utilizada para el programa
		btnReportes.setBorderPainted(false);
		btnReportes.setFocusPainted(false);
		optionPanel.add(btnReportes);

		JButton btnListados = new JButton("Listados");
		//Efecto de cambio de de color de botones
		btnListados.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnListados.setBackground(paleteDarkGreen);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnListados.setBackground(paleteGreen);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					cantEnfermPanel.setVisible(false);
					cantVacunasPanel.setVisible(false);
					cantCitasHoyPanel.setVisible(false);
					barGraphSickPanel.setVisible(false);
					infoListPanel.setVisible(true);
					adminListPanel.setVisible(true);
					vacunaListPanel.setVisible(true);
					doctorsListPanel.setVisible(true);
					pacientesListPanel.setVisible(true);
					lblDoctor.setVisible(true);
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error inesperado al mostrar la ventana", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnListados.setForeground(Color.WHITE);
		btnListados.setFont(buttonFont);
		if(mode==0) {
			btnListados.setVisible(true);
		}else {
			btnListados.setVisible(false);
		}
		btnListados.setBounds(0,365,240,47);
		btnListados.setBackground(paleteGreen);
		btnListados.setBorderPainted(false);
		btnListados.setFocusPainted(false);
		optionPanel.add(btnListados);

		JPanel infoUserPanel = new JPanel(); 
		infoUserPanel.setBackground(paleteDarkGreen);
		infoUserPanel.setBounds(0, 0, 1131, 120);
		infoPanel.add(infoUserPanel);
		infoUserPanel.setLayout(null);
		
		String idCod = "";
		String nombreUsuario = "";
		if(mode==0) {
			if(Clinica.getInstance().getAdministrativos().size()>0) {
				nombreUsuario = Clinica.getInstance().getAdministrativoPorUsuario(idUser).getNombre();
				idCod = String.format("ID: A-%03d", Clinica.getInstance().getAdministrativoPorUsuario(idUser).getIdAdmin()); 
			}else nombreUsuario = "Admin";	
		}else {
			if(Clinica.getInstance().getDoctores().size()>0) {
				nombreUsuario = Clinica.getInstance().getDoctorPorUsuario(idUser).getNombre();
				idCod = String.format("ID: D-%03d", Clinica.getInstance().getDoctorPorUsuario(idUser).getIdDoctor());
			}else nombreUsuario = "Doctor";
		}
		JLabel lblNombreUser = new JLabel(nombreUsuario);
		lblNombreUser.setForeground(Color.WHITE);
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
		
		String dirImagen = imagenEncontrada(idUser);
		ImageIcon imgUser;
		if(dirImagen.equalsIgnoreCase("")) {
			imgUser = new ImageIcon(getClass().getResource("/Imagenes/useResi.png"));
		}else {
			imgUser = new ImageIcon(idCod);
		}
		Image imgEscalada = imgUser.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JLabel lblFotoUser = new JLabel("");
		lblFotoUser.setBounds(1020, 11, 80, 80);
		lblFotoUser.setIcon(new ImageIcon(imgEscalada));
		infoUserPanel.add(lblFotoUser);

		//================================PANELES PARA DOCTORES====================================
		ImageIcon doctoraIcon = new ImageIcon(getClass().getResource("/Imagenes/doctora.png"));
		Image doctoraRedim = doctoraIcon.getImage().getScaledInstance(230, 250, Image.SCALE_SMOOTH);
		JLabel lblDoctora = new JLabel(new ImageIcon(doctoraRedim));
		lblDoctora.setBounds(200,127,230,250);
		if(mode==0) {
			lblDoctora.setVisible(false);
		}else {
			lblDoctora.setVisible(true);
		}
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

			private static final long serialVersionUID = 1L;

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

		//Panel inferior izquierdo
		JPanel listEnfPanel = new JPanel() {
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
		listEnfPanel.setBackground(Color.WHITE);
		listEnfPanel.setBounds(57, 425, 487, 280);
		if(mode==0) {
			listEnfPanel.setVisible(false);
		}else {
			listEnfPanel.setVisible(true);
		}
		listEnfPanel.setLayout(null);
		infoPanel.add(listEnfPanel);

		//Elementos del panel inferior izquierdo
		ImageIcon enfermedadIcon = new ImageIcon(getClass().getResource("/Imagenes/thermometer.png"));
		//Escalado
		Image enfEscala = enfermedadIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JLabel lblEnfIcon = new JLabel(new ImageIcon(enfEscala));
		lblEnfIcon.setBounds(397,10,80,80);
		listEnfPanel.add(lblEnfIcon);

		JLabel lblDesEnf = new JLabel("Enfermedades Controladas");
		lblDesEnf.setFont(normalUse);
		lblDesEnf.setBounds(10, 105, 151, 14);
		lblDesEnf.setFont(normalUse);
		listEnfPanel.add(lblDesEnf);

		JLabel lblCEnf = new JLabel("25");
		lblCEnf.setFont(indicativeNumber);
		lblCEnf.setBounds(10, 11, 80, 50);
		listEnfPanel.add(lblCEnf);

		//Panel inferior derecho
		JPanel citasHoyPanel = new JPanel() {
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
		citasHoyPanel.setBackground(Color.WHITE);
		citasHoyPanel.setBounds(584, 425, 487, 280);
		if(mode==0) {
			citasHoyPanel.setVisible(false);
		}else {
			citasHoyPanel.setVisible(true);
		}
		citasHoyPanel.setLayout(null);
		infoPanel.add(citasHoyPanel);

		//Elementos del panel inferior derecho
		ImageIcon citasHoyIcon = new ImageIcon(getClass().getResource("/Imagenes/stethoscope.png"));
		//Escalado
		Image citasEscala = citasHoyIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JLabel lblCitaIcon = new JLabel(new ImageIcon(citasEscala));
		lblCitaIcon.setBounds(397,10,80,80);
		citasHoyPanel.add(lblCitaIcon);



		//===================================PANELES PARA USUARIOS ADMINISTRATIVOS=================================
		cantEnfermPanel = new JPanel(){
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

		Integer cantEnf = Clinica.getInstance().getCatalogoEnfermedades().size();
		JLabel lblCountEnf = new JLabel(cantEnf.toString());
		lblCountEnf.setFont(indicativeNumber);
		lblCountEnf.setBounds(10, 11, 80, 50);
		cantEnfermPanel.add(lblCountEnf);

		cantVacunasPanel = new JPanel(){
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

		Integer cantVacunas = Clinica.getInstance().getVacunas().size();
		JLabel lblCountVac = new JLabel(cantVacunas.toString());
		lblCountVac.setFont(indicativeNumber);
		lblCountVac.setBounds(10, 11, 80, 50);
		cantVacunasPanel.add(lblCountVac);

		cantCitasHoyPanel = new JPanel() {
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

		//Escalando imagen
		Image citasEscalada = citasHoyIcon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
		JLabel lblCitas = new JLabel(new ImageIcon(citasEscalada));
		lblCitas.setBounds(171, 11, 108, 108);
		cantCitasHoyPanel.add(lblCitas);

		JLabel lblDescripCitas = new JLabel("Citas para hoy");
		lblDescripCitas.setFont(normalUse);
		lblDescripCitas.setBounds(10, 105, 151, 14);
		cantCitasHoyPanel.add(lblDescripCitas);

		LocalDate hoy = LocalDate.now();
		Integer cantCitasHoy = 0;
		for (Cita cita : Clinica.getInstance().getCitas()) {
			if(cita.getFecha()==hoy) cantCitasHoy++;
		}
		JLabel lblCountCitas = new JLabel(cantCitasHoy.toString());
		lblCountCitas.setFont(indicativeNumber);
		lblCountCitas.setBounds(10, 11, 80, 50);
		cantCitasHoyPanel.add(lblCountCitas);

		barGraphSickPanel = new JPanel(){
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

		DefaultCategoryDataset enfermedadesDataset = new DefaultCategoryDataset();
		int i = clinic.getCatalogoEnfermedades().size()-1;
		int maxEnfermedades = 5;
		if(clinic.getCatalogoEnfermedades().size() < 5) maxEnfermedades = i+1;
		JLabel noData = new JLabel("No hay data");
		if(clinic.getCatalogoEnfermedades().size() != 0) {
			for(Enfermedad e1 = clinic.getCatalogoEnfermedades().get(i); maxEnfermedades>0;i--,maxEnfermedades--) {
				enfermedadesDataset.addValue(1, "Pacientes", e1.getNombre());
			}
			JFreeChart chartEnfermedades = ChartFactory.createBarChart("Pacientes por Enfermedad", "Enfermedades", "Cantidad", enfermedadesDataset, PlotOrientation.VERTICAL, false, true, false);
			CategoryPlot ploteo = chartEnfermedades.getCategoryPlot();

			chartEnfermedades.setBackgroundPaint(Color.WHITE); 
			ploteo.setBackgroundPaint(Color.WHITE);  
			// Quitar lineas de fondo
			ploteo.setRangeGridlinesVisible(false);  
			ploteo.setDomainGridlinesVisible(false); 
			ploteo.setOutlineVisible(false); 

			BarRenderer render2D = (BarRenderer) ploteo.getRenderer();
			render2D.setSeriesPaint(0,new Color(21, 129, 191));
			render2D.setBarPainter(new StandardBarPainter());
			render2D.setMaximumBarWidth(0.15);
			//Modificando fuentes
			chartEnfermedades.getTitle().setFont(fuenteTituloGraph);
			CategoryAxis ejes = ploteo.getDomainAxis();
			ejes.setLabelFont(fuenteEjesGraph);
			ejes.setTickLabelFont(fuenteDatosGraph);
			// Mejorando el eje Y
			NumberAxis rangoEjes = (NumberAxis) ploteo.getRangeAxis();
			rangoEjes.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			ChartPanel chartPanel = new ChartPanel(chartEnfermedades);
			chartPanel.setBounds(30,5,954,308);			
			barGraphSickPanel.add(chartPanel);
		}else {
			noData.setBounds(30,5,954,308);
			noData.setVisible(true);
			barGraphSickPanel.add(noData);
		}
		
		//================PANELES PARA LISTADOS======================
		ImageIcon doctorIcon = new ImageIcon(getClass().getResource("/Imagenes/doctorPanel2.png"));
		Image doctorRedim = doctorIcon.getImage().getScaledInstance(300, 286, Image.SCALE_SMOOTH);
		lblDoctor = new JLabel(new ImageIcon(doctorRedim));
		lblDoctor.setBounds(680,115,300,286);
		lblDoctor.setVisible(false);
		infoPanel.add(lblDoctor);
		
		infoListPanel = new JPanel() {
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
		infoListPanel.setBackground(paleteBeautyBlu);
		infoListPanel.setBounds(57, 177, 1014, 225);
		infoListPanel.setVisible(false);
		infoListPanel.setLayout(null);
		infoPanel.add(infoListPanel);
		
		JLabel lblTitlePanel = new JLabel("Título del panel");
		lblTitlePanel.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 28f));
		lblTitlePanel.setForeground(Color.WHITE);
		lblTitlePanel.setBounds(40, 30, 300, 30);
		infoListPanel.add(lblTitlePanel);
		
		JLabel lblInfoPanel = new JLabel("Info del panel");
		lblInfoPanel.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 14f));
		lblInfoPanel.setForeground(Color.WHITE);
		lblInfoPanel.setBounds(45, 75, 100, 25);
		infoListPanel.add(lblInfoPanel);
		
		JButton btnVolver = new JButton(){
			/**
			 * 
			 */
			 private static final long serialVersionUID = 1L;

			    @Override
			    protected void paintComponent(Graphics g) {
			        Graphics2D g2 = (Graphics2D) g;
			        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);			        
			        g2.setColor(getBackground());
			        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
			        super.paintComponent(g);
			    }
		};
		btnVolver.setContentAreaFilled(false); 
		btnVolver.setFocusPainted(false); 
		btnVolver.setBorderPainted(false);
		btnVolver.setOpaque(false);
		btnVolver.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnVolver.setBackground(new Color(211,211,211));
				btnVolver.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnVolver.setBackground(Color.WHITE);
				btnVolver.setForeground(paleteBeautyBlu);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					cantEnfermPanel.setVisible(true);
					cantVacunasPanel.setVisible(true);
					cantCitasHoyPanel.setVisible(true);
					barGraphSickPanel.setVisible(true);
					infoListPanel.setVisible(false);
					adminListPanel.setVisible(false);
					vacunaListPanel.setVisible(false);
					doctorsListPanel.setVisible(false);
					pacientesListPanel.setVisible(false);
					lblDoctor.setVisible(false);
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error inesperado al mostrar la ventana", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnVolver.setText("Volver al menú principal");
		btnVolver.setFont(buttonFont);
		btnVolver.setForeground(paleteBeautyBlu);
		btnVolver.setBackground(Color.WHITE);
		btnVolver.setBounds(40, 175, 250, 25);
		btnVolver.setVisible(true);
		infoListPanel.add(btnVolver);
		
		//Panel del listado de doctores
		doctorsListPanel = new JPanel(){
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
		doctorsListPanel.setBounds(827, 432, 225, 225);
		doctorsListPanel.setBackground(Color.WHITE);
		doctorsListPanel.setVisible(false);
		doctorsListPanel.setLayout(null);
		doctorsListPanel.setFocusable(false);
		doctorsListPanel.setOpaque(false);
		infoPanel.add(doctorsListPanel);
		
		JLabel iconDoctorsList = new JLabel(){
			/**
			 * 
			 */
			 private static final long serialVersionUID = 1L;

			    @Override
			    protected void paintComponent(Graphics g) {
			        Graphics2D g2 = (Graphics2D) g;
			        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);			        
			        g2.setColor(getBackground());
			        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			        super.paintComponent(g);
			    }
		};
		MouseListener eventoDoctor = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				doctorsListPanel.setBackground(new Color(189, 214, 240));
				doctorsListPanel.repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				doctorsListPanel.setBackground(Color.WHITE);
				doctorsListPanel.repaint();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				doctorsListPanel.setBackground(new Color(207, 207, 207));
				try {
					ListarDoctores listDoctor = new ListarDoctores();
					listDoctor.setVisible(true);
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
				}
			}
		};		
		ImageIcon imgDoctorList = new ImageIcon(getClass().getResource("/Imagenes/doctorList.png"));
		Image scaleDoctorList = imgDoctorList.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		iconDoctorsList.setIcon(new ImageIcon(scaleDoctorList));
		iconDoctorsList.setBounds(50, 20, 125, 125);
		iconDoctorsList.setVisible(true);
		iconDoctorsList.setOpaque(false);
		iconDoctorsList.setBackground(new Color(0,0,0,0));
		iconDoctorsList.setHorizontalAlignment(JLabel.CENTER);
		iconDoctorsList.addMouseListener(eventoDoctor);
		JLabel lblDoctorList = new JLabel("Lista de doctores");
		lblDoctorList.setFont(normalUse);
		lblDoctorList.setForeground(paleteBeautyBlu);
		lblDoctorList.setBounds(20, 160, 185, 20);
		lblDoctorList.setHorizontalAlignment(JLabel.CENTER);
		lblDoctorList.setBackground(new Color(0,0,0,0));
		lblDoctorList.addMouseListener(eventoDoctor);
		lblDoctorList.setOpaque(false);
		doctorsListPanel.addMouseListener(eventoDoctor);
		doctorsListPanel.add(iconDoctorsList);
		doctorsListPanel.add(lblDoctorList);
		
		//panel del listado de administradores
		adminListPanel = new JPanel(){
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
		adminListPanel.setBounds(572, 432, 225, 225);
		adminListPanel.setBackground(Color.WHITE);
		adminListPanel.setVisible(false);
		adminListPanel.setLayout(null);
		adminListPanel.setFocusable(false);
		adminListPanel.setOpaque(false);
		infoPanel.add(adminListPanel);
		
		JLabel iconAdminList = new JLabel(){
			/**
			 * 
			 */
			 private static final long serialVersionUID = 1L;

			    @Override
			    protected void paintComponent(Graphics g) {
			        Graphics2D g2 = (Graphics2D) g;
			        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);			        
			        g2.setColor(getBackground());
			        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			        super.paintComponent(g);
			    }
		};
		MouseListener eventoAdmin = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				adminListPanel.setBackground(new Color(189, 214, 240));
				adminListPanel.repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				adminListPanel.setBackground(Color.WHITE);
				adminListPanel.repaint();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				adminListPanel.setBackground(new Color(207, 207, 207));
				try {
					ListarAdministradores listAdministrador = new ListarAdministradores();
					listAdministrador.setVisible(true);
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
				}
			}
		};		
		ImageIcon imgAdminList = new ImageIcon(getClass().getResource("/Imagenes/adminList.png"));
		Image scaleAdminList = imgAdminList.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		iconAdminList.setIcon(new ImageIcon(scaleAdminList));
		iconAdminList.setBounds(50, 20, 125, 125);
		iconAdminList.setVisible(true);
		iconAdminList.setOpaque(false);
		iconAdminList.setBackground(new Color(0,0,0,0));
		iconAdminList.setHorizontalAlignment(JLabel.CENTER);
		iconAdminList.addMouseListener(eventoAdmin);
		JLabel lblAdminList = new JLabel("Lista de administradores");
		lblAdminList.setFont(normalUse);
		lblAdminList.setForeground(paleteBeautyBlu);
		lblAdminList.setBounds(20, 160, 185, 20);
		lblAdminList.setHorizontalAlignment(JLabel.CENTER);
		lblAdminList.setBackground(new Color(0,0,0,0));
		lblAdminList.addMouseListener(eventoAdmin);
		lblAdminList.setOpaque(false);
		adminListPanel.addMouseListener(eventoAdmin);
		adminListPanel.add(iconAdminList);
		adminListPanel.add(lblAdminList);
		
		vacunaListPanel = new JPanel(){
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
		vacunaListPanel.setBounds(317, 432, 225, 225);
		vacunaListPanel.setBackground(Color.WHITE);
		vacunaListPanel.setVisible(false);
		vacunaListPanel.setLayout(null);
		vacunaListPanel.setFocusable(false);
		vacunaListPanel.setOpaque(false);
		infoPanel.add(vacunaListPanel);
		
		JLabel iconVacunaList = new JLabel(){
			/**
			 * 
			 */
			 private static final long serialVersionUID = 1L;

			    @Override
			    protected void paintComponent(Graphics g) {
			        Graphics2D g2 = (Graphics2D) g;
			        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);			        
			        g2.setColor(getBackground());
			        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			        super.paintComponent(g);
			    }
		};
		MouseListener eventoVacuna = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				vacunaListPanel.setBackground(new Color(189, 214, 240));
				vacunaListPanel.repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				vacunaListPanel.setBackground(Color.WHITE);
				vacunaListPanel.repaint();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				vacunaListPanel.setBackground(new Color(207, 207, 207));
				try {
					ListarVacunas listVacuna = new ListarVacunas();
					listVacuna.setVisible(true);
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
				}
			}
		};		
		ImageIcon imgVacunaList = new ImageIcon(getClass().getResource("/Imagenes/vacinList.png"));
		Image scaleVacunaList = imgVacunaList.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		iconVacunaList.setIcon(new ImageIcon(scaleVacunaList));
		iconVacunaList.setBounds(50, 20, 125, 125);
		iconVacunaList.setVisible(true);
		iconVacunaList.setOpaque(false);
		iconVacunaList.setBackground(new Color(0,0,0,0));
		iconVacunaList.setHorizontalAlignment(JLabel.CENTER);
		iconVacunaList.addMouseListener(eventoVacuna);
		JLabel lblVacunaList = new JLabel("Lista de vacunas");
		lblVacunaList.setFont(normalUse);
		lblVacunaList.setForeground(paleteBeautyBlu);
		lblVacunaList.setBounds(20, 160, 185, 20);
		lblVacunaList.setHorizontalAlignment(JLabel.CENTER);
		lblVacunaList.setBackground(new Color(0,0,0,0));
		lblVacunaList.addMouseListener(eventoVacuna);
		lblVacunaList.setOpaque(false);
		vacunaListPanel.addMouseListener(eventoVacuna);
		vacunaListPanel.add(iconVacunaList);
		vacunaListPanel.add(lblVacunaList);
		
		pacientesListPanel = new JPanel(){
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
		pacientesListPanel.setBounds(57, 432, 225, 225);
		pacientesListPanel.setBackground(Color.WHITE);
		pacientesListPanel.setVisible(false);
		pacientesListPanel.setLayout(null);
		pacientesListPanel.setFocusable(false);
		pacientesListPanel.setOpaque(false);
		infoPanel.add(pacientesListPanel);
		
		JLabel iconPacienteList = new JLabel(){
			/**
			 * 
			 */
			 private static final long serialVersionUID = 1L;

			    @Override
			    protected void paintComponent(Graphics g) {
			        Graphics2D g2 = (Graphics2D) g;
			        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);			        
			        g2.setColor(getBackground());
			        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			        super.paintComponent(g);
			    }
		};
		MouseListener eventoPaciente = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				pacientesListPanel.setBackground(new Color(189, 214, 240));
				pacientesListPanel.repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				pacientesListPanel.setBackground(Color.WHITE);
				pacientesListPanel.repaint();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				pacientesListPanel.setBackground(new Color(207, 207, 207));
				try {
					ListarPacientes listPaciente = new ListarPacientes();
					listPaciente.setVisible(true);
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
				}
			}
		};		
		ImageIcon imgPacienteList = new ImageIcon(getClass().getResource("/Imagenes/pacientList.png"));
		Image scalePacienteList = imgPacienteList.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		iconPacienteList.setIcon(new ImageIcon(scalePacienteList));
		iconPacienteList.setBounds(50, 20, 125, 125);
		iconPacienteList.setVisible(true);
		iconPacienteList.setOpaque(false);
		iconPacienteList.setBackground(new Color(0,0,0,0));
		iconPacienteList.setHorizontalAlignment(JLabel.CENTER);
		iconPacienteList.addMouseListener(eventoPaciente);
		JLabel lblPacienteList = new JLabel("Lista de pacientes");
		lblPacienteList.setFont(normalUse);
		lblPacienteList.setForeground(paleteBeautyBlu);
		lblPacienteList.setBounds(20, 160, 185, 20);
		lblPacienteList.setHorizontalAlignment(JLabel.CENTER);
		lblPacienteList.setBackground(new Color(0,0,0,0));
		lblPacienteList.addMouseListener(eventoPaciente);
		lblPacienteList.setOpaque(false);
		pacientesListPanel.addMouseListener(eventoPaciente);
		pacientesListPanel.add(iconPacienteList);
		pacientesListPanel.add(lblPacienteList);
	}

	private String imagenEncontrada(String id) {
		File carpeta = new File("FotosAdmin");
		File[] archivos = carpeta.listFiles();
		boolean encontrada = false;
		String ruta="";
		int i=0;
		if (archivos != null) {
			while(!encontrada && i<archivos.length) {
				if (archivos[i].isFile()) {
					String nombreArchivo = archivos[i].getName();
					if(nombreArchivo.equalsIgnoreCase(id+".png") 
							|| nombreArchivo.equalsIgnoreCase(id+".jpg") 
							|| nombreArchivo.equalsIgnoreCase(id+".jpeg")) {
						encontrada = true;
						ruta = "FotosAdmin/" + nombreArchivo;
					}
				}
				i++;
			}
        }
		return ruta;
	}
}
