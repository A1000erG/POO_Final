package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

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
					//frame.setUndecorated(true);
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
	    
	    // TÍTULO "INICIAR SESIÓN"
        JLabel lblTitulo = new JLabel("INICIAR SESIÓN");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 32));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setBorder(new EmptyBorder(80, 0, 40, 0));
        panelTitulo.add(lblTitulo);

        panelIzquierdo.add(panelTitulo, BorderLayout.NORTH);

	    // ------------------ PANEL DERECHO (IMAGEN / LOGO) -------------------
	    JPanel panelDerecho = new JPanel();
	    panelDerecho.setBackground(new Color(22,163, 74));
	    panelDerecho.setPreferredSize(new Dimension(420, 0));
	    contentPane.add(panelDerecho, BorderLayout.EAST);
		
	}

}
