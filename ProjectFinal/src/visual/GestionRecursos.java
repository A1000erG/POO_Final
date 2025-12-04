package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import Utilidades.FuenteUtil;
import logico.Clinica;
import logico.Enfermedad;
import logico.Vacuna;

public class GestionRecursos extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private JTable tableVacunas;
	private DefaultTableModel modelVacunas;
	private JButton btnAgregarStock;
	private JButton btnNuevaVacuna;
	private JButton btnVerSolicitudes;

	private JTable tableEnfermedades;
	private DefaultTableModel modelEnfermedades;
	private JButton btnNuevaEnfermedad;
	private JButton btnCambiarVigilancia;

	private Color colorPrimario = new Color(21, 129, 191);
	private Color colorVerde = new Color(22, 163, 74);
	private Color colorRojo = new Color(220, 38, 38);
	private Color colorBlanco = Color.WHITE;
	private Color colorFondoGris = new Color(245, 245, 245);

	private Font fuenteHeader = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f);
	private Font fuenteNormal = FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 13f);
	private Font fuenteTitulo = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 24f);

	public static void main(String[] args) {
		try {
			GestionRecursos dialog = new GestionRecursos();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GestionRecursos() {
		setTitle("Gestión de Recursos Clínicos");
		setBounds(100, 100, 1000, 700);
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);

		getContentPane().setLayout(new BorderLayout());

		JPanel panelHeader = new JPanel();
		panelHeader.setBackground(colorPrimario);
		panelHeader.setPreferredSize(new Dimension(0, 80));
		panelHeader.setLayout(null);
		getContentPane().add(panelHeader, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("GESTIÓN DE RECURSOS");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(fuenteTitulo);
		lblTitulo.setBounds(30, 20, 400, 40);
		panelHeader.add(lblTitulo);

		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(colorFondoGris);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(fuenteHeader);
		tabbedPane.setBackground(colorBlanco);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);

		JPanel panelVacunas = crearPanelVacunas();
		tabbedPane.addTab("  Inventario de Vacunas  ", null, panelVacunas, null);

		JPanel panelEnfermedades = crearPanelEnfermedades();
		tabbedPane.addTab("  Vigilancia Epidemiológica  ", null, panelEnfermedades, null);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBackground(colorBlanco);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setBackground(new Color(240, 240, 240));
		btnCerrar.setFont(fuenteNormal);
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);

		try {
			cargarVacunas();
			cargarEnfermedades();
		} catch (Exception e) {
			System.out.println("Modo diseño: No se cargaron datos.");
		}
	}

	private JPanel crearPanelVacunas() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(colorBlanco);
		panel.setBorder(new LineBorder(new Color(220, 220, 220), 1));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 30, 650, 480);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		panel.add(scrollPane);

		String[] headers = { "ID", "Nombre de Vacuna", "Stock Disponible", "Fecha Caducidad" };

		modelVacunas = new DefaultTableModel(null, headers) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableVacunas = new JTable(modelVacunas);
		configurarTabla(tableVacunas);
		scrollPane.setViewportView(tableVacunas);

		JPanel panelAcciones = new JPanel();
		panelAcciones.setLayout(null);
		panelAcciones.setBounds(700, 30, 250, 480);
		panelAcciones.setBackground(colorBlanco);
		panel.add(panelAcciones);

		JLabel lblAcciones = new JLabel("Acciones");
		lblAcciones.setFont(fuenteHeader);
		lblAcciones.setForeground(colorPrimario);
		lblAcciones.setBounds(0, 0, 200, 30);
		panelAcciones.add(lblAcciones);

		btnNuevaVacuna = crearBoton("Registrar Nueva Vacuna", 0, 40, colorPrimario);
		btnNuevaVacuna.addActionListener(e -> registrarNuevaVacuna());
		panelAcciones.add(btnNuevaVacuna);

		btnAgregarStock = crearBoton("Reponer Stock (+)", 0, 90, colorVerde);
		btnAgregarStock.addActionListener(e -> agregarStock());
		panelAcciones.add(btnAgregarStock);

		btnVerSolicitudes = crearBoton("Ver Solicitudes Pendientes", 0, 140, new Color(255, 140, 0));
		btnVerSolicitudes.addActionListener(e -> verSolicitudes());
		panelAcciones.add(btnVerSolicitudes);

		return panel;
	}

	private JPanel crearPanelEnfermedades() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(colorBlanco);
		panel.setBorder(new LineBorder(new Color(220, 220, 220), 1));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 30, 650, 480);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		panel.add(scrollPane);

		String[] headers = { "ID", "Nombre Enfermedad", "Estado de Vigilancia" };

		modelEnfermedades = new DefaultTableModel(null, headers) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableEnfermedades = new JTable(modelEnfermedades);
		configurarTabla(tableEnfermedades);
		scrollPane.setViewportView(tableEnfermedades);

		JPanel panelAcciones = new JPanel();
		panelAcciones.setLayout(null);
		panelAcciones.setBounds(700, 30, 250, 480);
		panelAcciones.setBackground(colorBlanco);
		panel.add(panelAcciones);

		JLabel lblAcciones = new JLabel("Acciones");
		lblAcciones.setFont(fuenteHeader);
		lblAcciones.setForeground(colorPrimario);
		lblAcciones.setBounds(0, 0, 200, 30);
		panelAcciones.add(lblAcciones);

		btnNuevaEnfermedad = crearBoton("Registrar Enfermedad", 0, 40, colorPrimario);
		btnNuevaEnfermedad.addActionListener(e -> registrarNuevaEnfermedad());
		panelAcciones.add(btnNuevaEnfermedad);

		btnCambiarVigilancia = crearBoton("Cambiar Estado Vigilancia", 0, 90, colorRojo);
		btnCambiarVigilancia.addActionListener(e -> cambiarEstadoVigilancia());
		panelAcciones.add(btnCambiarVigilancia);

		return panel;
	}

	private void cargarVacunas() {
		modelVacunas.setRowCount(0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		if (Clinica.getInstance().getVacunas() != null) {
			for (Vacuna v : Clinica.getInstance().getVacunas()) {
				String fechaMostrar = "N/A";
				if (v.getFechaCaducidad() != null) {
					fechaMostrar = v.getFechaCaducidad().format(formatter);
				}

				modelVacunas.addRow(new Object[] { v.getId(), v.getNombre(), v.getCantidadDisponible(), fechaMostrar });
			}
		}
		tableVacunas.revalidate();
		tableVacunas.repaint();
	}

	private void registrarNuevaVacuna() {
		JTextField txtNombre = new JTextField();
		JTextField txtStock = new JTextField();

		MaskFormatter formatoFecha = null;
		try {
			formatoFecha = new MaskFormatter("##/##/####");
			formatoFecha.setPlaceholderCharacter('_');
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JFormattedTextField txtFechaCaducidad = new JFormattedTextField(formatoFecha);

		Object[] message = { "Nombre de la Vacuna:", txtNombre, "Stock Inicial:", txtStock,
				"Fecha de Caducidad (DD/MM/AAAA):", txtFechaCaducidad };

		int option = JOptionPane.showConfirmDialog(this, message, "Nueva Vacuna", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			String nombre = txtNombre.getText().trim();
			String stockStr = txtStock.getText().trim();
			String fecha = txtFechaCaducidad.getText();

			boolean fechaValida = fecha != null && !fecha.contains("_");

			if (nombre.isEmpty() || stockStr.isEmpty() || !fechaValida) {
				JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios y la fecha debe estar completa.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				int stock = Integer.parseInt(stockStr);
				if (stock < 0) {
					JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				ArrayList<Vacuna> lista = Clinica.getInstance().getVacunas();
				if (lista != null) {
					for (Vacuna v : lista) {
						if (v.getNombre().equalsIgnoreCase(nombre)) {
							JOptionPane.showMessageDialog(this, "Ya existe una vacuna con ese nombre.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}

				int nuevoId = 1;
				if (lista != null && !lista.isEmpty()) {
					for (Vacuna v : lista) {
						if (v.getId() >= nuevoId) {
							nuevoId = v.getId() + 1;
						}
					}
				}

				try {
					Vacuna nueva = new Vacuna();
					nueva.setId(nuevoId);
					nueva.setNombre(nombre);
					nueva.setCantidadDisponible(stock);

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					LocalDate fechaDate = LocalDate.parse(fecha, formatter);
					nueva.setFechaCaducidad(fechaDate);

					Clinica.getInstance().getVacunas().add(nueva);
					Clinica.getInstance().guardarDatos();

					cargarVacunas();
					JOptionPane.showMessageDialog(this, "Vacuna registrada exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Error al crear objeto Vacuna: " + ex.getMessage(),
							"Error Interno", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "El stock debe ser un número entero válido.", "Error de Formato",
						JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error Crítico",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void agregarStock() {
		int selectedRow = tableVacunas.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una vacuna de la lista.", "Aviso",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			int idVacuna = Integer.parseInt(modelVacunas.getValueAt(selectedRow, 0).toString());
			Vacuna vacunaSel = null;

			if (Clinica.getInstance().getVacunas() != null) {
				for (Vacuna v : Clinica.getInstance().getVacunas()) {
					if (v.getId() == idVacuna) {
						vacunaSel = v;
						break;
					}
				}
			}

			if (vacunaSel != null) {
				String input = JOptionPane.showInputDialog(this,
						"Ingrese cantidad a agregar para: " + vacunaSel.getNombre(), "Reponer Stock",
						JOptionPane.QUESTION_MESSAGE);
				if (input != null && !input.isEmpty()) {
					try {
						int cant = Integer.parseInt(input);
						if (cant > 0) {
							vacunaSel.setCantidadDisponible(vacunaSel.getCantidadDisponible() + cant);
							Clinica.getInstance().guardarDatos();
							cargarVacunas();
							JOptionPane.showMessageDialog(this, "Stock actualizado correctamente.");
						} else {
							JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error al seleccionar la vacuna.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void verSolicitudes() {
		ArrayList<String> solicitudes = Clinica.getInstance().getSolicitudesVacunas();
		if (solicitudes == null || solicitudes.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No hay solicitudes pendientes.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			StringBuilder sb = new StringBuilder("Solicitudes Pendientes:\n\n");
			for (String s : solicitudes) {
				sb.append("• ").append(s).append("\n");
			}
			sb.append("\n¿Desea marcar todas como atendidas y limpiar la lista?");

			javax.swing.JTextArea areaTexto = new javax.swing.JTextArea(sb.toString());
			areaTexto.setEditable(false);
			areaTexto.setRows(10);
			areaTexto.setColumns(30);
			areaTexto.setBackground(new Color(240, 240, 240));
			JScrollPane scrollMsg = new JScrollPane(areaTexto);

			int opt = JOptionPane.showConfirmDialog(this, scrollMsg, "Gestión de Solicitudes",
					JOptionPane.YES_NO_OPTION);
			if (opt == JOptionPane.YES_OPTION) {
				Clinica.getInstance().limpiarSolicitudes();
				Clinica.getInstance().guardarDatos();
				JOptionPane.showMessageDialog(this, "Lista de solicitudes limpiada.");
			}
		}
	}

	private void cargarEnfermedades() {
		modelEnfermedades.setRowCount(0);
		if (Clinica.getInstance().getCatalogoEnfermedades() != null) {
			for (Enfermedad e : Clinica.getInstance().getCatalogoEnfermedades()) {
				modelEnfermedades.addRow(new Object[] { e.getIdEnfermedad(), e.getNombre(),
						e.isEsBajoVigilancia() ? "EN VIGILANCIA" : "Normal" });
			}
		}
		tableEnfermedades.revalidate();
		tableEnfermedades.repaint();
	}

	private void registrarNuevaEnfermedad() {
		JTextField txtNombre = new JTextField();
		JCheckBox chkVigilancia = new JCheckBox("¿Bajo Vigilancia Epidemiológica?");

		Object[] message = { "Nombre de la Enfermedad:", txtNombre, "Estado:", chkVigilancia };

		int option = JOptionPane.showConfirmDialog(this, message, "Nueva Enfermedad", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			String nombre = txtNombre.getText().trim();
			if (nombre.isEmpty()) {
				JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (Clinica.getInstance().buscarEnfermedadPorNombre(nombre) != null) {
				JOptionPane.showMessageDialog(this, "Esta enfermedad ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int nuevoId = 1;
			ArrayList<Enfermedad> lista = Clinica.getInstance().getCatalogoEnfermedades();
			if (lista != null && !lista.isEmpty()) {
				for (Enfermedad e : lista) {
					if (e.getIdEnfermedad() >= nuevoId) {
						nuevoId = e.getIdEnfermedad() + 1;
					}
				}
			}

			Enfermedad nueva = new Enfermedad(nuevoId, nombre, chkVigilancia.isSelected());
			Clinica.getInstance().insertarEnfermedad(nueva);
			Clinica.getInstance().guardarDatos();
			cargarEnfermedades();
			JOptionPane.showMessageDialog(this, "Enfermedad registrada exitosamente.");
		}
	}

	private void cambiarEstadoVigilancia() {
		int selectedRow = tableEnfermedades.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una enfermedad.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			int id = Integer.parseInt(modelEnfermedades.getValueAt(selectedRow, 0).toString());
			Enfermedad enfSel = null;
			if (Clinica.getInstance().getCatalogoEnfermedades() != null) {
				for (Enfermedad e : Clinica.getInstance().getCatalogoEnfermedades()) {
					if (e.getIdEnfermedad() == id) {
						enfSel = e;
						break;
					}
				}
			}

			if (enfSel != null) {
				boolean nuevoEstado = !enfSel.isEsBajoVigilancia();
				enfSel.setEsBajoVigilancia(nuevoEstado);
				cargarEnfermedades();
				Clinica.getInstance().guardarDatos();
				String estadoStr = nuevoEstado ? "BAJO VIGILANCIA" : "SIN VIGILANCIA";
				JOptionPane.showMessageDialog(this, "Estado actualizado a: " + estadoStr);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al actualizar estado.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void configurarTabla(JTable tabla) {
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.setRowHeight(30);
		tabla.getTableHeader().setFont(fuenteHeader);
		tabla.getTableHeader().setBackground(colorPrimario);
		tabla.getTableHeader().setForeground(colorBlanco);
		tabla.setFont(fuenteNormal);
		tabla.setShowVerticalLines(false);
		tabla.setGridColor(new Color(230, 230, 230));
	}

	private JButton crearBoton(String texto, int x, int y, Color bg) {
		JButton btn = new JButton(texto);
		btn.setBounds(x, y, 250, 45);
		btn.setBackground(bg);
		btn.setForeground(colorBlanco);
		btn.setFont(fuenteHeader);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(bg.darker());
			}

			public void mouseExited(MouseEvent e) {
				btn.setBackground(bg);
			}
		});
		return btn;
	}
}