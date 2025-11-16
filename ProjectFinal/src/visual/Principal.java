package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Principal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Dimension dimPrincipal;
	private static Color paleteBlue = new Color(55,65,81);
	private static Color paleteDarkGreen = new Color(22,163, 74);
	private static Color paleteLightGreen = new Color(74, 222, 128);
	private static Color paleteRareWhite = new Color(247, 250, 252);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Principal() {
		dimPrincipal = getToolkit().getScreenSize();
		setResizable(false);
		setTitle("Ventana principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(dimPrincipal.width+10,dimPrincipal.height-38);
		setLocationRelativeTo(null);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBounds(0, 0, 240, 701);
		optionPanel.setBackground(new Color(55,65,81));
		contentPane.add(optionPanel);
		optionPanel.setLayout(null);
		
		JLabel lbLogo = new JLabel("");
		lbLogo.setBounds(82, 26, 76, 76);
		lbLogo.setIcon(new ImageIcon("images/logoP.png"));
		optionPanel.add(lbLogo);
		
		JButton btnRegPaciente = new JButton("New button");
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
		btnReportes.setBackground(paleteBlue);
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
				
		JLabel lblNombreUser = new JLabel("Nombre de Usuario");
		lblNombreUser.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNombreUser.setBounds(846, 37, 153, 14);
		infoUserPanel.add(lblNombreUser);
		
		JLabel lblRolUser = new JLabel("Rol (doctor o admin)");
		lblRolUser.setBounds(846, 62, 119, 14);
		infoUserPanel.add(lblRolUser);
		
		JLabel lblFotoUser = new JLabel("");
		lblFotoUser.setBounds(1020, 11, 90, 90);
		lblFotoUser.setIcon(new ImageIcon("images/useResi.png"));
		infoUserPanel.add(lblFotoUser);
	}
}
