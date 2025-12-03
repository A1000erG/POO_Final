package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
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

import Utilidades.FuenteUtil;
import logico.Clinica;
import logico.Enfermedad;
import logico.Vacuna;

public class GestionRecursos extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    
    // Componentes Vacunas
    private JTable tableVacunas;
    private DefaultTableModel modelVacunas;
    private JButton btnAgregarStock;
    private JButton btnNuevaVacuna;
    private JButton btnVerSolicitudes;
    
    // Componentes Enfermedades
    private JTable tableEnfermedades;
    private DefaultTableModel modelEnfermedades;
    private JButton btnNuevaEnfermedad;
    private JButton btnCambiarVigilancia;
    
    // Estilos
    private Color colorPrimario = new Color(21, 129, 191);
    private Color colorVerde = new Color(22, 163, 74);
    private Color colorHeader = new Color(4, 111, 67);
    private Color colorRojo = new Color(220, 38, 38);
    private Color colorBlanco = Color.WHITE;
    private Color colorFondoGris = new Color(245, 245, 245);
    private Font fuenteHeader = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f);
    private Font fuenteNormal = FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 13f);
    private Font fuenteTitulo = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 24f);

    public GestionRecursos() {
        setTitle("Gestión de Recursos Clínicos");
        setBounds(100, 100, 1000, 700);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        
        getContentPane().setLayout(new BorderLayout());
        
        // --- HEADER ---
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(colorHeader);
        panelHeader.setPreferredSize(new Dimension(0, 80));
        panelHeader.setLayout(null);
        getContentPane().add(panelHeader, BorderLayout.NORTH);
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE RECURSOS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(fuenteTitulo);
        lblTitulo.setBounds(30, 20, 400, 40);
        panelHeader.add(lblTitulo);
        
        // --- CONTENIDO ---
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setBackground(colorFondoGris);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(fuenteHeader);
        tabbedPane.setBackground(colorBlanco);
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Pestaña 1: Vacunas
        JPanel panelVacunas = crearPanelVacunas();
        tabbedPane.addTab("  Inventario de Vacunas  ", null, panelVacunas, null);
        
        // Pestaña 2: Enfermedades
        JPanel panelEnfermedades = crearPanelEnfermedades();
        tabbedPane.addTab("  Vigilancia Epidemiológica  ", null, panelEnfermedades, null);
        
        // --- FOOTER ---
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(colorBlanco);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(240, 240, 240));
        btnCerrar.setFont(fuenteNormal);
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);
        
        cargarVacunas();
        cargarEnfermedades();
    }

    /*
       Función: crearPanelVacunas
       Objetivo: Crear panel con tabla y botones para gestión de vacunas.
    */
    private JPanel crearPanelVacunas() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(colorBlanco);
        panel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        // Panel Izquierdo (Tabla)
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 30, 650, 480);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane);
        
        String[] headers = {"ID", "Nombre de Vacuna", "Stock Disponible"};
        // Modelo no editable
        modelVacunas = new DefaultTableModel(null, headers) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableVacunas = new JTable(modelVacunas);
        configurarTabla(tableVacunas);
        scrollPane.setViewportView(tableVacunas);
        
        // Panel Derecho (Botones)
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

    /*
       Función: crearPanelEnfermedades
       Objetivo: Crear panel para gestión de enfermedades.
    */
    private JPanel crearPanelEnfermedades() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(colorBlanco);
        panel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 30, 650, 480);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane);
        
        String[] headers = {"ID", "Nombre Enfermedad", "Estado de Vigilancia"};
        modelEnfermedades = new DefaultTableModel(null, headers) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) { return false; }
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
    
    // --- LÓGICA DE NEGOCIO ---

    /*
       Función: cargarVacunas
       Objetivo: Refrescar la tabla de vacunas desde la lógica.
    */
    private void cargarVacunas() {
        modelVacunas.setRowCount(0);
        // Validar lista nula
        if (Clinica.getInstance().getVacunas() != null) {
            for (Vacuna v : Clinica.getInstance().getVacunas()) {
                modelVacunas.addRow(new Object[]{v.getId(), v.getNombre(), v.getCantidadDisponible()});
            }
        }
        tableVacunas.revalidate();
        tableVacunas.repaint();
    }
    
    /*
       Función: registrarNuevaVacuna
       Objetivo: Crear una nueva vacuna con ID autoincremental seguro y validación.
    */
    private void registrarNuevaVacuna() {
        JTextField txtNombre = new JTextField();
        JTextField txtStock = new JTextField();
        
        Object[] message = {
            "Nombre de la Vacuna:", txtNombre,
            "Stock Inicial:", txtStock
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nueva Vacuna", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String stockStr = txtStock.getText().trim();
            
            if (nombre.isEmpty() || stockStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Validación de stock
                int stock = Integer.parseInt(stockStr);
                if (stock < 0) {
                    JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validación de Duplicados y cálculo de ID seguro
                int maxId = 0;
                ArrayList<Vacuna> lista = Clinica.getInstance().getVacunas();
                if (lista != null) {
                    for (Vacuna v : lista) {
                        if (v.getNombre().equalsIgnoreCase(nombre)) {
                            JOptionPane.showMessageDialog(this, "Ya existe una vacuna con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (v.getId() > maxId) {
                            maxId = v.getId();
                        }
                    }
                }
                int nuevoId = maxId + 1;
                
                // Creación de objeto (Try-Catch por si falla el constructor vacío)
                try {
                    Vacuna nueva = new Vacuna(); 
                    nueva.setId(nuevoId);
                    nueva.setNombre(nombre);
                    nueva.setCantidadDisponible(stock);
                    
                    // Registro
                    // CAMBIO: Usamos getVacunas().add() directamente para evitar conflicto de métodos
                    Clinica.getInstance().getVacunas().add(nueva);
                    Clinica.getInstance().guardarDatos(); // Asegurar persistencia
                    
                    // Refresco visual inmediato
                    cargarVacunas();
                    JOptionPane.showMessageDialog(this, "Vacuna registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al crear objeto Vacuna: " + ex.getMessage(), "Error Interno", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El stock debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void agregarStock() {
        int selectedRow = tableVacunas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una vacuna de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Manejo seguro de la conversión de ID
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
                String input = JOptionPane.showInputDialog(this, "Ingrese cantidad a agregar para: " + vacunaSel.getNombre(), "Reponer Stock", JOptionPane.QUESTION_MESSAGE);
                if (input != null && !input.isEmpty()) {
                    try {
                        int cant = Integer.parseInt(input);
                        if (cant > 0) {
                            vacunaSel.setCantidadDisponible(vacunaSel.getCantidadDisponible() + cant);
                            // Guardado y refresco
                            Clinica.getInstance().guardarDatos(); 
                            cargarVacunas();
                            JOptionPane.showMessageDialog(this, "Stock actualizado correctamente.");
                        } else {
                            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "No hay solicitudes pendientes.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
            
            int opt = JOptionPane.showConfirmDialog(this, scrollMsg, "Gestión de Solicitudes", JOptionPane.YES_NO_OPTION);
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
                modelEnfermedades.addRow(new Object[]{e.getIdEnfermedad(), e.getNombre(), e.isEsBajoVigilancia() ? "EN VIGILANCIA" : "Normal"});
            }
        }
        tableEnfermedades.revalidate();
        tableEnfermedades.repaint();
    }
    
    private void registrarNuevaEnfermedad() {
        JTextField txtNombre = new JTextField();
        JCheckBox chkVigilancia = new JCheckBox("¿Bajo Vigilancia Epidemiológica?");
        
        Object[] message = {
            "Nombre de la Enfermedad:", txtNombre,
            "Estado:", chkVigilancia
        };

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
            
            // Generación de ID Enfermedad (Max + 1)
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

    // --- AUXILIARES UI ---
    
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
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }
}