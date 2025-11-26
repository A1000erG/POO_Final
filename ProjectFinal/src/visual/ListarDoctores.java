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
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField; // Importamos JPasswordField
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import Utilidades.FuenteUtil; 
import logico.Clinica;
import logico.Doctor;
import logico.Personal;

public class ListarDoctores extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtBuscador;
    private JTable table;
    private DefaultTableModel model;
    private Object[] rows;
    
    // Campos específicos
    private JTextField txtNombre;
    private JTextField txtEspecialidad;
    private JTextField txtCupo;
    private JTextField txtUsuario;
    private JTextField txtContrasenia;
    private JLabel lblFoto;
    
    private JLabel lblMensaje;
    
    private JButton btnModificar;
    private JButton btnEstado;
    
    private Doctor selectedDoctor = null;
    private Personal usuarioActual;
    private boolean buscar = false;

    private String nuevaRutaFotoTemp = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ListarDoctores dialog = new ListarDoctores(null);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    


    public ListarDoctores(Personal usuarioLogueado) {
        this.usuarioActual = usuarioLogueado;
        setTitle("Gestión de Doctores");
        setModal(true);
        // TAMAÑO 1366x768
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
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE DOCTORES");
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

        String[] headers = { "ID", "Nombre", "Especialidad", "Estado" };
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
                    String idNum = id.replace("D-", ""); 
                    try {
                        selectedDoctor = (Doctor) buscarDoctorPorId(Integer.parseInt(idNum));
                        cargarDatosDoctor();
                    } catch (NumberFormatException ex) { }
                }
            }
        });
        scrollPane.setViewportView(table);
        
        
        // --- LABEL PARA MENSAJES (DEBAJO DE LA TABLA) ---
        lblMensaje = new JLabel("");
        lblMensaje.setForeground(new Color(220, 38, 38));
        lblMensaje.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
        lblMensaje.setHorizontalAlignment(JLabel.CENTER);
        lblMensaje.setBounds(220, 530, 820, 25);
        panelCentral.add(lblMensaje);
        
        

        
        JPanel panelDetalle = new JPanel(); 
        panelDetalle.setBackground(new Color(245, 245, 245));
        panelDetalle.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        
        // Dimensiones ajustadas: 400 de ancho, 568 de alto (como pediste)
        panelDetalle.setBounds(900, margenY, 400, 568);
        panelDetalle.setLayout(null);
        panelCentral.add(panelDetalle);

        JLabel lblDetalleTitulo = new JLabel("DETALLES DEL DOCTOR");
        lblDetalleTitulo.setHorizontalAlignment(JLabel.CENTER);
        lblDetalleTitulo.setForeground(new Color(4, 111, 67));
        lblDetalleTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f));
        lblDetalleTitulo.setBounds(20, 20, 360, 30);
        panelDetalle.add(lblDetalleTitulo);
        
        // Foto
        lblFoto = new JLabel("");
        lblFoto.setBounds(125, 60, 150, 150);
        lblFoto.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.WHITE);
        lblFoto.setToolTipText("Click para cambiar foto");
        
        lblFoto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedDoctor != null && btnModificar.isEnabled()) { 
                    cambiarFotoDoctor();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedDoctor != null && btnModificar.isEnabled()) {
                    lblFoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblFoto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        cargarFotoEnLabel(null);
        panelDetalle.add(lblFoto);

        // Coordenadas ajustadas para que quepan bien los 5 campos
        int yStart = 230; 
        int gap = 55; 
        
        createLabelAndInput(panelDetalle, "NOMBRE:", yStart, txtNombre = new JTextField());
        createLabelAndInput(panelDetalle, "ESPECIALIDAD:", yStart + gap, txtEspecialidad = new JTextField());
        createLabelAndInput(panelDetalle, "CUPO DIARIO:", yStart + gap * 2, txtCupo = new JTextField());
        createLabelAndInput(panelDetalle, "USUARIO:", yStart + gap * 3, txtUsuario = new JTextField());
        
        // Campo de contraseña
        JLabel lblPass = new JLabel("CONTRASEÑA:");
        lblPass.setBounds(50, yStart + gap * 4, 300, 20);
        lblPass.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lblPass.setForeground(new Color(100, 100, 100));
        panelDetalle.add(lblPass);

        txtContrasenia = new JTextField();
        txtContrasenia.setBounds(50, yStart + gap * 4 + 20, 300, 35);
        txtContrasenia.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        txtContrasenia.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 15f));
        panelDetalle.add(txtContrasenia);
        
        txtUsuario.setEditable(false);
        txtUsuario.setBackground(new Color(240, 240, 240));
        
        
        // --- BOTONES ALINEADOS LADO A LADO ---
        // Ajustados más abajo para aprovechar el alto de 568
        int btnY = 510; 
        int btnWidth = 145; // Mitad del ancho disponible aprox

        btnModificar = new JButton("MODIFICAR");
        btnModificar.setBounds(50, btnY, btnWidth, 40);
        btnModificar.setBackground(new Color(150, 150, 150));
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnModificar.setFocusPainted(false);
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(e -> modificarDoctor());
        panelDetalle.add(btnModificar);

        btnEstado = new JButton("DESHABILITAR");
        btnEstado.setBounds(50 + btnWidth + 10, btnY, btnWidth, 40); // 10px de separación
        btnEstado.setBackground(new Color(150, 150, 150));
        btnEstado.setForeground(Color.WHITE);
        btnEstado.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnEstado.setFocusPainted(false);
        btnEstado.setEnabled(false);
        btnEstado.addActionListener(e -> cambiarEstadoDoctor());
        panelDetalle.add(btnEstado);

        // --- BOTÓN VOLVER CIRCULAR ---
        
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
        // Ubicado debajo de la tabla
        btnVolver.setBounds(40, 528, 70, 70);
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

        loadDoctores("");
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

    private void cargarFotoEnLabel(String ruta) {
        ImageIcon icon = null;
        if (ruta != null && !ruta.isEmpty()) {
            File imgFile = new File(ruta);
            if (imgFile.exists()) {
                icon = new ImageIcon(ruta);
            }
        }
        if (icon == null) {
            try {
                icon = new ImageIcon(getClass().getResource("/Imagenes/UsuarioGris.png"));
            } catch (Exception e) { icon = new ImageIcon(); }
        }
        if (icon != null && icon.getImage() != null) {
            Image img = icon.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
            lblFoto.setText("");
        } else {
            lblFoto.setIcon(null);
            lblFoto.setText("Sin Foto");
        }
    }

    private void iniciarHiloBuscador() {
        Thread hilo = new Thread(() -> {
            while (true) {
                if (buscar) {
                    try {
                        Thread.sleep(150);
                        String texto = txtBuscador.getText();
                        EventQueue.invokeLater(() -> loadDoctores(texto));
                        buscar = false;
                    } catch (InterruptedException e) { e.printStackTrace(); }
                }
                try { Thread.sleep(100); } catch (InterruptedException e) { break; }
            }
        });
        hilo.start();
    }

    private void loadDoctores(String filtro) {
        model.setRowCount(0);
        rows = new Object[model.getColumnCount()];
        ArrayList<Doctor> lista = Clinica.getInstance().getDoctores();
        int coincidencias = 0;
        Doctor unicoEncontrado = null;

        for (Doctor doc : lista) {
            String nombre = doc.getNombre().toLowerCase();
            String idStr = String.valueOf(doc.getId());
            String filtroMin = filtro.toLowerCase();

            if (filtro.isEmpty() || nombre.contains(filtroMin) || idStr.contains(filtroMin)) {
                rows[0] = String.format("D-%03d", doc.getId());
                rows[1] = doc.getNombre();
                rows[2] = doc.getEspecialidad();
                rows[3] = doc.isActivo() ? "Activo" : "Inactivo";
                model.addRow(rows);
                coincidencias++;
                unicoEncontrado = doc;
            }
        }
        
        if (coincidencias == 1 && !filtro.isEmpty()) {
            table.setRowSelectionInterval(0, 0);
            selectedDoctor = unicoEncontrado;
            cargarDatosDoctor();
        } else if (coincidencias != 1) {
            limpiarFormulario();
        }
    }

    private void cargarDatosDoctor() {
        if (selectedDoctor != null) {
            txtNombre.setText(selectedDoctor.getNombre());
            txtEspecialidad.setText(selectedDoctor.getEspecialidad());
            txtCupo.setText(String.valueOf(selectedDoctor.getCupoDia()));
            txtUsuario.setText(selectedDoctor.getUsuario());
            txtContrasenia.setText(selectedDoctor.getContrasenia());
            
            txtNombre.setEnabled(true);
            txtEspecialidad.setEnabled(true);
            txtCupo.setEnabled(true);
            txtContrasenia.setEnabled(true);
            
//          // --- SEGURIDAD: Solo ADMIN puede modificar la contraseña ---
//          boolean esAdmin = (usuarioActual instanceof Administrativo);
//          txtContrasenia.setEnabled(esAdmin);
            
            cargarFotoEnLabel(selectedDoctor.getRutaFoto());
            nuevaRutaFotoTemp = null; 
            
            btnModificar.setEnabled(true);
            btnEstado.setEnabled(true);
            
            btnModificar.setBackground(new Color(22, 163, 74));
            
            if (selectedDoctor.isActivo()) {
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
        txtEspecialidad.setText("");
        txtCupo.setText("");
        txtUsuario.setText("");
        txtContrasenia.setText("");
        
        txtNombre.setEnabled(false);
        txtEspecialidad.setEnabled(false);
        txtCupo.setEnabled(false);
        txtUsuario.setEnabled(false);
        txtContrasenia.setEnabled(false);
        
        cargarFotoEnLabel(null);
        
        btnModificar.setEnabled(false);
        btnEstado.setEnabled(false);
        
        btnModificar.setBackground(new Color(150, 150, 150));
        btnEstado.setBackground(new Color(150, 150, 150));
        
        btnEstado.setText("DESHABILITAR");
        btnEstado.setBackground(new Color(220, 38, 38));
        selectedDoctor = null;
        nuevaRutaFotoTemp = null;
    }

    private Doctor buscarDoctorPorId(int id) {
        for (Doctor d : Clinica.getInstance().getDoctores()) {
            if (d.getId() == id) return d;
        }
        return null;
    }

    private void cambiarFotoDoctor() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
        
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            nuevaRutaFotoTemp = archivo.getAbsolutePath();
            
            ImageIcon iconOriginal = new ImageIcon(nuevaRutaFotoTemp);
            Image imgEscalada = iconOriginal.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(imgEscalada));
        }
    }

    private void modificarDoctor() {
        if (selectedDoctor == null) return;
        
        String pass = txtContrasenia.getText();
        if(txtNombre.getText().isEmpty() || txtEspecialidad.getText().isEmpty() || txtCupo.getText().isEmpty() || pass.isEmpty()) {
        	mostrarMensajeError("Los campos no pueden estar vacíos.");
            return;
        }

        try {
            selectedDoctor.setNombre(txtNombre.getText());
            selectedDoctor.setEspecialidad(txtEspecialidad.getText());
            selectedDoctor.setCupoDia(Integer.parseInt(txtCupo.getText()));
            
            // Solo guardar contraseña si el campo estaba habilitado (es admin)
            if (txtContrasenia.isEnabled()) {
                selectedDoctor.setContrasenia(pass);
            }
            
            if (nuevaRutaFotoTemp != null) {
                try {
                    String rutaAnterior = selectedDoctor.getRutaFoto();
                    if (rutaAnterior != null && !rutaAnterior.isEmpty()) {
                        Path pathAnterior = Paths.get(rutaAnterior);
                        if (Files.exists(pathAnterior)) {
                            Files.delete(pathAnterior);
                        }
                    }
                    
                    File carpeta = new File("FotosDoctores");
                    if (!carpeta.exists()) carpeta.mkdirs();
                    
                    String extension = "";
                    int i = nuevaRutaFotoTemp.lastIndexOf('.');
                    if (i > 0) {
                        extension = nuevaRutaFotoTemp.substring(i);
                    }
                    
                    String nombreArchivo = String.format("doctor_%03d%s", selectedDoctor.getId(), extension);
                    File destino = new File(carpeta, nombreArchivo);
                    
                    Path origenPath = Paths.get(nuevaRutaFotoTemp);
                    Path destinoPath = destino.toPath();
                    
                    Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
                    
                    selectedDoctor.setRutaFoto(destino.getPath());
                    nuevaRutaFotoTemp = null; 
                    
                } catch (Exception e) {
                	mostrarMensajeError("Error al actualizar la foto: " + e.getMessage());
                    return;
                }
            }
            
            Clinica.getInstance().guardarDatos();
            mostrarMensajeExito("Doctor modificado correctamente.");
            loadDoctores(txtBuscador.getText());
            
        } catch (NumberFormatException e) {
        	mostrarMensajeError("El cupo debe ser un número válido.");
        }
    }

    private void cambiarEstadoDoctor() {
        if (selectedDoctor == null) return;

        // Bloqueo de seguridad: Un Doctor NO puede deshabilitar a otro (aunque el botón esté visible)
        if (usuarioActual instanceof Doctor) {
        	mostrarMensajeError("Acceso Denegado: Solo un Administrador puede inhabilitar Doctores.");
            return;
        }

        if (selectedDoctor.isActivo()) {
            String razon = JOptionPane.showInputDialog(this, "Ingrese la razón de la inhabilitación:", "Confirmar Inhabilitación", JOptionPane.WARNING_MESSAGE);
            if (razon != null && !razon.trim().isEmpty()) {
                selectedDoctor.setActivo(false);
                selectedDoctor.setCausaDeshabilitacion(razon);
                Clinica.getInstance().guardarDatos();
                mostrarMensajeExito("Doctor deshabilitado correctamente.");
                cargarDatosDoctor();
                loadDoctores(txtBuscador.getText());
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Razón anterior: " + selectedDoctor.getCausaDeshabilitacion() + "\n¿Desea habilitar nuevamente a este doctor?", 
                    "Confirmar Habilitación", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                selectedDoctor.setActivo(true);
                selectedDoctor.setCausaDeshabilitacion("");
                Clinica.getInstance().guardarDatos();
                mostrarMensajeExito("Doctor habilitado correctamente.");
                cargarDatosDoctor();
                loadDoctores(txtBuscador.getText());
            }
        }
    }
    
    /**
     * Método para mostrar mensajes de error
     */
    private void mostrarMensajeError(String mensaje) {
        lblMensaje.setForeground(new Color(220, 38, 38));
        lblMensaje.setText(mensaje);
        ocultarMensajeDespuesDeTiempo();
    }
    
    /**
     * Método para mostrar mensajes de éxito
     */
    private void mostrarMensajeExito(String mensaje) {
        lblMensaje.setForeground(new Color(22, 163, 74));
        lblMensaje.setText(mensaje);
        ocultarMensajeDespuesDeTiempo();
    }

    /**
     * Oculta el mensaje después de 3 segundos
     */
    private void ocultarMensajeDespuesDeTiempo() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                EventQueue.invokeLater(() -> {
                    lblMensaje.setText("");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}