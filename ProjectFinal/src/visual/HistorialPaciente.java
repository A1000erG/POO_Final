package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Utilidades.FuenteUtil;
import logico.Clinica;
import logico.Consulta;
import logico.Diagnostico;
import logico.Paciente;
import logico.Vacuna;

public class HistorialPaciente extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Paciente pacienteActual;

	private Color colorHeader = new Color(4, 111, 67);
	private Color colorFondo = Color.WHITE;
	private Color colorTexto = new Color(55, 65, 81);
	private Color colorBorde = new Color(220, 220, 220);

	private JTextArea txtAlergias;
	private JTextArea txtAntPatologicos;
	private JTextArea txtAntNoPatologicos;
	private JButton btnGuardarCambios;

	public HistorialPaciente(Paciente paciente) {
		this.pacienteActual = paciente;

		setTitle("Historial Clínico - " + paciente.getNombre());
		setModal(true);
		setBounds(100, 100, 1200, 750);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(colorFondo);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(colorHeader);
		panelSuperior.setLayout(null);
		panelSuperior.setPreferredSize(new Dimension(0, 100));
		contentPane.add(panelSuperior, BorderLayout.NORTH);

		ImageIcon iconLogo = cargarIcono("/Imagenes/logoBlanco.png", 60, 60);
		JLabel lblLogo = new JLabel(iconLogo);
		lblLogo.setBounds(30, 20, 60, 60);
		panelSuperior.add(lblLogo);

		JLabel lblTitulo = new JLabel("HISTORIAL CLÍNICO DEL PACIENTE");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 28f));
		lblTitulo.setBounds(110, 25, 600, 50);
		panelSuperior.add(lblTitulo);

		crearBotonVolver(panelSuperior);

		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(colorFondo);
		panelCentral.setLayout(new BorderLayout());
		panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.add(panelCentral, BorderLayout.CENTER);

		JPanel panelInfo = new JPanel();
		panelInfo.setPreferredSize(new Dimension(300, 0));
		panelInfo.setBackground(new Color(248, 250, 252));
		panelInfo.setBorder(new LineBorder(colorBorde, 1, true));
		panelInfo.setLayout(null);
		panelCentral.add(panelInfo, BorderLayout.WEST);

		cargarTarjetaPaciente(panelInfo);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));

		JPanel panelGeneral = new JPanel();
		panelGeneral.setLayout(null);
		panelGeneral.setBackground(Color.WHITE);
		cargarPanelGeneral(panelGeneral);
		tabbedPane.addTab("Resumen & Antecedentes", null, panelGeneral, "Datos médicos generales");

		JPanel panelConsultas = new JPanel();
		panelConsultas.setLayout(new BorderLayout());
		cargarTablaConsultas(panelConsultas);
		tabbedPane.addTab("Historial de Consultas", null, panelConsultas, "Registro de visitas");

		JPanel panelVacunas = new JPanel();
		panelVacunas.setLayout(new BorderLayout());
		cargarTablaVacunas(panelVacunas);
		tabbedPane.addTab("Esquema de Vacunación", null, panelVacunas, "Inmunizaciones aplicadas");

		JPanel panelSeparador = new JPanel();
		panelSeparador.setBackground(Color.WHITE);
		panelSeparador.setPreferredSize(new Dimension(20, 0));
		panelCentral.add(panelSeparador, BorderLayout.CENTER);

		JPanel panelDerechoContainer = new JPanel(new BorderLayout());
		panelDerechoContainer.setBackground(Color.WHITE);
		panelDerechoContainer.add(tabbedPane, BorderLayout.CENTER);
		panelDerechoContainer.setBorder(new EmptyBorder(0, 20, 0, 0));
		panelCentral.add(panelDerechoContainer, BorderLayout.CENTER);
	}

	private void cargarTarjetaPaciente(JPanel panel) {
		// Foto (Placeholder)
		ImageIcon iconUsuario = cargarIcono("/Imagenes/pacientList.png", 100, 100);
		JLabel lblFoto = new JLabel(iconUsuario);
		lblFoto.setBounds(100, 30, 100, 100);
		panel.add(lblFoto);

		JLabel lblNombre = new JLabel(pacienteActual.getNombre());
		lblNombre.setHorizontalAlignment(JLabel.CENTER);
		lblNombre.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f));
		lblNombre.setForeground(colorHeader);
		lblNombre.setBounds(10, 140, 280, 30);
		panel.add(lblNombre);

		JLabel lblCedula = new JLabel("ID: " + pacienteActual.getCedula());
		lblCedula.setHorizontalAlignment(JLabel.CENTER);
		lblCedula.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 14f));
		lblCedula.setForeground(Color.GRAY);
		lblCedula.setBounds(10, 170, 280, 20);
		panel.add(lblCedula);

		int yStart = 220;
		int gap = 40;

		agregarDatoResumen(panel, "Tipo Sangre:", pacienteActual.getTipoSangre(), yStart);
		agregarDatoResumen(panel, "Peso:", pacienteActual.getPeso() + " Kg", yStart + gap);
		agregarDatoResumen(panel, "Estatura:", pacienteActual.getEstatura() + " m", yStart + gap * 2);
		agregarDatoResumen(panel, "IMC:", String.format("%.2f", pacienteActual.getIMC()), yStart + gap * 3);

		JLabel lblEstadoIMC = new JLabel(interpretarIMC(pacienteActual.getIMC()));
		lblEstadoIMC.setHorizontalAlignment(JLabel.CENTER);
		lblEstadoIMC.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 12f));
		lblEstadoIMC.setForeground(obtenerColorIMC(pacienteActual.getIMC()));
		lblEstadoIMC.setBounds(10, yStart + gap * 3 + 25, 280, 20);
		panel.add(lblEstadoIMC);
	}

	private void agregarDatoResumen(JPanel panel, String titulo, String valor, int y) {
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setBounds(30, y, 100, 20);
		lblTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
		lblTitulo.setForeground(Color.GRAY);
		panel.add(lblTitulo);

		JLabel lblValor = new JLabel(valor == null ? "N/A" : valor);
		lblValor.setBounds(140, y, 150, 20);
		lblValor.setHorizontalAlignment(JLabel.RIGHT);
		lblValor.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 15f));
		lblValor.setForeground(colorTexto);
		panel.add(lblValor);

		JPanel linea = new JPanel();
		linea.setBackground(new Color(230, 230, 230));
		linea.setBounds(30, y + 25, 240, 1);
		panel.add(linea);
	}

	private void cargarPanelGeneral(JPanel panel) {
		JLabel lblAlergias = new JLabel("ALERGIAS CONOCIDAS:");
		lblAlergias.setForeground(new Color(220, 38, 38));
		lblAlergias.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
		lblAlergias.setBounds(20, 20, 300, 20);
		panel.add(lblAlergias);

		txtAlergias = new JTextArea(pacienteActual.getAlergias());
		configurarTextArea(txtAlergias);
		JScrollPane scrollAlergias = new JScrollPane(txtAlergias);
		scrollAlergias.setBounds(20, 50, 780, 60);
		panel.add(scrollAlergias);

		JLabel lblPatologicos = new JLabel("ANTECEDENTES PATOLÓGICOS (Enfermedades crónicas, cirugías):");
		lblPatologicos.setForeground(colorHeader);
		lblPatologicos.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
		lblPatologicos.setBounds(20, 130, 500, 20);
		panel.add(lblPatologicos);

		txtAntPatologicos = new JTextArea(pacienteActual.getAntecedentesPatologicos());
		configurarTextArea(txtAntPatologicos);
		JScrollPane scrollPat = new JScrollPane(txtAntPatologicos);
		scrollPat.setBounds(20, 160, 780, 80);
		panel.add(scrollPat);

		JLabel lblNoPatologicos = new JLabel("ANTECEDENTES NO PATOLÓGICOS (Estilo de vida, hábitos):");
		lblNoPatologicos.setForeground(colorHeader);
		lblNoPatologicos.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
		lblNoPatologicos.setBounds(20, 260, 500, 20);
		panel.add(lblNoPatologicos);

		txtAntNoPatologicos = new JTextArea(pacienteActual.getAntecedentesNoPatologicos());
		configurarTextArea(txtAntNoPatologicos);
		JScrollPane scrollNoPat = new JScrollPane(txtAntNoPatologicos);
		scrollNoPat.setBounds(20, 290, 780, 80);
		panel.add(scrollNoPat);

		btnGuardarCambios = new JButton("Actualizar Antecedentes");
		btnGuardarCambios.setBackground(new Color(21, 129, 191));
		btnGuardarCambios.setForeground(Color.WHITE);
		btnGuardarCambios.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
		btnGuardarCambios.setBounds(580, 400, 220, 40);
		btnGuardarCambios.setFocusPainted(false);
		btnGuardarCambios.addActionListener(e -> guardarAntecedentes());
		panel.add(btnGuardarCambios);
	}

	private void cargarTablaConsultas(JPanel panel) {
		String[] headers = { "Fecha", "Código", "Doctor", "Motivo/Síntomas", "Diagnósticos" };
		DefaultTableModel model = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setColumnIdentifiers(headers);

		if (pacienteActual.getHistorialClinico() != null) {
			for (Consulta c : pacienteActual.getHistorialClinico()) {
				StringBuilder diagnosticosStr = new StringBuilder();
				if (c.getDiagnosticos() != null) {
					for (Diagnostico d : c.getDiagnosticos()) {
						diagnosticosStr.append(d.getNombre()).append(", ");
					}
				}
				String diagFinal = diagnosticosStr.length() > 0
						? diagnosticosStr.substring(0, diagnosticosStr.length() - 2)
						: "Sin diagnóstico";

				model.addRow(new Object[] { c.getCitaAsociada().getFecha().toString(), c.getCodigo(),
						c.getDoctor().getNombre(), c.getSintoma(), diagFinal });
			}
		}

		JTable table = new JTable(model);
		table.setRowHeight(30);
		table.getTableHeader().setBackground(new Color(240, 240, 240));
		table.getTableHeader().setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
		table.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 13f));

		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setPreferredWidth(200);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		panel.add(scroll, BorderLayout.CENTER);
	}

	private void cargarTablaVacunas(JPanel panel) {
		String[] headers = { "Vacuna", "Fecha Aplicación", "Detalle" };
		DefaultTableModel model = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setColumnIdentifiers(headers);

		if (pacienteActual.getVacunasAplicadas() != null) {
			for (Vacuna v : pacienteActual.getVacunasAplicadas()) {
				String fechaApp = "Fecha no registrada";
				String detalle = "Aplicada en consulta";

				if (pacienteActual.getHistorialClinico() != null) {
					for (Consulta c : pacienteActual.getHistorialClinico()) {
						if (c.getDiagnosticos() != null) {
							for (Diagnostico d : c.getDiagnosticos()) {
								if (d.getTratamiento().contains(v.getNombre())) {
									fechaApp = c.getCitaAsociada().getFecha().toString();
								}
							}
						}
					}
				}

				model.addRow(new Object[] { v.getNombre(), fechaApp, detalle });
			}
		}

		JTable table = new JTable(model);
		table.setRowHeight(30);
		table.getTableHeader().setBackground(new Color(240, 240, 240));
		table.getTableHeader().setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
		table.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 13f));

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		panel.add(scroll, BorderLayout.CENTER);
	}

	private void configurarTextArea(JTextArea txt) {
		txt.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 14f));
		txt.setLineWrap(true);
		txt.setWrapStyleWord(true);
		txt.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	private void guardarAntecedentes() {
		pacienteActual.setAlergias(txtAlergias.getText());
		pacienteActual.setAntecedentesPatologicos(txtAntPatologicos.getText());
		pacienteActual.setAntecedentesNoPatologicos(txtAntNoPatologicos.getText());

		Clinica.getInstance().guardarDatos();
		JOptionPane.showMessageDialog(this, "Información clínica actualizada.");
	}

	private void crearBotonVolver(JPanel panel) {
		JPanel btnVolver = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillOval(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
		btnVolver.setOpaque(false);
		btnVolver.setLayout(null);
		btnVolver.setBounds(1100, 20, 60, 60);
		btnVolver.setBackground(new Color(6, 140, 85));

		btnVolver.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
				btnVolver.setBackground(new Color(34, 197, 94));
				btnVolver.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnVolver.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				btnVolver.setBackground(new Color(6, 140, 85));
				btnVolver.repaint();
			}
		});

		ImageIcon iconoFlecha = cargarIcono("/Imagenes/FlechaAtras.png", 30, 30);
		JLabel etiquetaFlecha = new JLabel(iconoFlecha);
		if (iconoFlecha.getImage() == null)
			etiquetaFlecha.setText("<");
		etiquetaFlecha.setForeground(Color.WHITE);
		etiquetaFlecha.setBounds(15, 15, 30, 30);
		btnVolver.add(etiquetaFlecha);

		panel.add(btnVolver);
	}

	private ImageIcon cargarIcono(String ruta, int w, int h) {
		java.net.URL url = getClass().getResource(ruta);
		if (url != null) {
			return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
		}
		return new ImageIcon();
	}

	private String interpretarIMC(float imc) {
		if (imc <= 0)
			return "Sin registro";
		if (imc < 18.5)
			return "Bajo Peso";
		if (imc < 24.9)
			return "Peso Normal";
		if (imc < 29.9)
			return "Sobrepeso";
		return "Obesidad";
	}

	private Color obtenerColorIMC(float imc) {
		if (imc <= 0)
			return Color.GRAY;
		if (imc < 18.5)
			return new Color(59, 130, 246);
		if (imc < 24.9)
			return new Color(22, 163, 74);
		if (imc < 29.9)
			return new Color(234, 179, 8);
		return new Color(220, 38, 38);
	}
}