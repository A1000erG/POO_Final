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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Utilidades.FuenteUtil;
import logico.Clinica;
import logico.Personal;
import logico.Vacuna;

public class ListarVacunas extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtBuscador;
    private JTable table;
    private DefaultTableModel model;
    private Object[] rows;
    
    private JTextField txtNombre;
    private JTextField txtStock;
    private JTextField txtFechaVenc;
    private JLabel lblFoto; 
    
    private JButton btnModificar;
    private JButton btnEstado; // Botón nuevo para deshabilitar vacunas
    
    private Vacuna selectedVacuna = null;
    private boolean buscar = false;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ListarVacunas dialog = new ListarVacunas(null);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    public ListarVacunas(Personal usuarioLogueado) {
        setTitle("Gestión de Vacunas");
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
        Image imgLogoEscalada = iconLogo.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imgLogoEscalada));
        lblLogo.setBounds(40, 25, 70, 70);
        panelSuperior.add(lblLogo);
        
        JLabel lblTitulo = new JLabel("INVENTARIO DE VACUNAS");
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
        JLabel lblBuscar = new JLabel("Buscar (Nombre):");
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

        String[] headers = { "ID", "Nombre", "Stock", "Vencimiento", "Estado" }; // Agregado Estado
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
                    selectedVacuna = buscarVacunaPorId(Integer.parseInt(id));
                    cargarDatosVacuna();
                }
            }
        });
        scrollPane.setViewportView(table);

        // DERECHA
        JPanel panelDetalle = new JPanel();
        panelDetalle.setBackground(new Color(245, 245, 245));
        panelDetalle.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        panelDetalle.setBounds(900, margenY, 400, 568);
        panelDetalle.setLayout(null);
        panelCentral.add(panelDetalle);

        JLabel lblDetalleTitulo = new JLabel("DETALLES DE LA VACUNA");
        lblDetalleTitulo.setHorizontalAlignment(JLabel.CENTER);
        lblDetalleTitulo.setForeground(new Color(4, 111, 67));
        lblDetalleTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f));
        lblDetalleTitulo.setBounds(20, 20, 360, 30);
        panelDetalle.add(lblDetalleTitulo);

        // Icono
        lblFoto = new JLabel("");
        lblFoto.setBounds(125, 60, 150, 150);
        lblFoto.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.WHITE);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/syringe.png"));
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
            lblFoto.setHorizontalAlignment(JLabel.CENTER);
        } catch(Exception e) {}
        panelDetalle.add(lblFoto);

        int yStart = 230;
        int gap = 60;
        
        createLabelAndInput(panelDetalle, "NOMBRE:", yStart, txtNombre = new JTextField());
        createLabelAndInput(panelDetalle, "STOCK DISPONIBLE:", yStart + gap, txtStock = new JTextField());
        createLabelAndInput(panelDetalle, "FECHA VENC. (AAAA-MM-DD):", yStart + gap * 2, txtFechaVenc = new JTextField());

        // Botones alineados
        int btnY = 510;
        int btnWidth = 145;

        btnModificar = new JButton("MODIFICAR");
        btnModificar.setBounds(50, btnY, btnWidth, 40);
        btnModificar.setBackground(new Color(22, 163, 74));
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnModificar.setFocusPainted(false);
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(e -> modificarVacuna());
        panelDetalle.add(btnModificar);

        btnEstado = new JButton("DESHABILITAR");
        btnEstado.setBounds(50 + btnWidth + 10, btnY, btnWidth, 40);
        btnEstado.setBackground(new Color(220, 38, 38));
        btnEstado.setForeground(Color.WHITE);
        btnEstado.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnEstado.setFocusPainted(false);
        btnEstado.setEnabled(false);
        btnEstado.addActionListener(e -> cambiarEstadoVacuna());
        panelDetalle.add(btnEstado);

        // Botón Volver
        ImageIcon iconFlecha = new ImageIcon(getClass().getResource("/Imagenes/FlechaAtras.png"));
        Image imgFlechaScaled = iconFlecha.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        
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
        btnVolver.setBounds(40, 528, 70, 70);
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
        panelCentral.add(btnVolver);

        loadVacunas("");
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
                        EventQueue.invokeLater(() -> loadVacunas(texto));
                        buscar = false;
                    } catch (InterruptedException e) { e.printStackTrace(); }
                }
                try { Thread.sleep(100); } catch (InterruptedException e) { break; }
            }
        });
        hilo.start();
    }

    private void loadVacunas(String filtro) {
        model.setRowCount(0);
        rows = new Object[model.getColumnCount()];
        ArrayList<Vacuna> lista = Clinica.getInstance().getVacunas();

        for (Vacuna v : lista) {
            String nombre = v.getNombre().toLowerCase();
            String filtroMin = filtro.toLowerCase();

            if (filtro.isEmpty() || nombre.contains(filtroMin)) {
                rows[0] = v.getId();
                rows[1] = v.getNombre();
                rows[2] = v.getCantidadDisponible();
                rows[3] = v.getFechaCaducidad();
                rows[4] = v.isActivo() ? "Activo" : "Descontinuada"; // Mostrar estado
                model.addRow(rows);
            }
        }
    }

    private void cargarDatosVacuna() {
        if (selectedVacuna != null) {
            txtNombre.setText(selectedVacuna.getNombre());
            txtStock.setText(String.valueOf(selectedVacuna.getCantidadDisponible()));
            txtFechaVenc.setText(selectedVacuna.getFechaCaducidad().toString());
            
            txtNombre.setEnabled(true);
            txtStock.setEnabled(true);
            txtFechaVenc.setEnabled(true);
            
            btnModificar.setEnabled(true);
            btnEstado.setEnabled(true);
            
            if (selectedVacuna.isActivo()) {
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
        txtStock.setText("");
        txtFechaVenc.setText("");
        
        txtNombre.setEnabled(false);
        txtStock.setEnabled(false);
        txtFechaVenc.setEnabled(false);
        
        btnModificar.setEnabled(false);
        btnEstado.setEnabled(false);
        btnEstado.setText("DESHABILITAR");
        btnEstado.setBackground(new Color(220, 38, 38));
        selectedVacuna = null;
    }

    private Vacuna buscarVacunaPorId(int id) {
        for (Vacuna v : Clinica.getInstance().getVacunas()) {
            if (v.getId() == id) return v;
        }
        return null;
    }

    private void modificarVacuna() {
        if (selectedVacuna == null) return;
        
        try {
            selectedVacuna.setNombre(txtNombre.getText());
            selectedVacuna.setCantidadDisponible(Integer.parseInt(txtStock.getText()));
            selectedVacuna.setFechaCaducidad(java.time.LocalDate.parse(txtFechaVenc.getText()));
            
            Clinica.getInstance().guardarDatos();
            JOptionPane.showMessageDialog(this, "Vacuna actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            loadVacunas(txtBuscador.getText());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en datos. Verifique Stock y Fecha.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoVacuna() {
        if (selectedVacuna == null) return;

        if (selectedVacuna.isActivo()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Seguro que desea descontinuar esta vacuna?", 
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                selectedVacuna.setActivo(false);
                Clinica.getInstance().guardarDatos();
                cargarDatosVacuna();
                loadVacunas(txtBuscador.getText());
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Reactivar el uso de esta vacuna?", 
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                selectedVacuna.setActivo(true);
                Clinica.getInstance().guardarDatos();
                cargarDatosVacuna();
                loadVacunas(txtBuscador.getText());
            }
        }
    }
}