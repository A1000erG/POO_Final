package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField; // Importado
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
import javax.swing.text.MaskFormatter; // Importado

import Utilidades.FuenteUtil;
import logico.Clinica;
import logico.Paciente;
import logico.Personal;

public class ListarPacientes extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtBuscador;
    private JTable table;
    private DefaultTableModel model;
    private Object[] rows;
    
    // Campos específicos
    private JTextField txtNombre;
    private JFormattedTextField txtCedula; // Cambiado a Formatted
    private JFormattedTextField txtTelefono; // Cambiado a Formatted
    private JTextField txtFechaNac;
    
    private JButton btnModificar;
    private JButton btnEstado;
    
    private Paciente selectedPaciente = null;
    private boolean buscar = false;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ListarPacientes dialog = new ListarPacientes(null);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    public ListarPacientes(Personal usuarioLogueado) {
        setTitle("Gestión de Pacientes");
        setModal(true);
        setBounds(100, 100, 1366, 768);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new java.awt.Color(4, 111, 67));
        panelSuperior.setLayout(null);
        panelSuperior.setPreferredSize(new java.awt.Dimension(0, 120));
        contentPane.add(panelSuperior, BorderLayout.NORTH);
        
        ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
        java.awt.Image imgLogoEscalada = iconLogo.getImage().getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imgLogoEscalada));
        lblLogo.setBounds(40, 25, 70, 70);
        panelSuperior.add(lblLogo);
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE PACIENTES");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 34f));
        lblTitulo.setBounds(130, 35, 800, 50);
        panelSuperior.add(lblTitulo);

        // PANEL CENTRAL
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setLayout(null);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.add(panelCentral, BorderLayout.CENTER);

        int margenX = 40;
        int margenY = 30;

        // IZQUIERDA
        JLabel lblBuscar = new JLabel("Buscar (Cédula o Nombre):");
        lblBuscar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 16f));
        lblBuscar.setForeground(new Color(55, 65, 81));
        lblBuscar.setBounds(margenX, margenY, 250, 20);
        panelCentral.add(lblBuscar);

        txtBuscador = new JTextField();
        txtBuscador.setBounds(margenX, margenY + 30, 450, 40);
        txtBuscador.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txtBuscador.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 16f));
        txtBuscador.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscar = true;
            }
        });
        panelCentral.add(txtBuscador);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(40, 120, 820, 402);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        panelCentral.add(scrollPane);

        String[] headers = { "ID", "Cédula", "Nombre", "Teléfono", "Estado" };
        model = new DefaultTableModel();
        model.setColumnIdentifiers(headers);
        table = new JTable();
        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(35);
        table.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
        
        table.getTableHeader().setBackground(new Color(4, 111, 67));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index != -1) {
                    // Usamos cédula como clave única, eliminando posibles caracteres de formato si es necesario
                    // Pero la tabla mostrará la cédula tal cual está guardada.
                    String cedula = table.getValueAt(index, 1).toString(); 
                    selectedPaciente = Clinica.getInstance().getPacientePorCedula(cedula);
                    cargarDatosPaciente();
                }
            }
        });
        scrollPane.setViewportView(table);

        // DERECHA (SIN FOTO)
        JPanel panelDetalle = new JPanel();
        panelDetalle.setBackground(new Color(245, 245, 245));
        panelDetalle.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        panelDetalle.setBounds(900, margenY, 400, 568);
        panelDetalle.setLayout(null);
        panelCentral.add(panelDetalle);

        JLabel lblDetalleTitulo = new JLabel("DETALLES DEL PACIENTE");
        lblDetalleTitulo.setHorizontalAlignment(JLabel.CENTER);
        lblDetalleTitulo.setForeground(new Color(4, 111, 67));
        lblDetalleTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f));
        lblDetalleTitulo.setBounds(20, 25, 360, 30);
        panelDetalle.add(lblDetalleTitulo);

        // Distribución
        int yStart = 100; 
        int gap = 80; 
        
        createLabelAndInput(panelDetalle, "NOMBRE:", yStart, txtNombre = new JTextField());
        
        // --- CÉDULA CON MÁSCARA ---
        JLabel lblCedula = new JLabel("CÉDULA:");
        lblCedula.setBounds(50, yStart + gap, 300, 20);
        lblCedula.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lblCedula.setForeground(new Color(100, 100, 100));
        panelDetalle.add(lblCedula);

        try {
            MaskFormatter cedulaMask = new MaskFormatter("###-#######-#");
            cedulaMask.setPlaceholderCharacter('_');
            txtCedula = new JFormattedTextField(cedulaMask);
            txtCedula.setBounds(50, yStart + gap + 20, 300, 35);
            txtCedula.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            txtCedula.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 15f));
            txtCedula.setEditable(false); // Cédula no editable (ID)
            txtCedula.setBackground(new Color(240, 240, 240));
            panelDetalle.add(txtCedula);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        // --- TELÉFONO CON MÁSCARA (SOLO NÚMEROS) ---
        JLabel lblTel = new JLabel("TELÉFONO:");
        lblTel.setBounds(50, yStart + gap * 2, 300, 20);
        lblTel.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lblTel.setForeground(new Color(100, 100, 100));
        panelDetalle.add(lblTel);

        try {
            MaskFormatter telMask = new MaskFormatter("(###) ###-####");
            telMask.setPlaceholderCharacter('_');
            txtTelefono = new JFormattedTextField(telMask);
            txtTelefono.setBounds(50, yStart + gap * 2 + 20, 300, 35);
            txtTelefono.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            txtTelefono.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 15f));
            panelDetalle.add(txtTelefono);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        createLabelAndInput(panelDetalle, "FECHA NAC. (AAAA-MM-DD):", yStart + gap * 3, txtFechaNac = new JTextField());
        
        // Botones
        int btnY = 480;
        int btnWidth = 145;

        btnModificar = new JButton("MODIFICAR");
        btnModificar.setBounds(50, btnY, btnWidth, 40);
        btnModificar.setBackground(new Color(22, 163, 74));
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnModificar.setFocusPainted(false);
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(e -> modificarPaciente());
        panelDetalle.add(btnModificar);

        btnEstado = new JButton("DESHABILITAR");
        btnEstado.setBounds(50 + btnWidth + 10, btnY, btnWidth, 40);
        btnEstado.setBackground(new Color(220, 38, 38));
        btnEstado.setForeground(Color.WHITE);
        btnEstado.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnEstado.setFocusPainted(false);
        btnEstado.setEnabled(false);
        btnEstado.addActionListener(e -> cambiarEstadoPaciente());
        panelDetalle.add(btnEstado);

        // --- BOTÓN VOLVER CIRCULAR ---
        ImageIcon iconFlecha = new ImageIcon(getClass().getResource("/Imagenes/FlechaAtras.png"));
        java.awt.Image imgFlechaScaled = iconFlecha.getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
        
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
        btnVolver.setBounds(40, 528, 70, 70); // Ubicado abajo a la izquierda
        btnVolver.setBackground(new Color(4, 111, 67));
        
        btnVolver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { dispose(); }
            @Override
            public void mouseEntered(MouseEvent e) {
                btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnVolver.setBackground(new Color(6, 140, 85));
                btnVolver.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnVolver.setBackground(new Color(4, 111, 67));
                btnVolver.repaint();
            }
        });
        
        JLabel lblFlecha = new JLabel(new ImageIcon(imgFlechaScaled));
        lblFlecha.setBounds(17, 17, 35, 35);
        btnVolver.add(lblFlecha);
        panelCentral.add(btnVolver); // Añadido al panel central

        loadPacientes("");
        limpiarFormulario();
        iniciarHiloBuscador();
    }

    private void createLabelAndInput(JPanel panel, String title, int y, JTextField field) {
        JLabel lbl = new JLabel(title);
        lbl.setBounds(50, y, 300, 20);
        lbl.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lbl.setForeground(new Color(100, 100, 100));
        panel.add(lbl);

        field.setBounds(50, y + 20, 300, 35);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        field.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 15f));
        panel.add(field);
    }

    private void iniciarHiloBuscador() {
        Thread hilo = new Thread(() -> {
            while (true) {
                if (buscar) {
                    try {
                        Thread.sleep(150);
                        String texto = txtBuscador.getText();
                        EventQueue.invokeLater(() -> loadPacientes(texto));
                        buscar = false;
                    } catch (InterruptedException e) { e.printStackTrace(); }
                }
                try { Thread.sleep(100); } catch (InterruptedException e) { break; }
            }
        });
        hilo.start();
    }

    private void loadPacientes(String filtro) {
        model.setRowCount(0);
        rows = new Object[model.getColumnCount()];
        ArrayList<Paciente> lista = Clinica.getInstance().getPacientes();

        for (Paciente pac : lista) {
            String nombre = pac.getNombre().toLowerCase();
            String cedula = pac.getCedula().toLowerCase();
            String filtroMin = filtro.toLowerCase();

            if (filtro.isEmpty() || nombre.contains(filtroMin) || cedula.contains(filtroMin)) {
                rows[0] = String.format("P-%03d", pac.getIdPaciente());
                rows[1] = pac.getCedula();
                rows[2] = pac.getNombre();
                rows[3] = pac.getTelefono();
                rows[4] = pac.isActivo() ? "Activo" : "Inactivo";
                model.addRow(rows);
            }
        }
    }

    private void cargarDatosPaciente() {
        if (selectedPaciente != null) {
            txtNombre.setText(selectedPaciente.getNombre());
            txtCedula.setText(selectedPaciente.getCedula());
            txtTelefono.setText(selectedPaciente.getTelefono());
            txtFechaNac.setText(selectedPaciente.getFechaNacimiento().toString());
            
            txtNombre.setEnabled(true);
            txtTelefono.setEnabled(true);
            txtFechaNac.setEnabled(true);
            // Cédula siempre deshabilitada porque es ID
            
            btnModificar.setEnabled(true);
            btnEstado.setEnabled(true);
            
            if (selectedPaciente.isActivo()) {
                btnEstado.setText("DESHABILITAR");
                btnEstado.setBackground(new Color(220, 38, 38));
            } else {
                btnEstado.setText("HABILITAR");
                btnEstado.setBackground(new Color(22, 163, 74));
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        txtFechaNac.setText("");
        
        txtNombre.setEnabled(false);
        txtCedula.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtFechaNac.setEnabled(false);
        
        btnModificar.setEnabled(false);
        btnEstado.setEnabled(false);
        btnEstado.setText("DESHABILITAR");
        btnEstado.setBackground(new Color(220, 38, 38));
        selectedPaciente = null;
    }

    private void modificarPaciente() {
        if (selectedPaciente == null) return;
        
        if(txtNombre.getText().isEmpty() || txtTelefono.getText().trim().length() < 10) { // Validación simple
            JOptionPane.showMessageDialog(this, "Nombre o Teléfono inválidos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            selectedPaciente.setNombre(txtNombre.getText());
            selectedPaciente.setTelefono(txtTelefono.getText());
            selectedPaciente.setFechaNacimiento(java.time.LocalDate.parse(txtFechaNac.getText()));
            
            Clinica.getInstance().guardarDatos();
            JOptionPane.showMessageDialog(this, "Paciente modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            loadPacientes(txtBuscador.getText());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en formato de fecha (AAAA-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoPaciente() {
        if (selectedPaciente == null) return;

        if (selectedPaciente.isActivo()) {
            String razon = JOptionPane.showInputDialog(this, "Razón de inhabilitación:", "Confirmar", JOptionPane.WARNING_MESSAGE);
            if (razon != null && !razon.trim().isEmpty()) {
                selectedPaciente.setActivo(false);
                selectedPaciente.setCausaDeshabilitacion(razon);
                Clinica.getInstance().guardarDatos();
                
                cargarDatosPaciente();
                loadPacientes(txtBuscador.getText());
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Razón anterior: " + selectedPaciente.getCausaDeshabilitacion() + "\n¿Habilitar?", 
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                selectedPaciente.setActivo(true);
                selectedPaciente.setCausaDeshabilitacion("");
                Clinica.getInstance().guardarDatos();
                
                cargarDatosPaciente();
                loadPacientes(txtBuscador.getText());
            }
        }
    }
}