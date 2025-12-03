package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JDateChooser;

import Utilidades.FuenteUtil;
import logico.Cita;
import logico.Clinica;
import logico.Doctor;
import logico.Paciente;

public class RegCita extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Componentes Paciente/Solicitante
    private JFormattedTextField txtCedula;
    private JTextField txtNombre;
    private JFormattedTextField txtTelefono;
    // CAMBIO: Usamos JDateChooser en lugar de JSpinner
    private JDateChooser dchFechaNacimiento; 
    private JButton btnBuscarPaciente;
    private JLabel lblTituloSeccionIzquierda; 
    
    // Componentes Cita
    private JComboBox<String> cbxDoctor;
    private JDateChooser dateChooserCita;
    private JLabel lblDisponibilidad; 
    
    private Paciente pacienteActual = null;
    private boolean esNuevoRegistro = false; 
    
    // Colores
    private Color verdePrincipal = new Color(4, 111, 67);
    private Color grisFondo = new Color(245, 245, 245);

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

    public RegCita() {
        setTitle("Registro de Citas");
        setBounds(100, 100, 1366, 768);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // --- 1. PANEL SUPERIOR (HEADER) ---
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(verdePrincipal);
        panelSuperior.setLayout(null);
        panelSuperior.setPreferredSize(new java.awt.Dimension(0, 140));
        contentPane.add(panelSuperior, BorderLayout.NORTH);
        
        ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
        Image imgLogoEscalada = iconLogo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imgLogoEscalada));
        lblLogo.setBounds(50, 30, 80, 80);
        panelSuperior.add(lblLogo);
        
        JLabel lblTitulo = new JLabel("PROGRAMACIÓN DE CITAS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 32f));
        lblTitulo.setBounds(150, 45, 600, 50);
        panelSuperior.add(lblTitulo);

        // --- 2. PANEL CENTRAL (CONTENIDO) ---
        JPanel panelContenido = new JPanel();
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setLayout(null);
        contentPane.add(panelContenido, BorderLayout.CENTER);
        
        // ========================================================================
        // SECCIÓN IZQUIERDA: DATOS DEL PACIENTE / SOLICITANTE
        // ========================================================================
        
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(grisFondo);
        panelIzquierdo.setBounds(100, 50, 500, 450);
        panelIzquierdo.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        panelIzquierdo.setLayout(null);
        panelContenido.add(panelIzquierdo);
        
        lblTituloSeccionIzquierda = new JLabel("1. DATOS DEL PACIENTE");
        lblTituloSeccionIzquierda.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f));
        lblTituloSeccionIzquierda.setForeground(verdePrincipal);
        lblTituloSeccionIzquierda.setBounds(30, 20, 350, 30);
        panelIzquierdo.add(lblTituloSeccionIzquierda);
        
        // Cédula y Búsqueda
        crearLabel(panelIzquierdo, "CÉDULA:", 70);
        try {
            MaskFormatter cedulaMask = new MaskFormatter("###-#######-#");
            cedulaMask.setPlaceholderCharacter('_');
            txtCedula = new JFormattedTextField(cedulaMask);
            txtCedula.setBounds(30, 100, 300, 35);
            txtCedula.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 15f));
            txtCedula.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
            panelIzquierdo.add(txtCedula);
        } catch (ParseException e) { e.printStackTrace(); }
        
        btnBuscarPaciente = new JButton("BUSCAR");
        btnBuscarPaciente.setBounds(340, 100, 120, 35);
        btnBuscarPaciente.setBackground(new Color(50, 50, 50));
        btnBuscarPaciente.setForeground(Color.WHITE);
        btnBuscarPaciente.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 12f));
        btnBuscarPaciente.setFocusPainted(false);
        btnBuscarPaciente.addActionListener(e -> buscarPaciente());
        panelIzquierdo.add(btnBuscarPaciente);
        
        // Nombre
        crearLabel(panelIzquierdo, "NOMBRE COMPLETO:", 150);
        txtNombre = crearInput(panelIzquierdo, 180);
        txtNombre.setEnabled(false); 
        
        // Teléfono con máscara
        crearLabel(panelIzquierdo, "TELÉFONO:", 230);
        try {
            MaskFormatter telMask = new MaskFormatter("(###) ###-####");
            telMask.setPlaceholderCharacter('_');
            txtTelefono = new JFormattedTextField(telMask);
            txtTelefono.setBounds(30, 260, 430, 35);
            txtTelefono.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 15f));
            txtTelefono.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
            txtTelefono.setEnabled(false); 
            panelIzquierdo.add(txtTelefono);
        } catch (ParseException e) { e.printStackTrace(); }
        
        // Fecha Nacimiento (CAMBIO: JDateChooser)
        crearLabel(panelIzquierdo, "FECHA DE NACIMIENTO:", 310);
        dchFechaNacimiento = new JDateChooser();
        dchFechaNacimiento.setBounds(30, 340, 430, 35);
        dchFechaNacimiento.setDateFormatString("dd/MM/yyyy"); // Formato visual
        dchFechaNacimiento.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 15f));
        // Opcional: Establecer un borde similar a los otros campos si se desea
        dchFechaNacimiento.getCalendarButton().setBackground(Color.WHITE);
        dchFechaNacimiento.setEnabled(false); 
        panelIzquierdo.add(dchFechaNacimiento);

        // ========================================================================
        // SECCIÓN DERECHA: DATOS DE LA CITA
        // ========================================================================
        
        JPanel panelDerecho = new JPanel();
        panelDerecho.setBackground(grisFondo);
        panelDerecho.setBounds(700, 50, 500, 450);
        panelDerecho.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        panelDerecho.setLayout(null);
        panelContenido.add(panelDerecho);
        
        JLabel lblTituloCita = new JLabel("2. DETALLES DE LA CITA");
        lblTituloCita.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f));
        lblTituloCita.setForeground(verdePrincipal);
        lblTituloCita.setBounds(30, 20, 300, 30);
        panelDerecho.add(lblTituloCita);
        
        // Selección de Doctor
        crearLabel(panelDerecho, "DOCTOR:", 70);
        cbxDoctor = new JComboBox<String>();
        cbxDoctor.setBounds(30, 100, 430, 35);
        cbxDoctor.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 14f));
        cbxDoctor.setBackground(Color.WHITE);
        
        // Evento para actualizar disponibilidad al cambiar doctor
        cbxDoctor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarDisponibilidad();
            }
        });
        panelDerecho.add(cbxDoctor);
        
        // Label de Disponibilidad (Debajo del combo)
        lblDisponibilidad = new JLabel("");
        lblDisponibilidad.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 13f));
        lblDisponibilidad.setForeground(new Color(100, 100, 100));
        lblDisponibilidad.setBounds(30, 140, 430, 20);
        panelDerecho.add(lblDisponibilidad);
        
        // Fecha Cita (JDateChooser)
        crearLabel(panelDerecho, "FECHA DE LA CITA:", 170);
        dateChooserCita = new JDateChooser();
        dateChooserCita.setBounds(30, 200, 430, 35);
        dateChooserCita.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 14f));
        dateChooserCita.setMinSelectableDate(new Date()); 
        
        // Evento para actualizar disponibilidad al cambiar fecha
        dateChooserCita.getDateEditor().addPropertyChangeListener(
            new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    if ("date".equals(e.getPropertyName())) {
                        actualizarDisponibilidad();
                    }
                }
            });
        
        panelDerecho.add(dateChooserCita);
        
        cargarDoctores(); 
        
        // --- BOTONES DE ACCIÓN ---
        
        JButton btnRegistrar = new JButton("PROGRAMAR CITA");
        btnRegistrar.setBounds(467, 511, 366, 50);
        btnRegistrar.setBackground(new Color(22, 163, 74));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 16f));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> registrarCita());
        panelContenido.add(btnRegistrar);
        
        // Botón Volver
        crearBotonVolver(panelContenido);
    }
    
    // ---------------- MÉTODOS LÓGICOS ----------------
    
    private void cargarDoctores() {
        ArrayList<Doctor> doctores = Clinica.getInstance().getDoctores();
        cbxDoctor.removeAllItems();
        cbxDoctor.addItem("<Seleccione un Doctor>");
        for (Doctor d : doctores) {
            if(d.isActivo()) {
                cbxDoctor.addItem(d.getId() + " - " + d.getNombre() + " (" + d.getEspecialidad() + ")");
            }
        }
    }
    
    // Método para calcular y mostrar disponibilidad en tiempo real
    private void actualizarDisponibilidad() {
        if (cbxDoctor.getSelectedIndex() <= 0 || dateChooserCita.getDate() == null) {
            lblDisponibilidad.setText("");
            return;
        }
        
        // 1. Obtener Doctor
        String docStr = (String) cbxDoctor.getSelectedItem();
        int idDoc = Integer.parseInt(docStr.split(" - ")[0]);
        Doctor doctorSeleccionado = null;
        for(Doctor d : Clinica.getInstance().getDoctores()) {
            if(d.getId() == idDoc) {
                doctorSeleccionado = d;
                break;
            }
        }
        
        if (doctorSeleccionado == null) return;
        
        // 2. Obtener Fecha
        Date fechaDate = dateChooserCita.getDate();
        LocalDate fechaLocal = fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // 3. Contar citas existentes
        int citasHoy = 0;
        for (Cita c : Clinica.getInstance().getCitas()) {
            if (c.getDoctor().equals(doctorSeleccionado) && 
                c.getFecha().isEqual(fechaLocal) && 
                !c.getEstado().equals("Cancelada")) {
                citasHoy++;
            }
        }
        
        // 4. Mostrar información
        int cupoTotal = doctorSeleccionado.getCupoDia();
        int restantes = cupoTotal - citasHoy;
        
        String texto = "Disponibilidad para el " + fechaLocal + ": " + restantes + " de " + cupoTotal + " turnos.";
        lblDisponibilidad.setText(texto);
        
        // Cambiar color si está lleno
        if (restantes <= 0) {
            lblDisponibilidad.setForeground(new Color(220, 38, 38)); // Rojo
            lblDisponibilidad.setText(texto + " (AGOTADO)");
        } else if (restantes <= 2) {
            lblDisponibilidad.setForeground(new Color(230, 140, 0)); // Naranja (Poco cupo)
        } else {
            lblDisponibilidad.setForeground(new Color(22, 163, 74)); // Verde
        }
    }
    
    private void buscarPaciente() {
        String cedula = txtCedula.getText(); 
        String cedulaLimpia = cedula.replace("-", "").replace("_", "").trim();
        
        if(cedulaLimpia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 1. Buscar en Pacientes
        Paciente p = Clinica.getInstance().getPacientePorCedula(cedula);
        
        if (p != null) {
            // ES PACIENTE REGISTRADO
            pacienteActual = p;
            esNuevoRegistro = false;
            
            lblTituloSeccionIzquierda.setText("1. DATOS DEL PACIENTE");
            txtNombre.setText(p.getNombre());
            txtTelefono.setText(p.getTelefono());
            
            if (p.getFechaNacimiento() != null) {
                Date date = Date.from(p.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
                // CAMBIO: Establecer fecha en JDateChooser
                dchFechaNacimiento.setDate(date);
            }
            
            bloquearCamposIzquierdos(true);
            JOptionPane.showMessageDialog(this, "Paciente encontrado en el sistema.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } else {
            // NO ES PACIENTE -> ES SOLICITANTE
            pacienteActual = null;
            esNuevoRegistro = true;
            
            lblTituloSeccionIzquierda.setText("1. DATOS DEL SOLICITANTE"); 
            
            limpiarCamposIzquierdos();
            bloquearCamposIzquierdos(false); 
            
            JOptionPane.showMessageDialog(this, "Cédula no registrada. Ingrese los datos del solicitante.", "Nuevo Registro", JOptionPane.INFORMATION_MESSAGE);
            txtNombre.requestFocus();
        }
    }
    
    private void registrarCita() {
        String cedulaLimpia = txtCedula.getText().replace("-", "").replace("_", "").trim();
        
        // CAMBIO: Validar usando dchFechaNacimiento.getDate()
        if(cedulaLimpia.isEmpty() || txtNombre.getText().isEmpty() || cbxDoctor.getSelectedIndex() <= 0 || dateChooserCita.getDate() == null || dchFechaNacimiento.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (esNuevoRegistro || pacienteActual == null) {
            Paciente nuevoPaciente = new Paciente();
            nuevoPaciente.setCedula(txtCedula.getText());
            nuevoPaciente.setNombre(txtNombre.getText());
            nuevoPaciente.setTelefono(txtTelefono.getText());
            
            // CAMBIO: Obtener fecha desde JDateChooser
            Date fechaNacDate = dchFechaNacimiento.getDate();
            LocalDate fechaNacLocal = fechaNacDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            nuevoPaciente.setFechaNacimiento(fechaNacLocal);
            
            Clinica.getInstance().registrarPaciente(nuevoPaciente);
            pacienteActual = nuevoPaciente;
        }
        
        String docStr = (String) cbxDoctor.getSelectedItem();
        int idDoc = Integer.parseInt(docStr.split(" - ")[0]);
        Doctor doctorSeleccionado = null;
        for(Doctor d : Clinica.getInstance().getDoctores()) {
            if(d.getId() == idDoc) {
                doctorSeleccionado = d;
                break;
            }
        }
        
        Date fechaCitaDate = dateChooserCita.getDate();
        LocalDate fechaCitaLocal = fechaCitaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String horaPorDefecto = "08:00 AM"; 
        
        boolean exito = Clinica.getInstance().programarCita(pacienteActual, doctorSeleccionado, fechaCitaLocal, horaPorDefecto);
        
        if(exito) {
            Clinica.getInstance().guardarDatos();
            JOptionPane.showMessageDialog(this, "Cita programada exitosamente para el " + fechaCitaLocal.toString(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Error al programar: " + Clinica.getInstance().getUltimoMensajeError(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bloquearCamposIzquierdos(boolean bloquear) {
        txtNombre.setEnabled(!bloquear);
        txtTelefono.setEnabled(!bloquear);
        // CAMBIO: Habilitar/Deshabilitar JDateChooser
        dchFechaNacimiento.setEnabled(!bloquear);
    }
    
    private void limpiarCamposIzquierdos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        // CAMBIO: Resetear JDateChooser a null o fecha actual
        dchFechaNacimiento.setDate(new Date());
    }
    
    // ---------------- AUXILIARES VISUALES ----------------
    
    private void crearLabel(JPanel panel, String texto, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 12f));
        lbl.setForeground(new Color(100, 100, 100));
        lbl.setBounds(30, y, 300, 20);
        panel.add(lbl);
    }
    
    private JTextField crearInput(JPanel panel, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(30, y, 430, 35);
        txt.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 15f));
        txt.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        panel.add(txt);
        return txt;
    }
    
    private void crearBotonVolver(JPanel panel) {
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
        btnVolver.setBounds(22, 511, 70, 70);
        btnVolver.setBackground(verdePrincipal);
        
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
                btnVolver.setBackground(verdePrincipal);
                btnVolver.repaint();
            }
        });
        
        JLabel lblFlecha = new JLabel(new ImageIcon(imgFlechaScaled));
        lblFlecha.setBounds(17, 17, 35, 35);
        btnVolver.add(lblFlecha);
        panel.add(btnVolver);
    }
}