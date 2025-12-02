package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import Utilidades.FuenteUtil;
import logico.Cita;
import logico.Clinica;
import logico.Doctor;

public class ListarCitas extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JCalendar calendario;
    
    // Componentes del Panel Derecho
    private JTextField txtId;
    private JTextField txtPaciente;
    private JTextField txtDoctor;
    private JTextField txtFecha;
    private JComboBox<String> cbEstado;
    
    private JButton btnAtender;
    private JButton btnGuardar;
    private JButton btnVerTodas;
    
    // Colores del Estilo ListarDoctores
    private Color colorHeader = new Color(4, 111, 67);
    private Color colorFondoDetalle = new Color(245, 245, 245);
    private Color colorBorde = new Color(220, 220, 220);
    private Color colorTexto = new Color(55, 65, 81);

    private Doctor doctorActual = null;
    private LocalDate fechaSeleccionada = null;
    private Cita citaSeleccionada = null;

    public ListarCitas(Doctor doctorLogueado) {
        this.doctorActual = doctorLogueado;
        
        setTitle("Gestión de Citas Médicas");
        setModal(true);
        setBounds(100, 100, 1366, 768);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // ------------------ PANEL SUPERIOR (HEADER) -------------------
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(colorHeader);
        panelSuperior.setLayout(null);
        panelSuperior.setPreferredSize(new Dimension(0, 120));
        contentPane.add(panelSuperior, BorderLayout.NORTH);
        
        ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
        Image imgLogoEscalada = iconLogo.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imgLogoEscalada));
        lblLogo.setBounds(40, 25, 70, 70);
        panelSuperior.add(lblLogo);
        
        JLabel lblTitulo = new JLabel(doctorActual == null ? "GESTIÓN DE CITAS" : "MIS CONSULTAS PROGRAMADAS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 34f));
        lblTitulo.setBounds(130, 35, 800, 50);
        panelSuperior.add(lblTitulo);

        // ------------------ PANEL CENTRAL -------------------
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setLayout(null);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.add(panelCentral, BorderLayout.CENTER);

        int margenX = 40;
        

        // === SECCIÓN IZQUIERDA (CALENDARIO Y TABLA) ===
        
        // 1. Calendario (Filtro Visual)
        JLabel lblCal = new JLabel("Filtrar por Fecha:");
        lblCal.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 16f));
        lblCal.setForeground(colorTexto);
        lblCal.setBounds(margenX, 10, 200, 20);
        panelCentral.add(lblCal);

        JPanel panelCalContenedor = new JPanel(new BorderLayout());
        panelCalContenedor.setBounds(margenX, 40, 350, 200);
        panelCalContenedor.setBorder(new LineBorder(colorBorde, 1, true));
        
        calendario = new JCalendar();
        calendario.setTodayButtonVisible(true);
        calendario.setWeekOfYearVisible(false);
        calendario.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Date date = calendario.getDate();
                fechaSeleccionada = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                cargarCitasEnTabla();
            }
        });
        panelCalContenedor.add(calendario, BorderLayout.CENTER);
        panelCentral.add(panelCalContenedor);
        
        btnVerTodas = new JButton("Ver Todas las Fechas");
        btnVerTodas.setBounds(margenX + 370, 40, 200, 40);
        btnVerTodas.setBackground(new Color(21, 129, 191));
        btnVerTodas.setForeground(Color.WHITE);
        btnVerTodas.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 12f));
        btnVerTodas.setFocusPainted(false);
        btnVerTodas.addActionListener(e -> {
            fechaSeleccionada = null;
            cargarCitasEnTabla();
        });
        panelCentral.add(btnVerTodas);

        // 2. Tabla de Resultados
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(40, 260, 820, 272);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        panelCentral.add(scrollPane);

        String[] headers = { "ID", "Fecha", "Hora", "Paciente", "Doctor", "Estado" };
        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(headers);
        
        tablaCitas = new JTable(modeloTabla) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
                return false; // Tabla NO editable, edición en panel lateral
            }
        };
        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCitas.setRowHeight(35);
        tablaCitas.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
        
        tablaCitas.getTableHeader().setBackground(colorHeader);
        tablaCitas.getTableHeader().setForeground(Color.WHITE);
        tablaCitas.getTableHeader().setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
        
        tablaCitas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarDetalleCita();
            }
        });
        scrollPane.setViewportView(tablaCitas);

        // === SECCIÓN DERECHA (DETALLES Y ACCIONES) ===
        
        JPanel panelDetalle = new JPanel();
        panelDetalle.setBackground(colorFondoDetalle);
        panelDetalle.setBorder(new LineBorder(colorBorde, 1, true));
        panelDetalle.setBounds(900, 30, 400, 530);
        panelDetalle.setLayout(null);
        panelCentral.add(panelDetalle);

        JLabel lblDetalleTitulo = new JLabel("DETALLES DE LA CITA");
        lblDetalleTitulo.setHorizontalAlignment(JLabel.CENTER);
        lblDetalleTitulo.setForeground(colorHeader);
        lblDetalleTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f));
        lblDetalleTitulo.setBounds(20, 20, 360, 30);
        panelDetalle.add(lblDetalleTitulo);

        int yStart = 80; 
        int gap = 55;
        
        // Campos de solo lectura
        crearCampoDetalle(panelDetalle, "ID Cita:", yStart, txtId = new JTextField());
        txtId.setEnabled(false);
        
        crearCampoDetalle(panelDetalle, "Paciente:", yStart + gap, txtPaciente = new JTextField());
        txtPaciente.setEnabled(false);
        
        crearCampoDetalle(panelDetalle, "Doctor:", yStart + gap*2, txtDoctor = new JTextField());
        txtDoctor.setEnabled(false);
        
        crearCampoDetalle(panelDetalle, "Fecha y Hora:", yStart + gap*3, txtFecha = new JTextField()); // Usamos uno para ambos visualmente o dos
        txtFecha.setEnabled(false);

        // Selector de Estado (Lo Amarillo en tu imagen)
        JLabel lblEstado = new JLabel("ESTADO (Modificable):");
        lblEstado.setBounds(50, yStart + gap*4, 300, 20);
        lblEstado.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        lblEstado.setForeground(new Color(255, 140, 0)); // Naranja para resaltar
        panelDetalle.add(lblEstado);

        cbEstado = new JComboBox<>();
        cbEstado.addItem("Pendiente");
        cbEstado.addItem("Realizada");
        cbEstado.addItem("Rechazada"); // Rechazada/Cancelada
        cbEstado.setBounds(50, yStart + gap*4 + 20, 300, 35);
        cbEstado.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 14f));
        cbEstado.setBackground(Color.WHITE);
        cbEstado.setEnabled(false);
        panelDetalle.add(cbEstado);

        // --- BOTONES DE ACCIÓN ---
        int btnY = 440;
        
        // Botón Modificar/Guardar (Rojo/Naranja)
        btnGuardar = new JButton("GUARDAR ESTADO");
        btnGuardar.setBounds(50, btnY, 145, 40);
        btnGuardar.setBackground(new Color(255, 140, 0)); // Naranja
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 11f));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setEnabled(false);
        btnGuardar.addActionListener(e -> guardarCambios());
        panelDetalle.add(btnGuardar);

        // Botón Atender (Verde) - Solo visible para doctores
        btnAtender = new JButton("ATENDER");
        btnAtender.setBounds(205, btnY, 145, 40);
        btnAtender.setBackground(new Color(22, 163, 74));
        btnAtender.setForeground(Color.WHITE);
        btnAtender.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 12f));
        btnAtender.setFocusPainted(false);
        btnAtender.setEnabled(false);
        btnAtender.setVisible(doctorActual != null); // Solo Doctores
        btnAtender.addActionListener(e -> abrirConsulta());
        panelDetalle.add(btnAtender);

        // --- BOTÓN VOLVER CIRCULAR ---
        crearBotonVolver(panelCentral);

        cargarCitasEnTabla();
    }

    private void crearCampoDetalle(JPanel panel, String titulo, int y, JTextField campo) {
        JLabel lbl = new JLabel(titulo);
        lbl.setBounds(50, y, 300, 20);
        lbl.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lbl.setForeground(new Color(100, 100, 100));
        panel.add(lbl);

        campo.setBounds(50, y + 20, 300, 35);
        campo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        campo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 14f));
        campo.setBackground(colorFondoDetalle);
        campo.setDisabledTextColor(Color.DARK_GRAY);
        panel.add(campo);
    }

    private void crearBotonVolver(JPanel panel) {
        JPanel btnVolver = new JPanel() {
            /**
			 * 
			 */
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
        btnVolver.setBounds(40, 538, 60, 60); // Abajo a la izquierda
        btnVolver.setBackground(colorHeader);

        btnVolver.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dispose(); }
            public void mouseEntered(MouseEvent e) { btnVolver.setBackground(new Color(6, 140, 85)); btnVolver.repaint(); }
            public void mouseExited(MouseEvent e) { btnVolver.setBackground(colorHeader); btnVolver.repaint(); }
        });

        ImageIcon iconFlecha = new ImageIcon(getClass().getResource("/Imagenes/FlechaAtras.png"));
        JLabel lblFlecha = new JLabel(new ImageIcon(iconFlecha.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        lblFlecha.setBounds(15, 15, 30, 30);
        btnVolver.add(lblFlecha);
        panel.add(btnVolver);
    }

    // --- LÓGICA ---

    private void cargarCitasEnTabla() {
        modeloTabla.setRowCount(0);
        Object[] row = new Object[6];
        ArrayList<Cita> lista = Clinica.getInstance().getCitas();
        if(lista == null) return;

        for (Cita c : lista) {
            boolean mostrar = true;
            // Filtro Doctor
            if(doctorActual != null && !c.getDoctor().getUsuario().equalsIgnoreCase(doctorActual.getUsuario())) {
                mostrar = false;
            }
            // Filtro Fecha
            if(fechaSeleccionada != null && !c.getFecha().isEqual(fechaSeleccionada)) {
                mostrar = false;
            }

            if(mostrar) {
                row[0] = c.getIdCita();
                row[1] = c.getFecha();
                row[2] = c.getHora();
                row[3] = c.getPaciente().getNombre();
                row[4] = c.getDoctor().getNombre();
                row[5] = c.getEstado();
                modeloTabla.addRow(row);
            }
        }
        limpiarDetalle();
    }

    private void cargarDetalleCita() {
        int row = tablaCitas.getSelectedRow();
        if(row != -1) {
            String idStr = tablaCitas.getValueAt(row, 0).toString();
            citaSeleccionada = Clinica.getInstance().buscarCitaPorId(idStr);
            
            if(citaSeleccionada != null) {
                txtId.setText(String.valueOf(citaSeleccionada.getIdCita()));
                txtPaciente.setText(citaSeleccionada.getPaciente().getNombre());
                txtDoctor.setText(citaSeleccionada.getDoctor().getNombre());
                txtFecha.setText(citaSeleccionada.getFecha().toString() + " - " + citaSeleccionada.getHora());
                
                // Configurar Combo
                cbEstado.setSelectedItem(citaSeleccionada.getEstado());
                
                // Habilitar controles según reglas
                boolean esRealizada = "Realizada".equalsIgnoreCase(citaSeleccionada.getEstado());
                
                cbEstado.setEnabled(!esRealizada); // No cambiar si ya está realizada
                btnGuardar.setEnabled(!esRealizada);
                
                // Botón Atender: Solo si es doctor, es pendiente y la fecha es hoy (opcional, aqui dejamos abierta la fecha)
                boolean puedeAtender = doctorActual != null && "Pendiente".equalsIgnoreCase(citaSeleccionada.getEstado());
                btnAtender.setEnabled(puedeAtender);
            }
        }
    }

    private void limpiarDetalle() {
        txtId.setText("");
        txtPaciente.setText("");
        txtDoctor.setText("");
        txtFecha.setText("");
        cbEstado.setSelectedIndex(0);
        cbEstado.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnAtender.setEnabled(false);
        citaSeleccionada = null;
    }

    private void guardarCambios() {
        if(citaSeleccionada == null) return;
        
        String nuevoEstado = (String) cbEstado.getSelectedItem();
        
        if (citaSeleccionada.getEstado().equalsIgnoreCase("Realizada")) {
             JOptionPane.showMessageDialog(this, "No se puede modificar una cita ya Realizada.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Cambiar estado a: " + nuevoEstado + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            citaSeleccionada.setEstado(nuevoEstado);
            Clinica.getInstance().guardarDatos();
            JOptionPane.showMessageDialog(this, "Estado actualizado.");
            cargarCitasEnTabla();
        }
    }

    private void abrirConsulta() {
        if(citaSeleccionada != null) {
            dispose(); // Cierra el listado para enfocarse en la consulta
            try {
                RegConsulta reg = new RegConsulta(citaSeleccionada);
                reg.setModal(true);
                reg.setVisible(true);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}