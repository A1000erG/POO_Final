package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Utilidades.FuenteUtil;
import logico.Cita;
import logico.Clinica;
import logico.Consulta;
import logico.Diagnostico;
import logico.Enfermedad;

public class RegConsulta extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Componentes UI
    private JTextArea txtSintomas;
    private JTable tablaDiagnosticos;
    private DefaultTableModel modeloDiagnostico;
    private JComboBox<String> cbEnfermedades;
    private JTextField txtTratamiento;
    private JButton btnAgregar;
    private JButton btnFinalizar;
    
    // Datos
    private Cita citaActual;
    private ArrayList<Diagnostico> listaDiagnosticosTemp;
    
    // Colores Estilo
    private Color colorHeader = new Color(4, 111, 67);
    private Color colorBorde = new Color(220, 220, 220);
    private Color colorFondoDetalle = new Color(245, 245, 245);
    private Color colorTexto = new Color(55, 65, 81);
    private Color colorAzul = new Color(21, 129, 191);

    public RegConsulta(Cita cita) {
        this.citaActual = cita;
        this.listaDiagnosticosTemp = new ArrayList<>();

        setTitle("Registro de Consulta Médica");
        setModal(true);
        setBounds(100, 100, 1200, 750);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // ------------------ HEADER -------------------
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(colorHeader);
        panelSuperior.setLayout(null);
        panelSuperior.setPreferredSize(new Dimension(0, 100));
        contentPane.add(panelSuperior, BorderLayout.NORTH);
        
        ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
        JLabel lblLogo = new JLabel(new ImageIcon(iconLogo.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        lblLogo.setBounds(30, 20, 60, 60);
        panelSuperior.add(lblLogo);
        
        JLabel lblTitulo = new JLabel("CONSULTA MÉDICA");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 30f));
        lblTitulo.setBounds(110, 25, 600, 50);
        panelSuperior.add(lblTitulo);

        // ------------------ CONTENIDO -------------------
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setLayout(null);
        contentPane.add(panelCentral, BorderLayout.CENTER);

        // === SECCIÓN IZQUIERDA: DATOS CITA Y SÍNTOMAS ===
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(null);
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setBorder(new LineBorder(colorBorde, 1, true));
        panelIzquierdo.setBounds(30, 30, 400, 550);
        panelCentral.add(panelIzquierdo);
        
        JLabel lblDatos = new JLabel("DATOS DEL PACIENTE");
        lblDatos.setForeground(colorHeader);
        lblDatos.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 16f));
        lblDatos.setBounds(20, 20, 300, 20);
        panelIzquierdo.add(lblDatos);
        
        crearLabelInfo(panelIzquierdo, "Paciente:", cita.getPaciente().getNombre(), 60);
        crearLabelInfo(panelIzquierdo, "Cédula:", cita.getPaciente().getCedula(), 100);
        crearLabelInfo(panelIzquierdo, "Edad:", obtenerEdad(cita.getPaciente().getFechaNacimiento()) + " Años", 140);
        crearLabelInfo(panelIzquierdo, "Fecha Cita:", cita.getFecha().toString(), 180);
        
        JLabel lblSintomas = new JLabel("SÍNTOMAS OBSERVADOS");
        lblSintomas.setForeground(colorHeader);
        lblSintomas.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 16f));
        lblSintomas.setBounds(20, 240, 300, 20);
        panelIzquierdo.add(lblSintomas);
        
        txtSintomas = new JTextArea();
        txtSintomas.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 14f));
        txtSintomas.setLineWrap(true);
        txtSintomas.setWrapStyleWord(true);
        
        JScrollPane scrollSintomas = new JScrollPane(txtSintomas);
        scrollSintomas.setBounds(20, 270, 360, 250);
        scrollSintomas.setBorder(BorderFactory.createLineBorder(colorBorde));
        panelIzquierdo.add(scrollSintomas);

        // === SECCIÓN DERECHA: DIAGNÓSTICO Y TRATAMIENTO ===
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBackground(colorFondoDetalle);
        panelDerecho.setBorder(new LineBorder(colorBorde, 1, true));
        panelDerecho.setBounds(460, 30, 700, 550);
        panelCentral.add(panelDerecho);
        
        JLabel lblDiagTitulo = new JLabel("DIAGNÓSTICO Y TRATAMIENTO");
        lblDiagTitulo.setForeground(colorHeader);
        lblDiagTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 16f));
        lblDiagTitulo.setBounds(20, 20, 400, 20);
        panelDerecho.add(lblDiagTitulo);
        
        // Formulario Diagnóstico
        JLabel lblEnf = new JLabel("Enfermedad Detectada:");
        lblEnf.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
        lblEnf.setBounds(20, 60, 200, 20);
        panelDerecho.add(lblEnf);
        
        cbEnfermedades = new JComboBox<>();
        cargarEnfermedadesCombo();
        cbEnfermedades.setBounds(20, 85, 300, 35);
        cbEnfermedades.setBackground(Color.WHITE);
        panelDerecho.add(cbEnfermedades);
        
        JLabel lblTrat = new JLabel("Tratamiento / Receta:");
        lblTrat.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
        lblTrat.setBounds(340, 60, 200, 20);
        panelDerecho.add(lblTrat);
        
        txtTratamiento = new JTextField();
        txtTratamiento.setBounds(340, 85, 220, 35);
        txtTratamiento.setBorder(BorderFactory.createLineBorder(colorBorde));
        panelDerecho.add(txtTratamiento);
        
        btnAgregar = new JButton("AGREGAR");
        btnAgregar.setBounds(580, 85, 100, 35);
        btnAgregar.setBackground(colorAzul);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 11f));
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(e -> agregarDiagnostico());
        panelDerecho.add(btnAgregar);
        
        // Tabla de Diagnósticos Agregados
        JScrollPane scrollTabla = new JScrollPane();
        scrollTabla.setBounds(20, 140, 660, 300);
        scrollTabla.setBorder(BorderFactory.createLineBorder(colorBorde));
        panelDerecho.add(scrollTabla);
        
        modeloDiagnostico = new DefaultTableModel();
        modeloDiagnostico.setColumnIdentifiers(new String[]{"Enfermedad", "Tratamiento", "Vigilancia"});
        
        tablaDiagnosticos = new JTable(modeloDiagnostico);
        tablaDiagnosticos.setRowHeight(30);
        tablaDiagnosticos.getTableHeader().setBackground(colorHeader);
        tablaDiagnosticos.getTableHeader().setForeground(Color.WHITE);
        tablaDiagnosticos.getTableHeader().setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        scrollTabla.setViewportView(tablaDiagnosticos);
        
        // Botón Finalizar Grande
        btnFinalizar = new JButton("FINALIZAR CONSULTA");
        btnFinalizar.setBounds(200, 470, 300, 50);
        btnFinalizar.setBackground(new Color(22, 163, 74));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 16f));
        btnFinalizar.setFocusPainted(false);
        btnFinalizar.addActionListener(e -> finalizarConsulta());
        panelDerecho.add(btnFinalizar);
        
        // Botón Volver (Circular)
        crearBotonVolver(panelSuperior);
    }
    
    // --- MÉTODOS VISUALES ---
    
    private void crearLabelInfo(JPanel panel, String titulo, String valor, int y) {
        JLabel lblT = new JLabel(titulo);
        lblT.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lblT.setForeground(Color.GRAY);
        lblT.setBounds(20, y, 100, 20);
        panel.add(lblT);
        
        JLabel lblV = new JLabel(valor);
        lblV.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 14f));
        lblV.setForeground(colorTexto);
        lblV.setBounds(120, y, 250, 20);
        panel.add(lblV);
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
            public void mouseClicked(MouseEvent e) { dispose(); }
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

        ImageIcon iconFlecha = new ImageIcon(getClass().getResource("/Imagenes/FlechaAtras.png"));
        if (iconFlecha.getImage() != null) {
            Image imgFlechaScaled = iconFlecha.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel lblFlecha = new JLabel(new ImageIcon(imgFlechaScaled));
            lblFlecha.setBounds(15, 15, 30, 30);
            btnVolver.add(lblFlecha);
        } else {
            JLabel lblTexto = new JLabel("<");
            lblTexto.setForeground(Color.WHITE);
            lblTexto.setFont(new Font("Arial", Font.BOLD, 24));
            lblTexto.setBounds(20, 15, 30, 30);
            btnVolver.add(lblTexto);
        }
        panel.add(btnVolver);
    }
    
    // --- LÓGICA DE NEGOCIO ---
    
    private void cargarEnfermedadesCombo() {
        cbEnfermedades.addItem("<Seleccionar Enfermedad>");
        for (Enfermedad enf : Clinica.getInstance().getCatalogoEnfermedades()) {
            cbEnfermedades.addItem(enf.getNombre());
        }
    }
    
    private void agregarDiagnostico() {
        String nombreEnf = (String) cbEnfermedades.getSelectedItem();
        String tratamiento = txtTratamiento.getText().trim();
        
        if (cbEnfermedades.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una enfermedad.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tratamiento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el tratamiento o receta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar duplicados en la lista temporal
        for (Diagnostico d : listaDiagnosticosTemp) {
            if (d.getNombre().equals(nombreEnf)) {
                JOptionPane.showMessageDialog(this, "Esta enfermedad ya fue agregada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        Enfermedad enfObj = Clinica.getInstance().buscarEnfermedadPorNombre(nombreEnf);
        // Crear Diagnóstico
        Diagnostico nuevoDiag = new Diagnostico();
        nuevoDiag.setNombre(nombreEnf);
        nuevoDiag.setEnfermedad(enfObj);
        nuevoDiag.setTratamiento(tratamiento);
        // Si hay lógica de severidad, se puede agregar un combo extra, por ahora default
        nuevoDiag.setSeveridad(Diagnostico.TipoSeveridad.LEVE); 
        
        listaDiagnosticosTemp.add(nuevoDiag);
        
        String vigilancia = (enfObj != null && enfObj.isEsBajoVigilancia()) ? "SÍ" : "NO";
        modeloDiagnostico.addRow(new Object[]{nombreEnf, tratamiento, vigilancia});
        
        // Limpiar campos parciales
        cbEnfermedades.setSelectedIndex(0);
        txtTratamiento.setText("");
    }
    
    private void finalizarConsulta() {
        String sintomas = txtSintomas.getText().trim();
        
        if (sintomas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe registrar los síntomas del paciente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Opcional: Obligar al menos un diagnóstico
        if (listaDiagnosticosTemp.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "No ha agregado ningún diagnóstico.\n¿Desea finalizar solo con los síntomas?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
        }
        
        // Crear y guardar Consulta
        Consulta consulta = new Consulta();
        consulta.setCodigo("CNS-" + (Clinica.getInstance().getConsultas().size() + 1));
        consulta.setCitaAsociada(citaActual);
        consulta.setSintoma(sintomas);
        consulta.setDiagnosticos(listaDiagnosticosTemp);
        consulta.setDoctor(citaActual.getDoctor()); // Asegurar referencia
        
        Clinica.getInstance().insertarConsulta(consulta);
        
        // Actualizar Cita
        citaActual.setEstado("Realizada");
        Clinica.getInstance().guardarDatos();
        
        JOptionPane.showMessageDialog(this, "Consulta finalizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    private int obtenerEdad(java.time.LocalDate fechaNac) {
        if (fechaNac == null) return 0;
        return java.time.Period.between(fechaNac, java.time.LocalDate.now()).getYears();
    }
}