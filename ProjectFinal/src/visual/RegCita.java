package visual;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

public class RegCita extends JFrame {

	private JPanel contentPane;
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtCell;
	private JDateChooser calendario;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegCita frame = new RegCita();
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
	public RegCita() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegCita.class.getResource("/Imagenes/logo.png")));
		setTitle("Registro de Citas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 475, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel contenidoPanel = new JPanel();
		contentPane.add(contenidoPanel, BorderLayout.CENTER);
		contenidoPanel.setLayout(null);
		
		calendario = new JDateChooser();
		calendario.setBounds(199, 226, 142, 20);
		contenidoPanel.add(calendario);
		
		JLabel lblCmbBox = new JLabel("Escoja un especialista:");
		lblCmbBox.setBounds(10, 11, 202, 20);
		contenidoPanel.add(lblCmbBox);
		
		JComboBox cmbEspecialista = new JComboBox();
		cmbEspecialista.setBounds(10, 42, 429, 27);
		contenidoPanel.add(cmbEspecialista);
		
		JLabel lblFecha = new JLabel("Seleccione la fecha:");
		lblFecha.setBounds(10, 311, 202, 20);
		contenidoPanel.add(lblFecha);
		
		JLabel lblDatoSolicitante = new JLabel("Datos del solicitante:");
		lblDatoSolicitante.setBounds(10, 102, 202, 20);
		contenidoPanel.add(lblDatoSolicitante);
		
		JLabel lblCedula = new JLabel("C\u00E9dula: ");
		lblCedula.setBounds(10, 133, 99, 20);
		contenidoPanel.add(lblCedula);
		
		txtCedula = new JTextField();
		txtCedula.setBounds(10, 155, 99, 20);
		contenidoPanel.add(txtCedula);
		txtCedula.setColumns(10);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(190, 133, 99, 20);
		contenidoPanel.add(lblNombre);
		
		txtNombre = new JTextField();
		lblNombre.setLabelFor(txtNombre);
		txtNombre.setBounds(190, 152, 99, 20);
		contenidoPanel.add(txtNombre);
		txtNombre.setColumns(10);
		
		JRadioButton rdbtnMasculino = new JRadioButton("M");
		rdbtnMasculino.setBounds(317, 154, 51, 23);
		contenidoPanel.add(rdbtnMasculino);
		
		JRadioButton rdbtnFemenino = new JRadioButton("F");
		rdbtnFemenino.setBounds(372, 152, 51, 23);
		contenidoPanel.add(rdbtnFemenino);
		
		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(317, 136, 46, 14);
		contenidoPanel.add(lblSexo);
		
		JLabel lblNewLabel = new JLabel("Tel\u00E9fono:");
		lblNewLabel.setBounds(10, 201, 99, 14);
		contenidoPanel.add(lblNewLabel);
		
		txtCell = new JTextField();
		txtCell.setBounds(10, 226, 86, 20);
		contenidoPanel.add(txtCell);
		txtCell.setColumns(10);
	}
}
