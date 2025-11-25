package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;

import java.awt.Toolkit;

import Utilidades.FuenteUtil;
import logico.Clinica;

public class RegAdmin extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegAdmin frame = new RegAdmin(0,"");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public RegAdmin(int mode, String idUser) {
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// ------------------ PANEL SUPERIOR (CABECERA) -------------------
		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(new java.awt.Color(4, 111, 67)); // verde oscuro
		panelSuperior.setLayout(null);
		panelSuperior.setPreferredSize(new java.awt.Dimension(0, 160));
		
		contentPane.add(panelSuperior, BorderLayout.NORTH);
		
		
		Dimension tamanioPantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int anchoPantalla = (int) tamanioPantalla.getWidth();
		
		
		// LOGO IZQUIERDO
		ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
		Image imgLogoEscalada = iconLogo.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		JLabel lblLogo = new JLabel(new ImageIcon(imgLogoEscalada));
		lblLogo.setBounds(70, 20, 90, 90);
		panelSuperior.add(lblLogo);
		
		
		// TEXTO LOGO
		JLabel lblNombreClinica = new JLabel("COMPILE SALUD");
		lblNombreClinica.setForeground(Color.WHITE);
		lblNombreClinica.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 22f));
		lblNombreClinica.setBounds(28, 110, 400, 40);
		panelSuperior.add(lblNombreClinica);
		
		
		// TÍTULO: REGISTRAR ADMINISTRADOR
		JLabel lblTitulo = new JLabel("REGISTRAR ADMINISTRADOR");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 34f));

		int anchoTitulo = 550;
		int xTitulo = (anchoPantalla - anchoTitulo) / 2;
		lblTitulo.setBounds(xTitulo, 100, anchoTitulo, 50);
		lblTitulo.setHorizontalAlignment(JLabel.CENTER);

		panelSuperior.add(lblTitulo);
		
		
		// ------------------ PANEL CENTRAL (CONTENIDO) -------------------
		JPanel panelContenido  = new JPanel();
		panelContenido.setBackground(java.awt.Color.WHITE);
		panelContenido.setLayout(null);
		
		contentPane.add(panelContenido, BorderLayout.CENTER);
		
		
		// ID ADMINISTRATIVO
		
		Clinica controlador = Clinica.getInstance();
		
		int nextIdAdmin = controlador.getProximoIdAdmin();
		String codigoAdmin = String.format("ID: A-%03d", nextIdAdmin);
		
		JLabel lblIdAdmin = new JLabel(codigoAdmin);
		lblIdAdmin.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 20f));
		lblIdAdmin.setForeground(new Color(50, 50, 50));
		
		int xID = (anchoPantalla  / 2) - 100;
		lblIdAdmin.setBounds (xID , 50 , 200, 40);
		
		lblIdAdmin.setHorizontalAlignment(JLabel.CENTER);
		panelContenido.add(lblIdAdmin);
		
		
		// AVATAR
		ImageIcon iconAvatar = new ImageIcon(getClass().getResource("/Imagenes/UsuarioGris.png"));
		Image imgAvatarEscalada = iconAvatar.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		JLabel lblAvatar = new JLabel(new ImageIcon(imgAvatarEscalada));

		int xAvatar = (anchoPantalla - 200) / 2;
		lblAvatar.setBounds(xAvatar, 100, 200, 200);

		panelContenido.add(lblAvatar);
		
		
		// CLICK PARA AGG FOTIS
		ImageIcon iconCam = new ImageIcon(getClass().getResource("/Imagenes/Camara.png"));
		Image imgCamEscalada = iconCam.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

		JLabel lblFotoTexto = new JLabel("   Click para agregar foto");
		lblFotoTexto.setIcon(new ImageIcon(imgCamEscalada));
		lblFotoTexto.setOpaque(true);
		
		lblFotoTexto.setBackground(new java.awt.Color(230, 230, 230));
		lblFotoTexto.setForeground(new java.awt.Color(55,65,81));
		
		lblFotoTexto.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));

		int anchoFotoTxt = 188;
		int xFotoTxt = (anchoPantalla - anchoFotoTxt) / 2;
		lblFotoTexto.setBounds(xFotoTxt, 320, anchoFotoTxt, 32);

		panelContenido.add(lblFotoTexto);
		
		
		// OPCION CAMBIAR FOTO
		lblAvatar.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {

		        JFileChooser chooser = new JFileChooser();
		        chooser.setFileFilter(
		            new FileNameExtensionFilter(
		                "Imágenes (JPG, PNG)", "jpg", "jpeg", "png"
		            )
		        );

		        int resultado = chooser.showOpenDialog(RegAdmin.this);
		        if (resultado != JFileChooser.APPROVE_OPTION) {
		            return; // uusuario canceló
		        }

		        File archivo = chooser.getSelectedFile();

		        // Cargar y escalar la imagen cuadardo
		        ImageIcon iconOriginal = new ImageIcon(archivo.getAbsolutePath());
		        Image imgEscalada = iconOriginal.getImage().getScaledInstance(
		                lblAvatar.getWidth(),
		                lblAvatar.getHeight(),
		                Image.SCALE_SMOOTH
		        );
		        ImageIcon iconFinal = new ImageIcon(imgEscalada);

		        lblAvatar.setIcon(iconFinal);

		        lblAvatar.putClientProperty("rutaFoto", archivo.getAbsolutePath());
		    }
		});

		lblFotoTexto.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        // mismo click del avatar
		        for (java.awt.event.MouseListener ml : lblAvatar.getMouseListeners()) {
		            ml.mouseClicked(e);
		        }
		    }
		});

		
		// ------------------ CAMPOS DE TEXTO PARA REGISTRO -------------------
		
		int anchoCampo = 350;
		int altoCampo = 40;
		
		//Camps de izquierda
		int xIzq = (anchoPantalla / 2) - (anchoCampo + 180);
		//Camps de derecha
		int xDer = (anchoPantalla / 2) + 180;
		
		int yFila1 = 110;
		int yFila2 = 230;
		int yFila3 = 350;
		
		// USUARIO
		JLabel lblUsuario = new JLabel("USUARIO");
		lblUsuario.setForeground(new Color(55,65,81));
		lblUsuario.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		lblUsuario.setBounds(xIzq, yFila1 - 25, 200, 20);
		panelContenido.add(lblUsuario);

		JTextField txtUsuario = new JTextField();
		txtUsuario.setBounds(xIzq, yFila1, anchoCampo, altoCampo);
		txtUsuario.setBorder(
		    BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK)
		);
		txtUsuario.setForeground(new Color(55,65,81));
		txtUsuario.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		panelContenido.add(txtUsuario);
		
		
		// CONTRASENIA
		JLabel lblContrasenia = new JLabel("CONTRASEÑA");
		lblContrasenia.setForeground(new Color(55,65,81));
		lblContrasenia.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		lblContrasenia.setBounds(xIzq, yFila2 - 25, 200, 20);
		panelContenido.add(lblContrasenia);

		JTextField txtContrasenia = new JTextField();
		txtContrasenia.setBounds(xIzq, yFila2, anchoCampo, altoCampo);
		txtContrasenia.setBorder(
		    BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK)
		);
		txtContrasenia.setForeground(new Color(55,65,81));
		txtContrasenia.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		panelContenido.add(txtContrasenia);
		
		
		
		// ===== DERECHA ====
		
		// NOMBRE
		JLabel lblNombre = new JLabel("NOMBRE");
		lblNombre.setForeground(new Color(55,65,81));
		lblNombre.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		lblNombre.setBounds(xDer, yFila1 - 25, 200, 20);
		panelContenido.add(lblNombre);

		JTextField txtNombre = new JTextField();
		txtNombre.setBounds(xDer, yFila1, anchoCampo, altoCampo);
		txtNombre.setBorder(
		    BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK)
		);
		txtNombre.setForeground(new Color(55,65,81));
		txtNombre.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		panelContenido.add(txtNombre);
		
		
		// CARGO
		JLabel lblCargo = new JLabel("CARGO");
		lblCargo.setForeground(new Color(55,65,81));
		lblCargo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		lblCargo.setBounds(xDer, yFila2 - 25, 200, 20);
		panelContenido.add(lblCargo);

		JTextField txtCargo = new JTextField();
		txtCargo.setBounds(xDer, yFila2, anchoCampo, altoCampo);
		txtCargo.setBorder(
		    BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK)
		);
		txtCargo.setForeground(new Color(55,65,81));
		txtCargo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		panelContenido.add(txtCargo);
		

		
		// ------------------ REGISTRAR -------------------
		
		// MENSAJE DE ERROR
		JLabel lblMensaje = new JLabel("");
		lblMensaje.setForeground(new Color(220, 38, 38)); // rojo por defecto (errores)
		lblMensaje.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
		
			//Temporizador
		javax.swing.Timer timerMensaje = new javax.swing.Timer(3000, e -> {
			lblMensaje.setText("");
		});
		timerMensaje.setRepeats(false);
		
		int anchoMensaje = 400;
		int xMensaje = (anchoPantalla - anchoMensaje) / 2;
		lblMensaje.setBounds(xMensaje, 380, anchoMensaje, 20);
		lblMensaje.setHorizontalAlignment(JLabel.CENTER);

		panelContenido.add(lblMensaje);
		
		
		// BOTOM
		Color verdeBtn = new Color(22,163,74);
		Color verdeHover = new Color(18,140,64);

		JButton btnRegistrar = new JButton("REGISTRAR");
		btnRegistrar.setBackground(verdeBtn);
		btnRegistrar.setForeground(Color.WHITE);
		btnRegistrar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 16f));
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.setBorderPainted(false);

		int anchoBtn = 260;
		int altoBtn = 40;
		int xBtn = (anchoPantalla - anchoBtn) / 2;
		btnRegistrar.setBounds(xBtn, yFila3 + 70, anchoBtn, altoBtn);

		panelContenido.add(btnRegistrar);

		// EFECTO DEL MAUSE EN BOTON
		btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseEntered(java.awt.event.MouseEvent e) {
		        btnRegistrar.setBackground(verdeHover);
		    }
		    @Override
		    public void mouseExited(java.awt.event.MouseEvent e) {
		        btnRegistrar.setBackground(verdeBtn);
		    }
		});
		
		btnRegistrar.addActionListener(e -> {
			String usuario = txtUsuario.getText();
		    String contrasenia = txtContrasenia.getText();
		    String nombre = txtNombre.getText();
		    String cargo = txtCargo.getText();
		    //String cupoTexto = txtCupoDia.getText();
		    
		    String rutaFotoOriginal = (String) lblAvatar.getClientProperty("rutaFoto");
			
		    boolean ok = controlador.registrarAdminDesdeFormulario(
		    		usuario, contrasenia, nombre, cargo, rutaFotoOriginal);

		    if (!ok) {
		        //  error
		        lblMensaje.setForeground(new Color(220, 38, 38)); // rojo
		        lblMensaje.setText(controlador.getUltimoMensajeError());
		        timerMensaje.restart();
		    } else {
		        // éxito
		        lblMensaje.setForeground(new Color(22, 163, 74)); // verde
		        lblMensaje.setText("Administrativo registrado correctamente.");
		        timerMensaje.restart();

		        txtUsuario.setText("");
		        txtContrasenia.setText("");
		        txtNombre.setText("");
		        txtCargo.setText("");
		        
		        // Avatar de inicio
		        ImageIcon iconUsuarioGris = new ImageIcon(
		                getClass().getResource("/Imagenes/UsuarioGris.png")
		        );
		        Image imgUsuarioGris = iconUsuarioGris.getImage().getScaledInstance(
		                lblAvatar.getWidth(),
		                lblAvatar.getHeight(),
		                Image.SCALE_SMOOTH
		        );
		        lblAvatar.setIcon(new ImageIcon(imgUsuarioGris));
		        lblAvatar.putClientProperty("rutaFoto", null);
		        
		        int siguienteId = controlador.getProximoIdAdmin();
		        String nuevoCodigo = String.format("ID: D-%03d", siguienteId);
		        lblIdAdmin.setText(nuevoCodigo);
		    }
		});
		
		
		// ------------------ FLECHA PARA REGRESAR -------------------
		ImageIcon iconFlecha = new ImageIcon(getClass().getResource("/Imagenes/FlechaAtras.png"));
		Image imgFlechaScaled = iconFlecha.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon iconFlechaFinal = new ImageIcon(imgFlechaScaled);
		

		JPanel btnVolver = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
		    protected void paintComponent(Graphics g) {

		        Graphics2D g2 = (Graphics2D) g;
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		        g2.setColor(getBackground());
		        g2.fillOval(0, 0, getWidth(), getHeight());
		    }
		};
		btnVolver.setOpaque(false);
		btnVolver.setLayout(null);
		btnVolver.setBounds(60, yFila3 + 90 , 70, 70);
		
		// COLOR CIRCULO
		btnVolver.setBackground(new Color(0, 64, 44));
		
		btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        dispose();
		    }
		    
		    @Override
		    public void mouseEntered(java.awt.event.MouseEvent e) {
		        btnVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		        btnVolver.setBackground(new Color(4, 120, 62)); // Verde mas claro
		        btnVolver.repaint();
		    }

		    @Override
		    public void mouseExited(java.awt.event.MouseEvent e) {
		        btnVolver.setBackground(new Color(0, 64, 44));
		        btnVolver.repaint();
		    }
		});
		
		// Flecha
		JLabel lblFlecha = new JLabel(iconFlechaFinal);
		int sizeCirculo = 70;
		int sizeIcono = 30;
		int xIcon = (sizeCirculo - sizeIcono) / 2;
		int yIcon = (sizeCirculo - sizeIcono) / 2;

		lblFlecha.setBounds(xIcon, yIcon, sizeIcono, sizeIcono);
		btnVolver.add(lblFlecha);

		panelContenido.add(btnVolver);
	}

}
