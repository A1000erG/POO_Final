package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Utilidades.FuenteUtil;
import logico.Cita;
import logico.Clinica;
import logico.Consulta;
import logico.Diagnostico;
import logico.Doctor;
import logico.Enfermedad;
import logico.Vacuna;

public class RegConsulta extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel panelPrincipal;
    
    private JTextArea areaTextoSintomas;
    private JTable tablaDiagnosticos;
    private DefaultTableModel modeloTablaDiagnostico;
    private JComboBox<String> comboBoxEnfermedades;
    private JTextField campoTextoTratamiento;
    private JButton botonAgregarDiagnostico;
    private JButton botonFinalizarConsulta;
    private JButton botonNuevaEnfermedad;
    private JButton btnVerHistorial;
    
    private JComboBox<String> cbTipoSangre;
    private JTextField txtPeso;
    private JTextField txtEstatura;
    private JTextField txtIMC;
    
    private JComboBox<String> comboBoxVacunas;
    private JLabel etiquetaStockVacuna;
    private JButton botonAplicarVacuna;
    private JButton botonSolicitarVacuna;
    
    private Cita citaActual;
    private ArrayList<Diagnostico> listaDiagnosticosTemporal;
    private ArrayList<Vacuna> vacunasAplicadasEnSesion; 
    
    private Color colorVerdeHeader = new Color(4, 111, 67);
    private Color colorBordeGris = new Color(220, 220, 220);
    private Color colorFondoDetalle = new Color(245, 245, 245);
    private Color colorTextoOscuro = new Color(55, 65, 81);
    private Color colorAzulAccion = new Color(21, 129, 191);
    private Color colorRojoAlerta = new Color(220, 38, 38);

    public RegConsulta(Cita citaRecibida) {
        this.citaActual = citaRecibida;
        this.listaDiagnosticosTemporal = new ArrayList<>();
        this.vacunasAplicadasEnSesion = new ArrayList<>(); 

        setTitle("Registro de Consulta Médica");
        setModal(true);
        setBounds(100, 100, 1200, 800);
        setLocationRelativeTo(null);
        
        panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(new BorderLayout(0, 0));
        setContentPane(panelPrincipal);

        construirEncabezadoSuperior();
        construirPanelCentral();
    }

    private void construirEncabezadoSuperior() {
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(colorVerdeHeader);
        panelSuperior.setLayout(null);
        panelSuperior.setPreferredSize(new Dimension(0, 100));
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        ImageIcon iconoLogo = cargarIcono("/Imagenes/logoBlanco.png", 60, 60);
        JLabel etiquetaLogo = new JLabel(iconoLogo);
        etiquetaLogo.setBounds(30, 20, 60, 60);
        panelSuperior.add(etiquetaLogo);
        
        JLabel etiquetaTitulo = new JLabel("CONSULTA MÉDICA");
        etiquetaTitulo.setForeground(Color.WHITE);
        etiquetaTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 30f));
        etiquetaTitulo.setBounds(110, 25, 600, 50);
        panelSuperior.add(etiquetaTitulo);

        crearBotonVolverPersonalizado(panelSuperior);
    }

    private void construirPanelCentral() {
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setLayout(null);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        construirPanelIzquierdoDatos(panelCentral);
        construirPanelDerechoAcciones(panelCentral);
    }

    private void construirPanelIzquierdoDatos(JPanel panelPadre) {
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(null);
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setBorder(new LineBorder(colorBordeGris, 1, true));
        panelIzquierdo.setBounds(30, 30, 400, 600);
        panelPadre.add(panelIzquierdo);
        
        JLabel etiquetaDatosTitulo = new JLabel("DATOS DEL PACIENTE");
        etiquetaDatosTitulo.setForeground(colorVerdeHeader);
        etiquetaDatosTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 16f));
        etiquetaDatosTitulo.setBounds(20, 20, 300, 20);
        panelIzquierdo.add(etiquetaDatosTitulo);
        
        crearEtiquetaInformativa(panelIzquierdo, "Paciente:", citaActual.getPaciente().getNombre(), 60);
        crearEtiquetaInformativa(panelIzquierdo, "Cédula:", citaActual.getPaciente().getCedula(), 90);
        crearEtiquetaInformativa(panelIzquierdo, "Edad:", calcularEdadPaciente(citaActual.getPaciente().getFechaNacimiento()) + " Años", 120);
        crearEtiquetaInformativa(panelIzquierdo, "Fecha Cita:", citaActual.getFecha().toString(), 150);

        if (citaActual.getPaciente().getHistorialClinico() != null && !citaActual.getPaciente().getHistorialClinico().isEmpty()) {
            btnVerHistorial = new JButton("Ver Historial Anterior");
            btnVerHistorial.setBounds(20, 550, 360, 35);
            btnVerHistorial.setBackground(new Color(255, 140, 0));
            btnVerHistorial.setForeground(Color.WHITE);
            btnVerHistorial.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
            btnVerHistorial.setFocusPainted(false);
            btnVerHistorial.addActionListener(e -> {
                HistorialPaciente historial = new HistorialPaciente(citaActual.getPaciente());
                historial.setVisible(true);
            });
            panelIzquierdo.add(btnVerHistorial);
        }
        
        JLabel lblVitales = new JLabel("SIGNOS VITALES / DATOS FÍSICOS");
        lblVitales.setForeground(colorVerdeHeader);
        lblVitales.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
        lblVitales.setBounds(20, 190, 300, 20);
        panelIzquierdo.add(lblVitales);
        
        JLabel lblSangre = new JLabel("Tipo Sangre:");
        lblSangre.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lblSangre.setBounds(20, 220, 100, 20);
        panelIzquierdo.add(lblSangre);
        
        cbTipoSangre = new JComboBox<>(new String[]{"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"});
        String sangreActual = citaActual.getPaciente().getTipoSangre();
        if(sangreActual != null) cbTipoSangre.setSelectedItem(sangreActual);
        cbTipoSangre.setBounds(110, 220, 80, 25);
        cbTipoSangre.setBackground(Color.WHITE);
        panelIzquierdo.add(cbTipoSangre);
        
        JLabel lblPeso = new JLabel("Peso (Kg):");
        lblPeso.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lblPeso.setBounds(20, 260, 100, 20);
        panelIzquierdo.add(lblPeso);
        
        txtPeso = new JTextField(String.valueOf(citaActual.getPaciente().getPeso() != null ? citaActual.getPaciente().getPeso() : 0.0f));
        txtPeso.setBounds(110, 260, 80, 25);
        panelIzquierdo.add(txtPeso);
        
        JLabel lblEstatura = new JLabel("Estatura (m):");
        lblEstatura.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lblEstatura.setBounds(210, 260, 100, 20);
        panelIzquierdo.add(lblEstatura);
        
        txtEstatura = new JTextField(String.valueOf(citaActual.getPaciente().getEstatura() != null ? citaActual.getPaciente().getEstatura() : 0.0f));
        txtEstatura.setBounds(300, 260, 80, 25);
        panelIzquierdo.add(txtEstatura);
        
        JLabel lblIMC = new JLabel("IMC:");
        lblIMC.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lblIMC.setBounds(210, 220, 50, 20);
        panelIzquierdo.add(lblIMC);
        
        txtIMC = new JTextField();
        txtIMC.setEditable(false);
        txtIMC.setBackground(colorFondoDetalle);
        txtIMC.setBounds(300, 220, 80, 25);
        panelIzquierdo.add(txtIMC);
        
        calcularIMC();
        
        KeyAdapter validadorInput = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == '-') e.consume();
            }
            @Override
            public void keyReleased(KeyEvent e) {
                calcularIMC();
            }
        };
        
        txtPeso.addKeyListener(validadorInput);
        txtEstatura.addKeyListener(validadorInput);

        JLabel etiquetaSintomasTitulo = new JLabel("SÍNTOMAS OBSERVADOS");
        etiquetaSintomasTitulo.setForeground(colorVerdeHeader);
        etiquetaSintomasTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 16f));
        etiquetaSintomasTitulo.setBounds(20, 310, 300, 20);
        panelIzquierdo.add(etiquetaSintomasTitulo);
        
        areaTextoSintomas = new JTextArea();
        areaTextoSintomas.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 14f));
        areaTextoSintomas.setLineWrap(true);
        areaTextoSintomas.setWrapStyleWord(true);
        
        JScrollPane panelDesplazamientoSintomas = new JScrollPane(areaTextoSintomas);
        int alturaScroll = (btnVerHistorial != null) ? 190 : 230;
        panelDesplazamientoSintomas.setBounds(20, 340, 360, alturaScroll);
        panelDesplazamientoSintomas.setBorder(BorderFactory.createLineBorder(colorBordeGris));
        panelIzquierdo.add(panelDesplazamientoSintomas);
    }
    
    private void calcularIMC() {
        try {
            float peso = Float.parseFloat(txtPeso.getText());
            float estatura = Float.parseFloat(txtEstatura.getText());
            
            if (estatura > 0 && peso > 0) {
                float imc = peso / (estatura * estatura);
                txtIMC.setText(String.format("%.2f", imc).replace(',', '.'));
            } else {
                txtIMC.setText("0.00");
            }
        } catch (NumberFormatException ex) {
            txtIMC.setText("0.00");
        }
    }

    private void construirPanelDerechoAcciones(JPanel panelPadre) {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBackground(colorFondoDetalle);
        panelDerecho.setBorder(new LineBorder(colorBordeGris, 1, true));
        panelDerecho.setBounds(460, 30, 700, 600);
        panelPadre.add(panelDerecho);
        
        JLabel etiquetaTituloDiagnostico = new JLabel("DIAGNÓSTICO Y TRATAMIENTO");
        etiquetaTituloDiagnostico.setForeground(colorVerdeHeader);
        etiquetaTituloDiagnostico.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 16f));
        etiquetaTituloDiagnostico.setBounds(20, 20, 400, 20);
        panelDerecho.add(etiquetaTituloDiagnostico);
        
        JLabel etiquetaEnfermedad = new JLabel("Enfermedad Detectada:");
        etiquetaEnfermedad.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
        etiquetaEnfermedad.setBounds(20, 60, 200, 20);
        panelDerecho.add(etiquetaEnfermedad);
        
        comboBoxEnfermedades = new JComboBox<>();
        cargarEnfermedadesEnCombo();
        comboBoxEnfermedades.setBounds(20, 85, 260, 35);
        comboBoxEnfermedades.setBackground(Color.WHITE);
        panelDerecho.add(comboBoxEnfermedades);
        
        botonNuevaEnfermedad = new JButton("+");
        botonNuevaEnfermedad.setBounds(290, 85, 45, 35);
        botonNuevaEnfermedad.setBackground(colorAzulAccion);
        botonNuevaEnfermedad.setForeground(Color.WHITE);
        botonNuevaEnfermedad.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
        botonNuevaEnfermedad.setToolTipText("Registrar nueva enfermedad");
        botonNuevaEnfermedad.addActionListener(e -> registrarNuevaEnfermedad());
        panelDerecho.add(botonNuevaEnfermedad);
        
        JLabel etiquetaTratamiento = new JLabel("Tratamiento / Receta:");
        etiquetaTratamiento.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
        etiquetaTratamiento.setBounds(350, 60, 200, 20);
        panelDerecho.add(etiquetaTratamiento);
        
        campoTextoTratamiento = new JTextField();
        campoTextoTratamiento.setBounds(350, 85, 210, 35);
        campoTextoTratamiento.setBorder(BorderFactory.createLineBorder(colorBordeGris));
        panelDerecho.add(campoTextoTratamiento);
        
        botonAgregarDiagnostico = new JButton("AGREGAR");
        botonAgregarDiagnostico.setBounds(570, 85, 110, 35);
        botonAgregarDiagnostico.setBackground(colorAzulAccion);
        botonAgregarDiagnostico.setForeground(Color.WHITE);
        botonAgregarDiagnostico.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 11f));
        botonAgregarDiagnostico.setFocusPainted(false);
        botonAgregarDiagnostico.addActionListener(e -> agregarDiagnosticoALista());
        panelDerecho.add(botonAgregarDiagnostico);
        
        JScrollPane panelDesplazamientoTabla = new JScrollPane();
        panelDesplazamientoTabla.setBounds(20, 140, 660, 200);
        panelDesplazamientoTabla.setBorder(BorderFactory.createLineBorder(colorBordeGris));
        panelDerecho.add(panelDesplazamientoTabla);
        
        modeloTablaDiagnostico = new DefaultTableModel();
        modeloTablaDiagnostico.setColumnIdentifiers(new String[]{"Enfermedad", "Tratamiento", "Vigilancia"});
        
        tablaDiagnosticos = new JTable(modeloTablaDiagnostico);
        tablaDiagnosticos.setRowHeight(30);
        tablaDiagnosticos.getTableHeader().setBackground(colorVerdeHeader);
        tablaDiagnosticos.getTableHeader().setForeground(Color.WHITE);
        tablaDiagnosticos.getTableHeader().setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        panelDesplazamientoTabla.setViewportView(tablaDiagnosticos);
        
        JPanel panelVacunacion = new JPanel();
        panelVacunacion.setLayout(null);
        panelVacunacion.setBackground(Color.WHITE);
        TitledBorder bordeTitulo = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(colorAzulAccion), "Gestión de Inmunización");
        bordeTitulo.setTitleFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
        bordeTitulo.setTitleColor(colorAzulAccion);
        panelVacunacion.setBorder(bordeTitulo);
        panelVacunacion.setBounds(20, 360, 660, 150);
        panelDerecho.add(panelVacunacion);
        
        JLabel lblSelVacuna = new JLabel("Seleccionar Vacuna:");
        lblSelVacuna.setBounds(20, 30, 150, 20);
        panelVacunacion.add(lblSelVacuna);
        
        comboBoxVacunas = new JComboBox<>();
        comboBoxVacunas.setBounds(20, 55, 250, 30);
        cargarVacunasEnCombo();
        comboBoxVacunas.addActionListener(e -> actualizarStockVacuna());
        panelVacunacion.add(comboBoxVacunas);
        
        etiquetaStockVacuna = new JLabel("Stock: --");
        etiquetaStockVacuna.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
        etiquetaStockVacuna.setForeground(Color.GRAY);
        etiquetaStockVacuna.setBounds(290, 55, 150, 30);
        panelVacunacion.add(etiquetaStockVacuna);
        
        botonAplicarVacuna = new JButton("Aplicar Vacuna");
        botonAplicarVacuna.setBounds(20, 100, 150, 30);
        botonAplicarVacuna.setBackground(colorVerdeHeader);
        botonAplicarVacuna.setForeground(Color.WHITE);
        botonAplicarVacuna.addActionListener(e -> aplicarVacunaPaciente());
        panelVacunacion.add(botonAplicarVacuna);
        
        botonSolicitarVacuna = new JButton("Solicitar Stock");
        botonSolicitarVacuna.setBounds(190, 100, 150, 30);
        botonSolicitarVacuna.setBackground(colorRojoAlerta);
        botonSolicitarVacuna.setForeground(Color.WHITE);
        botonSolicitarVacuna.setEnabled(false);
        botonSolicitarVacuna.addActionListener(e -> solicitarStockVacuna());
        panelVacunacion.add(botonSolicitarVacuna);

        botonFinalizarConsulta = new JButton("FINALIZAR CONSULTA");
        botonFinalizarConsulta.setBounds(200, 530, 300, 50);
        botonFinalizarConsulta.setBackground(new Color(22, 163, 74));
        botonFinalizarConsulta.setForeground(Color.WHITE);
        botonFinalizarConsulta.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 16f));
        botonFinalizarConsulta.setFocusPainted(false);
        botonFinalizarConsulta.addActionListener(e -> finalizarConsultaMedica());
        panelDerecho.add(botonFinalizarConsulta);
    }

    private void crearEtiquetaInformativa(JPanel panel, String titulo, String valor, int posicionY) {
        JLabel etiquetaTitulo = new JLabel(titulo);
        etiquetaTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        etiquetaTitulo.setForeground(Color.GRAY);
        etiquetaTitulo.setBounds(20, posicionY, 100, 20);
        panel.add(etiquetaTitulo);
        
        JLabel etiquetaValor = new JLabel(valor);
        etiquetaValor.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 14f));
        etiquetaValor.setForeground(colorTextoOscuro);
        etiquetaValor.setBounds(120, posicionY, 250, 20);
        panel.add(etiquetaValor);
    }

    private void crearBotonVolverPersonalizado(JPanel panel) {
        JPanel botonVolver = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics grafico) {
                Graphics2D graficos2D = (Graphics2D) grafico;
                graficos2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graficos2D.setColor(getBackground());
                graficos2D.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(grafico);
            }
        };
        botonVolver.setOpaque(false);
        botonVolver.setLayout(null);
        botonVolver.setBounds(1100, 20, 60, 60); 
        botonVolver.setBackground(new Color(6, 140, 85)); 

        botonVolver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { dispose(); }
            @Override
            public void mouseEntered(MouseEvent e) {
                botonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
                botonVolver.setBackground(new Color(34, 197, 94));
                botonVolver.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                botonVolver.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                botonVolver.setBackground(new Color(6, 140, 85));
                botonVolver.repaint();
            }
        });

        ImageIcon iconoFlecha = cargarIcono("/Imagenes/FlechaAtras.png", 30, 30);
        JLabel etiquetaFlecha = new JLabel(iconoFlecha);
        if (iconoFlecha.getImage() == null) etiquetaFlecha.setText("<");
        etiquetaFlecha.setForeground(Color.WHITE);
        etiquetaFlecha.setBounds(15, 15, 30, 30);
        botonVolver.add(etiquetaFlecha);
        
        panel.add(botonVolver);
    }
    
    private ImageIcon cargarIcono(String ruta, int w, int h) {
        java.net.URL url = getClass().getResource(ruta);
        if (url != null) {
            return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        }
        return new ImageIcon();
    }

    private void cargarEnfermedadesEnCombo() {
        comboBoxEnfermedades.removeAllItems();
        comboBoxEnfermedades.addItem("<Seleccionar Enfermedad>");
        for (Enfermedad enfermedad : Clinica.getInstance().getCatalogoEnfermedades()) {
            comboBoxEnfermedades.addItem(enfermedad.getNombre());
        }
    }
    
    private void cargarVacunasEnCombo() {
        comboBoxVacunas.removeAllItems();
        comboBoxVacunas.addItem("<Seleccionar Vacuna>");
        for (Vacuna vacuna : Clinica.getInstance().getVacunas()) {
            comboBoxVacunas.addItem(vacuna.getNombre());
        }
    }
    
    private void registrarNuevaEnfermedad() {
        JTextField textoNombre = new JTextField();
        JCheckBox checkVigilancia = new JCheckBox("¿Está bajo vigilancia epidemiológica?");
        
        Object[] mensaje = {
            "Nombre de la Enfermedad:", textoNombre,
            "Vigilancia:", checkVigilancia
        };
        
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Registrar Nueva Enfermedad", JOptionPane.OK_CANCEL_OPTION);
        
        if (opcion == JOptionPane.OK_OPTION) {
            String nombre = textoNombre.getText().trim();
            if (!nombre.isEmpty()) {
                if (Clinica.getInstance().buscarEnfermedadPorNombre(nombre) == null) {
                    Enfermedad nuevaEnfermedad = new Enfermedad(Clinica.getInstance().getCatalogoEnfermedades().size() + 1, nombre, checkVigilancia.isSelected());
                    Clinica.getInstance().insertarEnfermedad(nuevaEnfermedad);
                    cargarEnfermedadesEnCombo();
                    comboBoxEnfermedades.setSelectedItem(nombre);
                    JOptionPane.showMessageDialog(this, "Enfermedad registrada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "Esta enfermedad ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void actualizarStockVacuna() {
        if (comboBoxVacunas.getSelectedIndex() <= 0) {
            etiquetaStockVacuna.setText("Stock: --");
            botonAplicarVacuna.setEnabled(false);
            botonSolicitarVacuna.setEnabled(false);
            return;
        }
        
        String nombreVacuna = (String) comboBoxVacunas.getSelectedItem();
        Vacuna vacuna = buscarVacunaPorNombre(nombreVacuna);
        
        if (vacuna != null) {
            etiquetaStockVacuna.setText("Stock: " + vacuna.getCantidadDisponible());
            if (vacuna.getCantidadDisponible() > 0) {
                etiquetaStockVacuna.setForeground(colorVerdeHeader);
                botonAplicarVacuna.setEnabled(true);
                botonSolicitarVacuna.setEnabled(false);
            } else {
                etiquetaStockVacuna.setForeground(colorRojoAlerta);
                botonAplicarVacuna.setEnabled(false);
                botonSolicitarVacuna.setEnabled(true);
            }
        }
    }
    
    private void aplicarVacunaPaciente() {
        String nombreVacuna = (String) comboBoxVacunas.getSelectedItem();
        Vacuna vacuna = buscarVacunaPorNombre(nombreVacuna);
        
        if (vacuna != null) {
            for(Vacuna v : vacunasAplicadasEnSesion) {
                if(v.getNombre().equalsIgnoreCase(vacuna.getNombre())) {
                    JOptionPane.showMessageDialog(this, "No se puede aplicar la misma vacuna dos veces en la misma consulta.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if(vacuna.getCantidadDisponible() > 0) {
                vacuna.setCantidadDisponible(vacuna.getCantidadDisponible() - 1);
                vacunasAplicadasEnSesion.add(vacuna);
                
                actualizarStockVacuna();
                
                String tratamientoActual = campoTextoTratamiento.getText();
                String registroVacuna = "[Vacuna Aplicada: " + nombreVacuna + "]";
                if (tratamientoActual.isEmpty()) {
                    campoTextoTratamiento.setText(registroVacuna);
                } else {
                    campoTextoTratamiento.setText(tratamientoActual + ", " + registroVacuna);
                }
                
                JOptionPane.showMessageDialog(this, "Vacuna aplicada y agregada al registro.");
            }
        }
    }
    
    private void solicitarStockVacuna() {
        String nombreVacuna = (String) comboBoxVacunas.getSelectedItem();
        JOptionPane.showMessageDialog(this, "Solicitud de pedido enviada a Administración para: " + nombreVacuna, "Solicitud Enviada", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Vacuna buscarVacunaPorNombre(String nombre) {
        for (Vacuna v : Clinica.getInstance().getVacunas()) {
            if (v.getNombre().equalsIgnoreCase(nombre)) return v;
        }
        return null;
    }

    private void agregarDiagnosticoALista() {
        String nombreEnfermedad = (String) comboBoxEnfermedades.getSelectedItem();
        String tratamiento = campoTextoTratamiento.getText().trim();
        
        if (comboBoxEnfermedades.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una enfermedad.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tratamiento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el tratamiento o receta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (Diagnostico diag : listaDiagnosticosTemporal) {
            if (diag.getNombre().equals(nombreEnfermedad)) {
                JOptionPane.showMessageDialog(this, "Esta enfermedad ya fue agregada.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        Enfermedad enfermedadObjeto = Clinica.getInstance().buscarEnfermedadPorNombre(nombreEnfermedad);
        Diagnostico nuevoDiagnostico = new Diagnostico();
        nuevoDiagnostico.setNombre(nombreEnfermedad);
        nuevoDiagnostico.setEnfermedad(enfermedadObjeto);
        nuevoDiagnostico.setTratamiento(tratamiento);
        nuevoDiagnostico.setSeveridad(Diagnostico.TipoSeveridad.LEVE); 
        
        listaDiagnosticosTemporal.add(nuevoDiagnostico);
        
        String vigilancia = (enfermedadObjeto != null && enfermedadObjeto.isEsBajoVigilancia()) ? "SÍ" : "NO";
        modeloTablaDiagnostico.addRow(new Object[]{nombreEnfermedad, tratamiento, vigilancia});
        
        comboBoxEnfermedades.setSelectedIndex(0);
        campoTextoTratamiento.setText("");
    }
    
    private void finalizarConsultaMedica() {
        String sintomasPaciente = areaTextoSintomas.getText().trim();
        
        if (sintomasPaciente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe registrar los síntomas del paciente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (listaDiagnosticosTemporal.isEmpty() && vacunasAplicadasEnSesion.isEmpty()) {
            int confirmacion = JOptionPane.showConfirmDialog(this, "No ha agregado diagnósticos ni vacunas.\n¿Desea finalizar solo con los síntomas?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion != JOptionPane.YES_OPTION) return;
        }
        
        try {
            float peso = Float.parseFloat(txtPeso.getText());
            float estatura = Float.parseFloat(txtEstatura.getText());

            if (peso < 0 || estatura < 0) {
                JOptionPane.showMessageDialog(this, "El Peso y la Estatura no pueden ser valores negativos.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            float imc = 0;
            try { imc = Float.parseFloat(txtIMC.getText().replace(',', '.')); } catch(Exception e) { imc = 0; }
            
            citaActual.getPaciente().setPeso(peso);
            citaActual.getPaciente().setEstatura(estatura);
            citaActual.getPaciente().setIMC(imc);
            citaActual.getPaciente().setTipoSangre((String) cbTipoSangre.getSelectedItem());
            
            // GUARDAR VACUNAS APLICADAS EN EL HISTORIAL DEL PACIENTE
            if(!vacunasAplicadasEnSesion.isEmpty()) {
                if(citaActual.getPaciente().getVacunasAplicadas() == null) {
                    citaActual.getPaciente().setVacunasAplicadas(new ArrayList<>());
                }
                citaActual.getPaciente().getVacunasAplicadas().addAll(vacunasAplicadasEnSesion);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifique que el Peso y Estatura sean numéricos.", "Error en Datos Físicos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Consulta nuevaConsulta = new Consulta();
        nuevaConsulta.setCodigo("CNS-" + (Clinica.getInstance().getConsultas().size() + 1));
        nuevaConsulta.setCitaAsociada(citaActual);
        nuevaConsulta.setSintoma(sintomasPaciente);
        nuevaConsulta.setDiagnosticos(listaDiagnosticosTemporal);
        nuevaConsulta.setDoctor(citaActual.getDoctor());
        
        Clinica.getInstance().insertarConsulta(nuevaConsulta);
        
        Doctor doc = citaActual.getDoctor();
        if (doc != null) {
            doc.setCupoDia(doc.getCupoDia() - 1);
        }
        
        citaActual.setEstado("Realizada");
        Clinica.getInstance().guardarDatos();
        
        JOptionPane.showMessageDialog(this, "Consulta finalizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    private int calcularEdadPaciente(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return 0;
        return java.time.Period.between(fechaNacimiento, java.time.LocalDate.now()).getYears();
    }
}