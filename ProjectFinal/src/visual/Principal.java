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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Utilidades.FuenteUtil;
import logico.Administrativo;
import logico.Clinica;
import logico.Personal;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Colores y Fuentes
	private static Color paleteGreen = new Color(22, 163, 74);
	private static Color paleteDarkGreen = new Color(18, 140, 64);
	
	private static Font indicativeNumber = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 20f);
	private static Font normalUse = FuenteUtil.cargarFuente("/Fuentes/Roboto-Light.ttf", 11f);
	private static Font nameUser = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 13f);
	private static Font buttonFont = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 12f);
	
	private static JPanel infoPanel;
	private JPanel bkgPanel;
	
	// Usuario que inició sesión (Objeto Real)
	private Personal usuarioActual; 

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal(0,"admin"); // Test admin
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error inesperado al abrir la ventana.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public Principal(int mode, String idUser) {
		// --- Lógica para recuperar el Objeto Personal (Usuario Logueado) ---
		// Esto es necesario para pasarlo a los Listar y controlar permisos.
		if (mode == 0) { // Admin
			if (idUser.equalsIgnoreCase("admin")) {
				// Usuario "Super Admin" hardcodeado (Backdoor)
				Administrativo superAdmin = new Administrativo("admin", "admin", "Super Admin", "General");
				superAdmin.setIdAdmin(-1); // ID especial
				this.usuarioActual = superAdmin;
			} else {
				// Buscar en la lista real
				for (Administrativo a : Clinica.getInstance().getAdministrativos()) {
					if (a.getUsuario().equalsIgnoreCase(idUser)) {
						this.usuarioActual = a;
						break;
					}
				}
			}
		} else { // Doctor
			this.usuarioActual = Clinica.getInstance().getDoctorPorUsuario(idUser);
		}
		// -------------------------------------------------------------------

		getToolkit().getScreenSize();
		setResizable(false);
		setTitle("Compile Salud");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);		
		
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(820, 3100));
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
		
		// --- PANEL LATERAL IZQUIERDO (MENÚ) ---
		JPanel optionPanel = new JPanel();
		optionPanel.setBackground(paleteGreen);
		optionPanel.setLayout(null);
		optionPanel.setBounds(0, 0, 240, 738); // Altura ajustada
		bkgPanel.add(optionPanel);
		
		ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
		Image logoEscalado = logoIcon.getImage().getScaledInstance(76, 76, Image.SCALE_SMOOTH);
		JLabel lbLogo = new JLabel(new ImageIcon(logoEscalado));
		lbLogo.setBounds(82, 26, 76, 76);
		optionPanel.add(lbLogo);
		
		// 1. Botón Registrar Admin
		JButton btnRegAdmin = crearBotonMenu("Registrar Admin");
		btnRegAdmin.setBounds(0, 177, 240, 47);
		if(mode != 0) btnRegAdmin.setText("Citas"); // Si es doctor
		btnRegAdmin.addActionListener(e -> {
			if(mode == 0) {
				try {
					RegAdmin regAdmin = new RegAdmin(mode, idUser);
					regAdmin.setVisible(true);
				} catch (Exception ex) { ex.printStackTrace(); }
			}
		});
		optionPanel.add(btnRegAdmin);
		
		// 2. Botón Registrar Doctor
		JButton btnRegDoctor = crearBotonMenu("Registrar Doctor");
		btnRegDoctor.setBounds(0, 224, 240, 47);
		if(mode != 0) btnRegDoctor.setText("---"); // Placeholder doctor
		btnRegDoctor.addActionListener(e -> {
			if(mode == 0) {
				try {
					RegDoctor regDoctor = new RegDoctor(mode, idUser);
					regDoctor.setVisible(true);
				} catch (Exception ex) { ex.printStackTrace(); }
			}
		});
		optionPanel.add(btnRegDoctor);
		
		// 3. Botón Registrar Cita
		JButton btnConsultas = crearBotonMenu("Registrar Cita");
		btnConsultas.setBounds(0, 271, 240, 47);
		if(mode != 0) btnConsultas.setText("---");
		btnConsultas.addActionListener(e -> {
			if(mode == 0) {
				try {
					RegCita regCita = new RegCita(); // Asumiendo constructor vacío o ajustar
					regCita.setVisible(true);
				} catch (Exception ex) { ex.printStackTrace(); }
			}
		});
		optionPanel.add(btnConsultas);
		
		// 4. Botón Reportes
		JButton btnReportes = crearBotonMenu("Reportes");
		btnReportes.setBounds(0, 318, 240, 47);
		if(mode != 0) btnReportes.setVisible(false);
		optionPanel.add(btnReportes);
		
		// --- NUEVOS BOTONES DE LISTADO ---
		
		// 5. Botón Listar Doctores
		JButton btnListarDoc = crearBotonMenu("Listar Doctores");
		btnListarDoc.setBounds(0, 365, 240, 47);
		if(mode != 0) btnListarDoc.setVisible(false); // Solo Admin ve listas completas
		btnListarDoc.addActionListener(e -> {
			ListarDoctores listDoc = new ListarDoctores(usuarioActual);
			listDoc.setVisible(true);
		});
		optionPanel.add(btnListarDoc);
		
		// 6. Botón Listar Administradores
		JButton btnListarAdmin = crearBotonMenu("Listar Administradores");
		btnListarAdmin.setBounds(0, 412, 240, 47);
		if(mode != 0) btnListarAdmin.setVisible(false);
		btnListarAdmin.addActionListener(e -> {
			ListarAdministradores listAdmin = new ListarAdministradores(usuarioActual);
			listAdmin.setVisible(true);
		});
		optionPanel.add(btnListarAdmin);
		
		// --- FIN MENÚ ---

		// HEADER USUARIO
		JPanel infoUserPanel = new JPanel(); 
		infoUserPanel.setBackground(paleteDarkGreen);
		infoUserPanel.setBounds(0, 0, 1131, 120);
		infoPanel.add(infoUserPanel);
		infoUserPanel.setLayout(null);
		
		JLabel lblNombreUser = new JLabel("Nombre Usuario");
		lblNombreUser.setForeground(Color.WHITE);
		// Mostrar nombre real si existe usuario
		if (usuarioActual != null) {
			lblNombreUser.setText(usuarioActual.getNombre());
		} else {
			lblNombreUser.setText(idUser);
		}
		
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
		// Intentar cargar foto del usuario si tiene
		ImageIcon iconoUser = new ImageIcon(getClass().getResource("/Imagenes/useResi.png"));
		if (usuarioActual != null && usuarioActual.getRutaFoto() != null && !usuarioActual.getRutaFoto().isEmpty()) {
			ImageIcon fotoReal = new ImageIcon(usuarioActual.getRutaFoto());
			Image imgReal = fotoReal.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
			iconoUser = new ImageIcon(imgReal);
		}
		lblFotoUser.setIcon(iconoUser);
		infoUserPanel.add(lblFotoUser);
		
		// IMAGEN DE FONDO (DOCTORA)
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
		
		// --- PANELES DASHBOARD ---
		
		// Panel Bienvenida (Solo Doctor)
		crearPanelBienvenida(mode);
		
		// Paneles Estadísticas
		crearPanelesEstadisticas(mode);
	}
	
	// Método auxiliar para crear botones del menú con estilo uniforme
	private JButton crearBotonMenu(String texto) {
		JButton btn = new JButton(texto);
		btn.setForeground(Color.WHITE);
		btn.setFont(buttonFont);
		btn.setBackground(paleteGreen);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(paleteDarkGreen);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btn.setBackground(paleteGreen);
			}
		});
		return btn;
	}

	private void crearPanelBienvenida(int mode) {
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
		JLabel lblBanner = new JLabel(new ImageIcon(bannerEscalado)) {
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
		welcomePanel.add(lblBanner);
		infoPanel.add(welcomePanel);
	}

	private void crearPanelesEstadisticas(int mode) {
		// Iconos comunes
		ImageIcon enfermedadIcon = new ImageIcon(getClass().getResource("/Imagenes/thermometer.png"));
		ImageIcon citasHoyIcon = new ImageIcon(getClass().getResource("/Imagenes/stethoscope.png"));
		ImageIcon vacunaIcon = new ImageIcon(getClass().getResource("/Imagenes/syringe.png"));

		// --- PANELES MODO DOCTOR (Inferiores) ---
		
		JPanel listEnfPanel = crearPanelRedondeado(20);
		listEnfPanel.setBounds(57, 425, 487, 280);
		listEnfPanel.setVisible(mode != 0);
		infoPanel.add(listEnfPanel);
		
		Image enfEscala = enfermedadIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JLabel lblEnfIcon = new JLabel(new ImageIcon(enfEscala));
		lblEnfIcon.setBounds(397,10,80,80);
		listEnfPanel.add(lblEnfIcon);
		
		JLabel lblDesEnf = new JLabel("Enfermedades Controladas");
		lblDesEnf.setFont(normalUse);
		lblDesEnf.setBounds(10, 105, 151, 14);
		listEnfPanel.add(lblDesEnf);
		
		JLabel lblCEnf = new JLabel("25"); // Dato Dummy
		lblCEnf.setFont(indicativeNumber);
		lblCEnf.setBounds(10, 11, 80, 50);
		listEnfPanel.add(lblCEnf);
		
		JPanel citasHoyPanel = crearPanelRedondeado(20);
		citasHoyPanel.setBounds(584, 425, 487, 280);
		citasHoyPanel.setVisible(mode != 0);
		infoPanel.add(citasHoyPanel);
		
		Image citasEscala = citasHoyIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JLabel lblCitaIcon = new JLabel(new ImageIcon(citasEscala));
		lblCitaIcon.setBounds(397,10,80,80);
		citasHoyPanel.add(lblCitaIcon);

		// --- PANELES MODO ADMIN (Superiores) ---
		
		// 1. Enfermedades
		JPanel cantEnfermPanel = crearPanelRedondeado(20);
		cantEnfermPanel.setBounds(57, 177, 300, 130);
		cantEnfermPanel.setVisible(mode == 0);
		infoPanel.add(cantEnfermPanel);
		
		Image enfermedadEscalada = enfermedadIcon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
		JLabel lblEnfermedades = new JLabel(new ImageIcon(enfermedadEscalada));
		lblEnfermedades.setBounds(171, 11, 108, 108);
		cantEnfermPanel.add(lblEnfermedades);
		
		JLabel lblDescripEnf = new JLabel("Enfermedades Controladas");
		lblDescripEnf.setFont(normalUse);
		lblDescripEnf.setBounds(10, 105, 151, 14);
		cantEnfermPanel.add(lblDescripEnf);
		
		JLabel lblCountEnf = new JLabel("25");
		lblCountEnf.setFont(indicativeNumber);
		lblCountEnf.setBounds(10, 11, 80, 50);
		cantEnfermPanel.add(lblCountEnf);
		
		// 2. Vacunas
		JPanel cantVacunasPanel = crearPanelRedondeado(20);
		cantVacunasPanel.setBounds(414, 177, 300, 130);
		cantVacunasPanel.setVisible(mode == 0);
		infoPanel.add(cantVacunasPanel);
		
		Image vacunaEscalada = vacunaIcon.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
		JLabel lblVacuna = new JLabel(new ImageIcon(vacunaEscalada));
		lblVacuna.setBounds(171, 11, 108, 108);
		cantVacunasPanel.add(lblVacuna);
		
		JLabel lblDescripVacuna = new JLabel("Vacunas Existentes");
		lblDescripVacuna.setFont(normalUse);
		lblDescripVacuna.setBounds(10, 105, 151, 14);
		cantVacunasPanel.add(lblDescripVacuna);
		
		JLabel lblCountVac = new JLabel("13");
		lblCountVac.setFont(indicativeNumber);
		lblCountVac.setBounds(10, 11, 80, 50);
		cantVacunasPanel.add(lblCountVac);
		
		// 3. Citas
		JPanel cantCitasHoyPanel = crearPanelRedondeado(20);
		cantCitasHoyPanel.setBounds(771, 177, 300, 130);
		cantCitasHoyPanel.setVisible(mode == 0);
		infoPanel.add(cantCitasHoyPanel);
		
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
		
		// 4. Gráfico Grande
		JPanel barGraphSickPanel = crearPanelRedondeado(20);
		barGraphSickPanel.setBounds(57, 355, 1014, 318);
		barGraphSickPanel.setVisible(mode == 0);
		infoPanel.add(barGraphSickPanel);
	}
	
	private JPanel crearPanelRedondeado(int radio) {
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radio, radio);
			}
		};
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		return panel;
	}
}