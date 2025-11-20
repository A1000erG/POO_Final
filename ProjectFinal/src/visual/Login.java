package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Utilidades.FuenteUtil; // Descomentado
import logico.Clinica;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Clinica controlador; // Variable de instancia para el controlador

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					//frame.setUndecorated(true);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Login() {
		controlador = Clinica.getInstance(); // Inicializar el controlador
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// TAMANIO PARA EL LOGIN
		setSize(930, 650);
		setResizable(false);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		
		
		// ------------------ PANEL IZQUIERDO (FORMULARIO) -------------------
		JPanel panelIzquierdo = new JPanel();
		panelIzquierdo.setBackground(Color.WHITE);
		panelIzquierdo.setLayout(null);
		contentPane.add(panelIzquierdo, BorderLayout.CENTER);
		
		
		// LOGO
		ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Imagenes/logo.png"));

		Image imagenEscalada = iconLogo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		JLabel lblLogo = new JLabel(new ImageIcon(imagenEscalada));
		lblLogo.setBounds(80, 60, 40, 40);
		panelIzquierdo.add(lblLogo);
		
		// NOMBRE DE LA CLINICA
		JLabel lblEmpresa = new JLabel("COMPILE SALUD");
		lblEmpresa.setForeground(new Color(22,163, 74)); // color VERDE OSCURO
		lblEmpresa.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 24f));
		lblEmpresa.setBounds(130, 55, 700, 50);
		panelIzquierdo.add(lblEmpresa);
		
		// TÍTULO "INICIAR SESIÓN"
		JLabel lblTitulo = new JLabel("INICIAR SESIÓN");
		lblTitulo.setForeground(new Color(55,65,81)); // color NEGRO SUAVE
		lblTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 32f));
		lblTitulo.setBounds(80, 180, 400, 40);
		panelIzquierdo.add(lblTitulo);
		
		
		// LABEL USUARIO
		JLabel lblUsuario = new JLabel("USUARIO");
		lblUsuario.setForeground(new Color(55,65,81)); // gris oscuro bonito
		lblUsuario.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		lblUsuario.setBounds(80, 250, 200, 25);
		panelIzquierdo.add(lblUsuario);
		
		// CAMPO DE TEXTO PARA USUARIO
		JTextField txtUsuario = new JTextField();
		
		txtUsuario.setBorder( // línea bonita debajo de los datos
			BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(209, 213, 219))
		); 
		txtUsuario.setForeground(new Color(55,65,81));
		txtUsuario.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		txtUsuario.setBounds(80, 280, 350, 30);
		panelIzquierdo.add(txtUsuario);
		
		
		// LABEL CONTRASENIA
		JLabel lblContrasenia = new JLabel("CONTRASEÑA");
		lblContrasenia.setForeground(new Color(55,65,81)); // gris oscuro bonito
		lblContrasenia.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		lblContrasenia.setBounds(80, 350, 200, 25);
		panelIzquierdo.add(lblContrasenia);
		
		// CAMPO DE CONTRASENIA
		JPasswordField txtContrasenia = new JPasswordField();
		
		txtContrasenia.setBorder( // línea bonita debajo de los datos
			BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(209, 213, 219))
		); 
		txtContrasenia.setForeground(new Color(55,65,81));
		txtContrasenia.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
		txtContrasenia.setBounds(80, 380, 350, 30);
		panelIzquierdo.add(txtContrasenia);
		
		// ICONOS DEL OJO
		ImageIcon iconOjo = new ImageIcon(
			new ImageIcon(getClass().getResource("/Imagenes/Ver.png"))
				.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)
		);

		ImageIcon iconOjoTachado = new ImageIcon(
			new ImageIcon(getClass().getResource("/Imagenes/NoVer.png"))
				.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)
		);
		
		// BOTON DE VER CONTRASENIA
		JButton btnOjo = new JButton(iconOjoTachado); // empieza ocultando
		btnOjo.setBorder(null);
		btnOjo.setContentAreaFilled(false);
		btnOjo.setFocusPainted(false);
		btnOjo.setBounds(440, 380, 30, 30);

		panelIzquierdo.add(btnOjo);
		
		char echoOriginal = txtContrasenia.getEchoChar();
		final boolean[] mostrando = { false };

		btnOjo.addActionListener(e -> {
			if (mostrando[0]) {
				// Ocultar contraseña
				txtContrasenia.setEchoChar(echoOriginal);
				btnOjo.setIcon(iconOjoTachado);
				mostrando[0] = false;
			} else {
				// Mostrar contraseña
				txtContrasenia.setEchoChar((char) 0);
				btnOjo.setIcon(iconOjo);
				mostrando[0] = true;
			}
		});
		
		// MENSAJE DE ERROR
		JLabel lblError = new JLabel("");
		lblError.setForeground(new Color(220, 38, 38)); // rojo bonito
		lblError.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
		lblError.setBounds(80, 420, 350, 25);
		panelIzquierdo.add(lblError);
		
		// TIMER PARA OCULTAR EL MENSAJE DE ERROR
		javax.swing.Timer timerError = new javax.swing.Timer(3000, e -> {
			lblError.setText("");
		});
		timerError.setRepeats(false);
		
		
		
		// BOTON INICIAR SESION
		JButton btnLogin = new JButton("INICIAR SESIÓN");
		btnLogin.setBounds(80, 470, 200, 40);
		
		Color verdeBtn = new Color(22, 163, 74);
		Color verdeHover = new Color(18, 140, 64);
		
		btnLogin.setBackground(verdeBtn);
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 16f));
		
		// Quitar bordes feos
		btnLogin.setFocusPainted(false);
		btnLogin.setBorderPainted(false);
		btnLogin.setContentAreaFilled(true);

		panelIzquierdo.add(btnLogin);
		
		// EFECTO DE COLOR POR EL MAUSE
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnLogin.setBackground(verdeHover);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnLogin.setBackground(verdeBtn);
			}
		});
		
		
		// COMPROBAR AUTENTICACION
		btnLogin.addActionListener(e -> {
			String usuario = txtUsuario.getText().trim();
			String pass = new String(txtContrasenia.getPassword());

			String tipoString = controlador.loginTipo(usuario, pass);

			// 2. Verificamos si devolvió NULL (Error)
			if (tipoString == null) {
				// Recuperamos el mensaje de error guardado en la clase Clinica
				lblError.setText(Clinica.getInstance().getUltimoMensajeError());
				timerError.restart();
			} 
			// 3. Si no es NULL, procedemos con el acceso
			else {
				if (tipoString.equals("Administrativo")) {
					timerError.stop();
					Principal ad = new Principal(1, usuario);
					ad.setVisible(true);
					this.dispose();
					
				} else if (tipoString.equals("Doctor")) {
					timerError.stop();
					Principal doc = new Principal(2, usuario);
					doc.setVisible(true);
					this.dispose();
				}
			}
		});
		
		
		// ------------------ PANEL DERECHO (IMAGEN / LOGO) -------------------
		
		// IMAGEL DEL PANEL DERECHO
		ImageIcon imgPanelDerechoIcon = new ImageIcon(
			getClass().getResource("/Imagenes/panelDerecho2.png")
		);
		
		JPanel panelDerecho = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img = imgPanelDerechoIcon.getImage();
				g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			}
		};
		
		panelDerecho.setPreferredSize(new Dimension(420, 0));
		panelDerecho.setLayout(null);
		contentPane.add(panelDerecho, BorderLayout.EAST);
		
		
		// LOGO EN EL PANEL DERECHO
		ImageIcon iconLogoDer = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));

		Image imgDer = iconLogoDer.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
		JLabel lblLogoDer = new JLabel(new ImageIcon(imgDer));

		lblLogoDer.setBounds( (420 - 170)/2, 30, 170, 170 ); 
		panelDerecho.add(lblLogoDer);
		
		
		// NOMBRE DE LA CLINICA DERECHO
		JLabel lblNombreDer = new JLabel("COMPILE SALUD");

		lblNombreDer.setForeground(Color.WHITE);
		lblNombreDer.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 36f));
		lblNombreDer.setBounds(0, 210, 420, 40);
		lblNombreDer.setHorizontalAlignment(JLabel.CENTER);

		panelDerecho.add(lblNombreDer);
	}
}