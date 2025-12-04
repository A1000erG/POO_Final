package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

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
import logico.Administrativo;
import logico.Cita;
import logico.Clinica;
import logico.Consulta;
import logico.Doctor;
import logico.Enfermedad;
import logico.Vacuna;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	// Colores 
	private static Color paleteGreen = new Color(22, 163, 74);
	private static Color paleteDarkGreen = new Color(18, 140, 64);
	private static Color paleteBeautyBlu = new Color(21, 129, 191);
	private static Color paleteLightGrey = new Color(240, 245, 250);
	private static Color paletaRojo = new Color(220, 38, 38);
	private static Color paletaRojoOscuro = new Color(180, 0, 0);
	
	// Fuentes 
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
	
	// Nuevo Panel para gráfico de vacunas (Integrado de PrincipalAngel) [cite: 158]
	private static JPanel barGraphVacunaPanel;

	private static JPanel doctorsListPanel;	
	private static JPanel adminListPanel;	
	private static JPanel vacunaListPanel;	
	private static JPanel pacientesListPanel;
	
	private static Clinica clinic = Clinica.getInstance();
	private JPanel bkgPanel;
	private JLabel lblDoctor;
	
	// Timer para notificaciones (Integrado de PrincipalAngel) [cite: 160]
	private Timer timerNotificacion;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal(0,"admin"); // Usuario por defecto para pruebas
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana.", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

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
					// Integración: Login al cerrar sesión si fuese necesario, o salir.
					System.exit(0);
				}
			}
		});

		contentPane = new JPanel();
		// Ajuste de altura para acomodar los nuevos gráficos [cite: 18]
		contentPane.setPreferredSize(new Dimension(820, 3100)); 
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		bkgPanel = new JPanel();
		bkgPanel.setBackground(paleteLightGrey);
		contentPane.add(bkgPanel, BorderLayout.CENTER);
		bkgPanel.setLayout(null);

		infoPanel = new JPanel();
		infoPanel.setSize(1121, 1080); // Aumentado el alto para acomodar el segundo gráfico
		infoPanel.setLocation(240, 0);
		infoPanel.setOpaque(false);
		infoPanel.setLayout(null);
		bkgPanel.add(infoPanel);

		JPanel optionPanel = new JPanel();
		optionPanel.setBackground(paleteGreen);
		optionPanel.setLayout(null);
		optionPanel.setBounds(0, 0, 240, 1080); // Ajustado al alto del infoPanel
		bkgPanel.add(optionPanel);

		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
		Image logoEscalado = logoIcon.getImage().getScaledInstance(76, 76, Image.SCALE_SMOOTH);
		JLabel lbLogo = new JLabel(new ImageIcon(logoEscalado));
		lbLogo.setBounds(82, 26, 76, 76);
		optionPanel.add(lbLogo);

		// ================= BOTONES DEL MENÚ LATERAL =================
		
		JButton btnRegAdmin = new JButton("New button");	
		configurarBotonMenu(btnRegAdmin, mode == 0 ? "Registrar Admin" : "Citas", 177);
		btnRegAdmin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mode==0) {
					try {
						RegAdmin regAdmin = new RegAdmin(mode, idUser);
						regAdmin.setVisible(true);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		optionPanel.add(btnRegAdmin);

		JButton btnRegDoctor = new JButton("New button");
		configurarBotonMenu(btnRegDoctor, mode == 0 ? "Registrar Doctor" : "Mis Consultas", 224);
		btnRegDoctor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mode==0) {
					try {
						RegDoctor regDoctor = new RegDoctor(mode, idUser);
						regDoctor.setVisible(true);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		optionPanel.add(btnRegDoctor);

		JButton btnConsultas = new JButton("New button");
		configurarBotonMenu(btnConsultas, mode == 0 ? "Registrar Cita" : "Nueva Cita", 271);
		btnConsultas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					RegCita regCita = new RegCita();
					regCita.setVisible(true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		optionPanel.add(btnConsultas);

		JButton btnReportes = new JButton("New button");
		configurarBotonMenu(btnReportes, "Reportes", 318);
		if (mode != 0) btnReportes.setVisible(false);
		btnReportes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mode==0) {
					try {
						Reportes report = new Reportes();
						report.setVisible(true);
						report.setModal(true);
					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana.", "Error Inesperado", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		optionPanel.add(btnReportes);

		JButton btnListados = new JButton("Listados");
		configurarBotonMenu(btnListados, "Listados", 365);
		if (mode != 0) btnListados.setVisible(false);
		btnListados.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarListados(true);
			}
		});
		optionPanel.add(btnListados);
		
		// --- INTEGRACIÓN: BOTÓN GESTIÓN DE RECURSOS  ---
		// Solo visible para Admin (mode 0), situado debajo de Listados
		JButton btnGestionRecursos = new JButton("Gestión Recursos");
		configurarBotonMenu(btnGestionRecursos, "Gestión Recursos", 412);
		if (mode != 0) btnGestionRecursos.setVisible(false);
		btnGestionRecursos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					GestionRecursos gestion = new GestionRecursos();
					gestion.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		optionPanel.add(btnGestionRecursos);

		JButton btnCerrarSesion = new JButton("Cerrar Sesión");
		btnCerrarSesion.setForeground(Color.WHITE);
		btnCerrarSesion.setFont(buttonFont);
		btnCerrarSesion.setBackground(paletaRojo);
		btnCerrarSesion.setBounds(0, 650, 240, 47); 
		btnCerrarSesion.setVisible(true);
		btnCerrarSesion.setBorderPainted(false);
		btnCerrarSesion.setFocusPainted(false);
		btnCerrarSesion.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        btnCerrarSesion.setBackground(paletaRojoOscuro);
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
		        btnCerrarSesion.setBackground(paletaRojo);
		    }

		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int opcion = JOptionPane.showConfirmDialog(null, 
		                "¿Estás seguro de que deseas salir de la aplicación?", 
		                "Confirmar Cierre", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		        if(opcion == JOptionPane.YES_OPTION) {
		            Clinica.getInstance().guardarDatosLocal();
		            dispose();
		            System.exit(0);
		            /*
		             * 
		             */
		        }
		    }
		});
		optionPanel.add(btnCerrarSesion);

		// ================= INFO USER PANEL =================
		
		JPanel infoUserPanel = new JPanel(); 
		infoUserPanel.setBackground(paleteDarkGreen);
		infoUserPanel.setBounds(0, 0, 1131, 120);
		infoPanel.add(infoUserPanel);
		infoUserPanel.setLayout(null);
		
		String idCod = "";
		String nombreUsuario = "";
		if(mode==0) {
			if(Clinica.getInstance().getAdministrativos().size()>0) {
				Administrativo admin = Clinica.getInstance().getAdministrativoPorUsuario(idUser);
				if(admin != null) {
					nombreUsuario = admin.getNombre();
					idCod = String.format("ID: A-%03d", admin.getIdAdmin()); 
				} else { nombreUsuario = "Admin"; }
			}else nombreUsuario = "Admin";	
		}else {
			if(Clinica.getInstance().getDoctores().size()>0) {
				Doctor doctor = Clinica.getInstance().getDoctorPorUsuario(idUser);
				if(doctor != null) {
					nombreUsuario = doctor.getNombre();
					idCod = String.format("ID: D-%03d", doctor.getIdDoctor());
				} else { nombreUsuario = "Doctor"; }
			}else nombreUsuario = "Doctor";
		}
		
		JLabel lblNombreUser = new JLabel(nombreUsuario);
		lblNombreUser.setForeground(Color.WHITE);
		lblNombreUser.setFont(nameUser);
		lblNombreUser.setBounds(846, 37, 153, 14);
		infoUserPanel.add(lblNombreUser);

		JLabel lblRolUser = new JLabel(mode == 0 ? "Administrativo" : "Doctor");
		lblRolUser.setForeground(Color.WHITE);
		lblRolUser.setFont(normalUse);
		lblRolUser.setBounds(846, 62, 119, 14);
		infoUserPanel.add(lblRolUser);
		
		String dirImagen = imagenEncontrada(idUser);
		ImageIcon imgUser;
		if(dirImagen.equalsIgnoreCase("")) {
			imgUser = new ImageIcon(getClass().getResource("/Imagenes/useResi.png"));
		}else {
			imgUser = new ImageIcon(idCod); // Nota: Esto parece depender de una lógica específica de carga por ID
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
		lblDoctora.setVisible(mode != 0);
		infoPanel.add(lblDoctora);

		JPanel welcomePanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
			}
		};
		welcomePanel.setBackground(Color.WHITE);
		welcomePanel.setBounds(57, 177, 1014, 200);
		welcomePanel.setVisible(mode != 0);
		welcomePanel.setLayout(null);
		
		ImageIcon bannerIcon = new ImageIcon(getClass().getResource("/Imagenes/doctorBanner.png"));
		Image bannerEscalado = bannerIcon.getImage().getScaledInstance(1041, 200, Image.SCALE_SMOOTH);
		JLabel lblBanner = new JLabel(bannerIcon) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D redondeo = (Graphics2D) g.create();
				redondeo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int radio = 30;
				redondeo.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radio, radio));
				super.paintComponent(redondeo);
				redondeo.dispose();
			}
		};
		lblBanner.setBounds(0, 0, 1014, 200);
		lblBanner.setIcon(new ImageIcon(bannerEscalado));
		welcomePanel.add(lblBanner);
		infoPanel.add(welcomePanel);

		// Paneles inferiores (Doctor Mode)
		crearPanelesInferioresDoctor(mode);

		//===================================PANELES PARA USUARIOS ADMINISTRATIVOS=================================
		
		// 1. Panel Cantidad Enfermedades
		cantEnfermPanel = crearPanelEstadistica(57, 177, "/Imagenes/thermometer.png", 
				"Enfermedades Controladas", Clinica.getInstance().getCatalogoEnfermedades().size(), mode);
		infoPanel.add(cantEnfermPanel);

		// 2. Panel Cantidad Vacunas
		cantVacunasPanel = crearPanelEstadistica(414, 177, "/Imagenes/syringe.png", 
				"Vacunas Existentes", Clinica.getInstance().getVacunas().size(), mode);
		infoPanel.add(cantVacunasPanel);

		// 3. Panel Citas Hoy
		Integer cantCitasHoy = 0;
		LocalDate hoy = LocalDate.now();
		for (Cita cita : Clinica.getInstance().getCitas()) {
			if(cita.getFecha().isEqual(hoy)) cantCitasHoy++; // Corrección fecha [cite: 72]
		}
		cantCitasHoyPanel = crearPanelEstadistica(771, 177, "/Imagenes/stethoscope.png", 
				"Citas para hoy", cantCitasHoy, mode);
		infoPanel.add(cantCitasHoyPanel);

		// 4. Gráfico de Enfermedades [cite: 74-82]
		barGraphSickPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			}
		};
		barGraphSickPanel.setBackground(Color.WHITE);
		barGraphSickPanel.setBounds(57, 355, 1014, 318);
		barGraphSickPanel.setVisible(mode == 0);
		infoPanel.add(barGraphSickPanel);
		barGraphSickPanel.setLayout(null);

		configurarGraficoEnfermedades(); // Lógica movida a método para limpieza
		
		// 5. --- INTEGRACIÓN: Gráfico de Vacunas (Nuevo) [cite: 158, 237, 303] ---
		barGraphVacunaPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			}
		};
		barGraphVacunaPanel.setBackground(Color.WHITE);
		barGraphVacunaPanel.setBounds(57, 685, 1014, 318); // Posicionado debajo del primer gráfico
		barGraphVacunaPanel.setVisible(mode == 0);
		infoPanel.add(barGraphVacunaPanel);
		barGraphVacunaPanel.setLayout(null);
		
		configurarGraficoVacunas(); // Nueva lógica traída de PrincipalAngel

		//================PANELES PARA LISTADOS (Ocultos inicialmente)======================
		
		ImageIcon doctorIcon = new ImageIcon(getClass().getResource("/Imagenes/doctorPanel2.png"));
		Image doctorRedim2 = doctorIcon.getImage().getScaledInstance(300, 286, Image.SCALE_SMOOTH);
		lblDoctor = new JLabel(new ImageIcon(doctorRedim2));
		lblDoctor.setBounds(680,115,300,286);
		lblDoctor.setVisible(false);
		infoPanel.add(lblDoctor);

		infoListPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
			}
		};
		infoListPanel.setBackground(paleteBeautyBlu);
		infoListPanel.setBounds(57, 177, 1014, 225);
		infoListPanel.setVisible(false);
		infoListPanel.setLayout(null);
		infoPanel.add(infoListPanel);
		
		JLabel lblTitlePanel = new JLabel("Panel de Gestión");
		lblTitlePanel.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 28f));
		lblTitlePanel.setForeground(Color.WHITE);
		lblTitlePanel.setBounds(40, 30, 300, 30);
		infoListPanel.add(lblTitlePanel);
		
		JLabel lblInfoPanel = new JLabel("Seleccione una categoría.");
		lblInfoPanel.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 14f));
		lblInfoPanel.setForeground(Color.WHITE);
		lblInfoPanel.setBounds(45, 75, 200, 25);
		infoListPanel.add(lblInfoPanel);

		JButton btnVolver = new JButton("Volver al Dashboard"){
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
				mostrarListados(false);
			}
		});
		btnVolver.setFont(buttonFont);
		btnVolver.setForeground(paleteBeautyBlu);
		btnVolver.setBackground(Color.WHITE);
		btnVolver.setBounds(40, 175, 250, 25);
		infoListPanel.add(btnVolver);

		// Inicialización de Paneles de Listado (Doctores, Admins, Vacunas, Pacientes)
		// Mantenemos la lógica visual de Clinica.txt pero encapsulada ligeramente
		doctorsListPanel = crearPanelListado(827, "/Imagenes/doctorList.png", "Lista de doctores", "Doctores");
		infoPanel.add(doctorsListPanel);
		
		adminListPanel = crearPanelListado(572, "/Imagenes/adminList.png", "Lista de administradores", "Administradores");
		infoPanel.add(adminListPanel);
		
		vacunaListPanel = crearPanelListado(317, "/Imagenes/vacinList.png", "Lista de vacunas", "Vacunas");
		infoPanel.add(vacunaListPanel);
		
		pacientesListPanel = crearPanelListado(57, "/Imagenes/pacientList.png", "Lista de pacientes", "Pacientes");
		infoPanel.add(pacientesListPanel);
		
		// --- INTEGRACIÓN: INICIAR NOTIFICACIONES DOCTOR [cite: 167, 184] ---
		if(mode != 0) {
			iniciarNotificacionesDoctor(idUser);
		}
	}

	// ================= MÉTODOS AUXILIARES Y LÓGICA DE INTEGRACIÓN =================

	// Lógica para el gráfico de Enfermedades (Restaurada de Clinica.txt con pequeñas mejoras)
	private void configurarGraficoEnfermedades() {
		DefaultCategoryDataset enfermedadesDataset = new DefaultCategoryDataset();
		int maxEnfermedades = 5;
		int count = clinic.getCatalogoEnfermedades().size();
		
		if(count > 0) {
			// Lógica simplificada para tomar los últimos 5 o todos
			int start = Math.max(0, count - 5);
			for(int i = start; i < count; i++) {
				Enfermedad e = clinic.getCatalogoEnfermedades().get(i);
				enfermedadesDataset.addValue(1, "Pacientes", e.getNombre()); 
			}

			JFreeChart chartEnfermedades = ChartFactory.createBarChart("Pacientes por Enfermedad", "Enfermedades", "Cantidad", enfermedadesDataset, PlotOrientation.VERTICAL, false, true, false);
			estilizarGrafico(chartEnfermedades, barGraphSickPanel);
		} else {
			JLabel noData = new JLabel("No hay data");
			noData.setBounds(30,5,954,308);
			barGraphSickPanel.add(noData);
		}
	}

	// Lógica para el gráfico de Vacunas (Integrada de PrincipalAngel) [cite: 303-314]
	private void configurarGraficoVacunas() {
		DefaultCategoryDataset vacunaDataset = new DefaultCategoryDataset();
		ArrayList<Vacuna> listaVacunas = clinic.getVacunas();
		Map<String, Integer> mapaConteo = new HashMap<>();

		if (listaVacunas != null) {
			for (Vacuna vacuna : listaVacunas) {
				mapaConteo.put(vacuna.getNombre(), mapaConteo.getOrDefault(vacuna.getNombre(), 0) + 1);
			}
		}
		
		if (!mapaConteo.isEmpty()) {
			for (Map.Entry<String, Integer> entry : mapaConteo.entrySet()) {
				vacunaDataset.addValue(entry.getValue(), "Stock", entry.getKey());
			}
			JFreeChart chartVacunas = ChartFactory.createBarChart("Inventario de Vacunas", "Vacuna", "Stock", vacunaDataset, PlotOrientation.HORIZONTAL, false, true, false);
			estilizarGrafico(chartVacunas, barGraphVacunaPanel);
		} else {
			JLabel noData = new JLabel("No hay data de vacunas");
			noData.setBounds(30,5,954,308);
			barGraphVacunaPanel.add(noData);
		}
	}

	private void estilizarGrafico(JFreeChart chart, JPanel parent) {
		CategoryPlot plot = chart.getCategoryPlot();
		chart.setBackgroundPaint(Color.WHITE);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);
		plot.setOutlineVisible(false);

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, paleteBeautyBlu);
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setMaximumBarWidth(0.15);

		chart.getTitle().setFont(fuenteTituloGraph);
		plot.getDomainAxis().setLabelFont(fuenteEjesGraph);
		plot.getDomainAxis().setTickLabelFont(fuenteDatosGraph);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(30, 5, 954, 308);
		parent.add(chartPanel);
	}

	// Método auxiliar para crear paneles de estadística y reducir código repetitivo [cite: 57-70]
	private JPanel crearPanelEstadistica(int x, int y, String iconPath, String desc, int count, int mode) {
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			}
		};
		panel.setBackground(Color.WHITE);
		panel.setBounds(x, y, 300, 130);
		panel.setVisible(mode == 0);
		panel.setLayout(null);

		JLabel lblIcon = new JLabel(cargarIcono(iconPath, 108, 108));
		lblIcon.setBounds(171, 11, 108, 108);
		panel.add(lblIcon);

		JLabel lblDesc = new JLabel(desc);
		lblDesc.setFont(normalUse);
		lblDesc.setBounds(10, 105, 151, 14);
		panel.add(lblDesc);

		JLabel lblCount = new JLabel(String.valueOf(count));
		lblCount.setFont(indicativeNumber);
		lblCount.setBounds(10, 11, 80, 50);
		panel.add(lblCount);
		
		return panel;
	}

	// Método auxiliar para configurar botones del menú
	private void configurarBotonMenu(JButton btn, String text, int y) {
		btn.setText(text);
		btn.setBounds(0, y, 240, 47);
		btn.setForeground(Color.WHITE);
		btn.setFont(buttonFont);
		btn.setBackground(paleteGreen);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) { btn.setBackground(paleteDarkGreen); }
			@Override
			public void mouseExited(MouseEvent e) { btn.setBackground(paleteGreen); }
		});
	}

	// Lógica de visualización de listados [cite: 31, 91]
	private void mostrarListados(boolean mostrar) {
		cantEnfermPanel.setVisible(!mostrar);
		cantVacunasPanel.setVisible(!mostrar);
		cantCitasHoyPanel.setVisible(!mostrar);
		barGraphSickPanel.setVisible(!mostrar);
		barGraphVacunaPanel.setVisible(!mostrar); // También ocultamos el nuevo gráfico
		
		infoListPanel.setVisible(mostrar);
		adminListPanel.setVisible(mostrar);
		vacunaListPanel.setVisible(mostrar);
		doctorsListPanel.setVisible(mostrar);
		pacientesListPanel.setVisible(mostrar);
		lblDoctor.setVisible(mostrar);
	}

	// Método auxiliar para crear paneles de listado (Doctores, Admin, etc.) [cite: 93-137]
	private JPanel crearPanelListado(int x, String iconPath, String text, String tipo) {
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
			}
		};
		panel.setBounds(x, 432, 225, 225);
		panel.setBackground(Color.WHITE);
		panel.setVisible(false);
		panel.setLayout(null);
		panel.setOpaque(false);

		MouseListener eventoMouse = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				panel.setBackground(new Color(189, 214, 240));
				panel.repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panel.setBackground(Color.WHITE);
				panel.repaint();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				panel.setBackground(new Color(207, 207, 207));
				try {
					switch(tipo) {
						case "Doctores": new ListarDoctores().setVisible(true); break;
						case "Administradores": new ListarAdministradores().setVisible(true); break;
						case "Vacunas": new ListarVacunas().setVisible(true); break;
						case "Pacientes": new ListarPacientes().setVisible(true); break;
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		};

		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(cargarIcono(iconPath, 100, 100));
		iconLabel.setBounds(50, 20, 125, 125);
		iconLabel.setHorizontalAlignment(JLabel.CENTER);
		iconLabel.addMouseListener(eventoMouse);
		
		JLabel textLabel = new JLabel(text);
		textLabel.setFont(normalUse);
		textLabel.setForeground(paleteBeautyBlu);
		textLabel.setBounds(20, 160, 185, 20);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		textLabel.addMouseListener(eventoMouse);
		
		panel.add(iconLabel);
		panel.add(textLabel);
		panel.addMouseListener(eventoMouse);
		
		return panel;
	}
	
	private void crearPanelesInferioresDoctor(int mode) {
		// Panel inferior izquierdo (Enfermedades)
		JPanel listEnfPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			}
		};
		listEnfPanel.setBackground(Color.WHITE);
		listEnfPanel.setBounds(57, 425, 487, 280);
		listEnfPanel.setVisible(mode != 0);
		listEnfPanel.setLayout(null);
		infoPanel.add(listEnfPanel);

		JLabel lblEnfIcon = new JLabel(cargarIcono("/Imagenes/thermometer.png", 80, 80));
		lblEnfIcon.setBounds(397,10,80,80);
		listEnfPanel.add(lblEnfIcon);

		JLabel lblDesEnf = new JLabel("Enfermedades Controladas");
		lblDesEnf.setFont(normalUse);
		lblDesEnf.setBounds(10, 105, 151, 14);
		listEnfPanel.add(lblDesEnf);

		JLabel lblCEnf = new JLabel("25");
		lblCEnf.setFont(indicativeNumber);
		lblCEnf.setBounds(10, 11, 80, 50);
		listEnfPanel.add(lblCEnf);
		
		// Panel inferior derecho (Citas)
		JPanel citasHoyPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
			}
		};
		citasHoyPanel.setBackground(Color.WHITE);
		citasHoyPanel.setBounds(584, 425, 487, 280);
		citasHoyPanel.setVisible(mode != 0);
		citasHoyPanel.setLayout(null);
		infoPanel.add(citasHoyPanel);

		JLabel lblCitaIcon = new JLabel(cargarIcono("/Imagenes/stethoscope.png", 80, 80));
		lblCitaIcon.setBounds(397,10,80,80);
		citasHoyPanel.add(lblCitaIcon);
	}

	// --- LÓGICA DE NOTIFICACIONES (Extraída de PrincipalAngel) [cite: 184-199] ---
	private void iniciarNotificacionesDoctor(String idUsuario) {
		timerNotificacion = new Timer(60000, e -> verificarCitasProximas(idUsuario));
		timerNotificacion.start();
	}

	private void verificarCitasProximas(String idUsuario) {
    Doctor doctor = clinic.getDoctorPorUsuario(idUsuario);
    if (doctor == null) return;
    LocalDate hoy = LocalDate.now();
    LocalTime ahora = LocalTime.now();
    
    // DEFINIR EL FORMATO: "hh:mm a" maneja 12 horas con AM/PM
    // Usamos Locale.US para asegurar que reconozca "AM" y "PM" correctamente
    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);

    for (Cita cita : clinic.getCitas()) {
        if(cita.getDoctor().getIdDoctor() == doctor.getIdDoctor() && 
           cita.getFecha().isEqual(hoy) && 
           cita.getEstado().equalsIgnoreCase("Pendiente")) {
            
            try {
                // PARSEO SEGURO: Usamos el formato definido arriba
                LocalTime horaCita = LocalTime.parse(cita.getHora(), formatoHora);
                
                long diferenciaMinutos = ChronoUnit.MINUTES.between(ahora, horaCita);
                
                // Verificamos si faltan entre 0 y 10 minutos
                if(diferenciaMinutos >= 0 && diferenciaMinutos <= 10) {
                    // Detener el timer temporalmente para que no spamee la alerta
                    timerNotificacion.stop(); 
                    
                    JOptionPane.showMessageDialog(this, 
                            "RECORDATORIO: Tienes una cita con " + cita.getPaciente().getNombre() + 
                            " a las " + cita.getHora(), 
                            "Cita Próxima", JOptionPane.INFORMATION_MESSAGE);
                            
                    // Reiniciar el timer después de cerrar la alerta
                    timerNotificacion.restart();
                }
            } catch (Exception e) {
                // Si hay una hora mal formateada en la base de datos, la ignoramos para no romper el programa
                System.err.println("Error al parsear la hora de la cita: " + cita.getHora());
            }
        }
    }
}
	// --- Helper para cargar imágenes de forma segura (Inspirado en PrincipalAngel) [cite: 264] ---
	private ImageIcon cargarIcono(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(getClass().getResource(path));
		if (icon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
			return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		}
		return new ImageIcon();
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