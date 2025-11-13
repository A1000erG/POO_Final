package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Login() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		
		
		// ------------------ PANEL IZQUIERDO (FORMULARIO) -------------------
	    JPanel panelIzquierdo = new JPanel();
	    panelIzquierdo.setBackground(Color.WHITE);
	    panelIzquierdo.setLayout(new BorderLayout());
	    contentPane.add(panelIzquierdo, BorderLayout.CENTER);

	    // ------------------ PANEL DERECHO (IMAGEN / LOGO) -------------------
	    JPanel panelDerecho = new JPanel();
	    panelDerecho.setBackground(new Color(0, 155, 198)); // Azul tipo login de tu imagen
	    panelDerecho.setPreferredSize(new Dimension(420, 0)); // Ancho fijo
	    contentPane.add(panelDerecho, BorderLayout.EAST);
		
	}

}
