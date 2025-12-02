package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Utilidades.FuenteUtil; 
import logico.Clinica;
import logico.Enfermedad;

public class ListarEnfermedades extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtBuscador;
    private JTable table;
    private DefaultTableModel model;
    private Object[] rows;
    
    // Campos de detalle
    private JTextField txtId;
    private JTextField txtNombre;
    
    private JButton btnModificar;
    private JButton btnEliminar;
    
    private Enfermedad selectedEnfermedad = null;
    private boolean buscar = false;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ListarEnfermedades dialog = new ListarEnfermedades();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    public ListarEnfermedades() {
        setTitle("Gestión de Enfermedades");
        setModal(true);
        setBounds(100, 100, 1366, 768);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // ------------------ PANEL SUPERIOR -------------------
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new java.awt.Color(4, 111, 67)); 
        panelSuperior.setLayout(null);
        panelSuperior.setPreferredSize(new java.awt.Dimension(0, 120)); 
        contentPane.add(panelSuperior, BorderLayout.NORTH);
        
        ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
        Image imgLogoEscalada = iconLogo.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imgLogoEscalada));
        lblLogo.setBounds(40, 25, 70, 70);
        panelSuperior.add(lblLogo);
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE ENFERMEDADES");
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
        int margenY = 30;

        // === SECCIÓN IZQUIERDA (LISTADO) ===
        
        JLabel lblBuscar = new JLabel("Buscar (ID o Nombre):");
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

        String[] headers = { "ID", "Nombre Enfermedad" };
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
                    String id = table.getValueAt(index, 0).toString();
                    String idNum = id.replace("E-", ""); 
                    try {
                        selectedEnfermedad = buscarEnfermedadPorId(Integer.parseInt(idNum));
                        cargarDatosEnfermedad();
                    } catch (NumberFormatException ex) { }
                }
            }
        });
        scrollPane.setViewportView(table);

        // === SECCIÓN DERECHA (DETALLES) ===
        
        JPanel panelDetalle = new JPanel(); 
        panelDetalle.setBackground(new Color(245, 245, 245));
        panelDetalle.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        
        // Ajustamos altura del panel de detalle ya que tiene menos campos
        panelDetalle.setBounds(900, margenY, 400, 400); 
        panelDetalle.setLayout(null);
        panelCentral.add(panelDetalle);

        JLabel lblDetalleTitulo = new JLabel("DETALLES ENFERMEDAD");
        lblDetalleTitulo.setHorizontalAlignment(JLabel.CENTER);
        lblDetalleTitulo.setForeground(new Color(4, 111, 67));
        lblDetalleTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f));
        lblDetalleTitulo.setBounds(20, 25, 360, 30);
        panelDetalle.add(lblDetalleTitulo);

        // Icono decorativo (Genérico)
        JLabel lblIcono = new JLabel("");
        lblIcono.setBounds(150, 70, 100, 100);
        // Intentamos cargar un icono genérico de virus/bacteria o similar si existe, si no uno por defecto
        ImageIcon iconGen = new ImageIcon(getClass().getResource("/Imagenes/thermometer.png")); // Reutilizamos icono existente
        if (iconGen.getImage() != null) {
             Image img = iconGen.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
             lblIcono.setIcon(new ImageIcon(img));
        }
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        panelDetalle.add(lblIcono);

        int yStart = 200;
        int gap = 60;
        
        createLabelAndInput(panelDetalle, "ID:", yStart, txtId = new JTextField());
        txtId.setEditable(false);
        txtId.setBackground(new Color(230,230,230));
        
        createLabelAndInput(panelDetalle, "NOMBRE:", yStart + gap, txtNombre = new JTextField());
        
        // BOTONES
        int btnY = 330;
        int btnWidth = 145;

        btnModificar = new JButton("MODIFICAR");
        btnModificar.setBounds(50, btnY, btnWidth, 40);
        btnModificar.setBackground(new Color(22, 163, 74)); 
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnModificar.setFocusPainted(false);
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(e -> modificarEnfermedad());
        panelDetalle.add(btnModificar);

        btnEliminar = new JButton("ELIMINAR");
        btnEliminar.setBounds(50 + btnWidth + 10, btnY, btnWidth, 40); 
        btnEliminar.setBackground(new Color(220, 38, 38)); 
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarEnfermedad());
        panelDetalle.add(btnEliminar);

        // BOTÓN VOLVER (Fuera del panel de detalle, abajo a la izquierda)
        ImageIcon iconFlecha = new ImageIcon(getClass().getResource("/Imagenes/FlechaAtras.png"));
        Image imgFlechaScaled = iconFlecha.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        ImageIcon iconFlechaFinal = new ImageIcon(imgFlechaScaled);

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
        btnVolver.setBounds(40, 550, 70, 70); 
        btnVolver.setBackground(new Color(4, 111, 67)); 

        btnVolver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
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

        JLabel lblFlecha = new JLabel(iconFlechaFinal);
        lblFlecha.setBounds(17, 17, 35, 35); 
        btnVolver.add(lblFlecha);

        panelCentral.add(btnVolver);

        loadEnfermedades("");
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
                        EventQueue.invokeLater(() -> loadEnfermedades(texto));
                        buscar = false;
                    } catch (InterruptedException e) { e.printStackTrace(); }
                }
                try { Thread.sleep(100); } catch (InterruptedException e) { break; }
            }
        });
        hilo.start();
    }

    private void loadEnfermedades(String filtro) {
        model.setRowCount(0);
        rows = new Object[model.getColumnCount()];
        ArrayList<Enfermedad> lista = Clinica.getInstance().getCatalogoEnfermedades();
        int coincidencias = 0;
        Enfermedad unicoEncontrado = null;

        for (Enfermedad enf : lista) {
            String nombre = enf.getNombre().toLowerCase();
            String filtroMin = filtro.toLowerCase();
            
            String idRaw = String.valueOf(enf.getIdEnfermedad());
            String idFormatted = String.format("%03d", enf.getIdEnfermedad());
            String idFull = String.format("E-%03d", enf.getIdEnfermedad()).toLowerCase();

            if (filtro.isEmpty() || 
                nombre.contains(filtroMin) || 
                idRaw.contains(filtroMin) ||
                idFormatted.contains(filtroMin) ||
                idFull.contains(filtroMin)) {

                rows[0] = String.format("E-%03d", enf.getIdEnfermedad());
                rows[1] = enf.getNombre();
                model.addRow(rows);
                coincidencias++;
                unicoEncontrado = enf;
            }
        }
        
        if (coincidencias == 1 && !filtro.isEmpty()) {
            table.setRowSelectionInterval(0, 0);
            selectedEnfermedad = unicoEncontrado;
            cargarDatosEnfermedad();
        } else if (coincidencias != 1) {
            limpiarFormulario();
        }
    }

    private void cargarDatosEnfermedad() {
        if (selectedEnfermedad != null) {
            txtId.setText(String.format("E-%03d", selectedEnfermedad.getIdEnfermedad()));
            txtNombre.setText(selectedEnfermedad.getNombre());
            
            txtNombre.setEnabled(true);
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        
        txtNombre.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        selectedEnfermedad = null;
    }

    private Enfermedad buscarEnfermedadPorId(int id) {
        for (Enfermedad e : Clinica.getInstance().getCatalogoEnfermedades()) {
            if (e.getIdEnfermedad() == id) return e;
        }
        return null;
    }

    private void modificarEnfermedad() {
        if (selectedEnfermedad == null) return;
        
        if(txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedEnfermedad.setNombre(txtNombre.getText());
        
        Clinica.getInstance().guardarDatos();
        JOptionPane.showMessageDialog(this, "Enfermedad modificada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        loadEnfermedades(txtBuscador.getText());
    }

    private void eliminarEnfermedad() {
        if (selectedEnfermedad == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que deseas eliminar esta enfermedad?\nEsta acción no se puede deshacer.", 
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Clinica.getInstance().getCatalogoEnfermedades().remove(selectedEnfermedad);
            Clinica.getInstance().guardarDatos();
            limpiarFormulario();
            loadEnfermedades(txtBuscador.getText());
            JOptionPane.showMessageDialog(this, "Enfermedad eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}