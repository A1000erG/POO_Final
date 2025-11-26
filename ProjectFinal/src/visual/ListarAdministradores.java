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
//import javax.swing.JPasswordField; 
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import Utilidades.FuenteUtil; 
import logico.Administrativo;
import logico.Clinica;
import logico.Personal;

public class ListarAdministradores extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtBuscador;
    private JTable table;
    private DefaultTableModel model;
    private Object[] rows;
    
    private JTextField txtNombre;
    private JTextField txtCargo;
    private JTextField txtUsuario;
    private JTextField txtContrasenia;
    private JLabel lblFoto;
    
    private JLabel lblMensaje;
    
    private JButton btnModificar;
    private JButton btnEstado;
    
    private Administrativo selectedAdmin = null;
    private Personal usuarioActual;
    private boolean buscar = false;
    
    private String nuevaRutaFotoTemp = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ListarAdministradores dialog = new ListarAdministradores(null);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    public ListarAdministradores(Personal usuarioLogueado) {
        this.usuarioActual = usuarioLogueado;
        setTitle("Gestión de Administradores");
        setModal(true);
        // TAMAÑO 1366x768 EXACTO
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
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE ADMINISTRADORES");
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

        String[] headers = { "ID", "Nombre", "Cargo", "Estado" };
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
                    String idNum = id.replace("A-", ""); 
                    try {
                        selectedAdmin = (Administrativo) buscarAdminPorId(Integer.parseInt(idNum));
                        cargarDatosAdmin();
                    } catch (NumberFormatException ex) { }
                }
            }
        });
        scrollPane.setViewportView(table);

        
        // --- LABEL PARA MENSAJES ---
        lblMensaje = new JLabel("");
        lblMensaje.setForeground(new Color(220, 38, 38));
        lblMensaje.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 14f));
        lblMensaje.setHorizontalAlignment(JLabel.CENTER);
        lblMensaje.setBounds(290, 530, 820, 25);
        panelCentral.add(lblMensaje);
        
        
        // === SECCIÓN DERECHA (DETALLES) ===
        
        JPanel panelDetalle = new JPanel(); 
        panelDetalle.setBackground(new Color(245, 245, 245));
        panelDetalle.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        
        // TAMAÑO AMPLIADO A 568px DE ALTO (IGUAL QUE DOCTORES)
        panelDetalle.setBounds(900, margenY, 400, 568);
        panelDetalle.setLayout(null);
        panelCentral.add(panelDetalle);

        JLabel lblDetalleTitulo = new JLabel("DETALLES DEL USUARIO");
        lblDetalleTitulo.setHorizontalAlignment(JLabel.CENTER);
        lblDetalleTitulo.setForeground(new Color(4, 111, 67));
        lblDetalleTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f));
        lblDetalleTitulo.setBounds(20, 20, 360, 30);
        panelDetalle.add(lblDetalleTitulo);

        lblFoto = new JLabel("");
        lblFoto.setBounds(125, 60, 150, 150);
        lblFoto.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.WHITE);
        lblFoto.setToolTipText("Click para cambiar foto");
        
        lblFoto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedAdmin != null && btnModificar.isEnabled()) { 
                    cambiarFotoAdmin();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedAdmin != null && btnModificar.isEnabled()) {
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

        // Ajuste de espaciado vertical
        int yStart = 230;
        int gap = 60;
        
        createLabelAndInput(panelDetalle, "NOMBRE:", yStart, txtNombre = new JTextField());
        createLabelAndInput(panelDetalle, "CARGO:", yStart + gap, txtCargo = new JTextField());
        createLabelAndInput(panelDetalle, "USUARIO:", yStart + gap * 2, txtUsuario = new JTextField());
        
        JLabel lblPass = new JLabel("CONTRASEÑA:");
        lblPass.setBounds(50, yStart + gap * 3, 300, 20);
        lblPass.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Light.ttf", 13f));
        lblPass.setForeground(new Color(100, 100, 100));
        panelDetalle.add(lblPass);

        txtContrasenia = new JTextField();
        txtContrasenia.setBounds(50, yStart + gap * 3 + 20, 300, 35);
        txtContrasenia.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        txtContrasenia.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Regular.ttf", 15f));
        panelDetalle.add(txtContrasenia);

        txtUsuario.setEditable(false); 
        txtUsuario.setBackground(new Color(240, 240, 240));

        // --- BOTONES ALINEADOS LADO A LADO ---
        int btnY = 510;
        int btnWidth = 145;

        btnModificar = new JButton("MODIFICAR");
        btnModificar.setBounds(50, btnY, btnWidth, 40);
        btnModificar.setBackground(new Color(150, 150, 150)); 
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnModificar.setFocusPainted(false);
        btnModificar.setEnabled(false);
        btnModificar.addActionListener(e -> modificarAdmin());
        panelDetalle.add(btnModificar);

        btnEstado = new JButton("DESHABILITAR");
        btnEstado.setBounds(50 + btnWidth + 10, btnY, btnWidth, 40); // Separación 10px
        btnEstado.setBackground(new Color(150, 150, 150)); 
        btnEstado.setForeground(Color.WHITE);
        btnEstado.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 13f));
        btnEstado.setFocusPainted(false);
        btnEstado.setEnabled(false);
        btnEstado.addActionListener(e -> cambiarEstadoAdmin());
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
        // Ubicado debajo de la tabla (Y=560)
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

        loadAdmins("");
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
                        EventQueue.invokeLater(() -> loadAdmins(texto));
                        buscar = false;
                    } catch (InterruptedException e) { e.printStackTrace(); }
                }
                try { Thread.sleep(100); } catch (InterruptedException e) { break; }
            }
        });
        hilo.start();
    }

    private void loadAdmins(String filtro) {
        model.setRowCount(0);
        rows = new Object[model.getColumnCount()];
        ArrayList<Administrativo> lista = Clinica.getInstance().getAdministrativos();
        int coincidencias = 0;
        Administrativo unicoEncontrado = null;

        for (Administrativo adm : lista) {
            String nombre = adm.getNombre().toLowerCase();
            String idStr = String.valueOf(adm.getId());
            String filtroMin = filtro.toLowerCase();

            if (filtro.isEmpty() || nombre.contains(filtroMin) || idStr.contains(filtroMin)) {
                rows[0] = String.format("A-%03d", adm.getId());
                rows[1] = adm.getNombre();
                rows[2] = adm.getCargo();
                rows[3] = adm.isActivo() ? "Activo" : "Inactivo";
                model.addRow(rows);
                coincidencias++;
                unicoEncontrado = adm;
            }
        }
        
        if (coincidencias == 1 && !filtro.isEmpty()) {
            table.setRowSelectionInterval(0, 0);
            selectedAdmin = unicoEncontrado;
            cargarDatosAdmin();
        } else if (coincidencias != 1) {
            limpiarFormulario();
        }
    }

    private void cargarDatosAdmin() {
        if (selectedAdmin != null) {
            txtNombre.setText(selectedAdmin.getNombre());
            txtCargo.setText(selectedAdmin.getCargo());
            txtUsuario.setText(selectedAdmin.getUsuario());
            txtContrasenia.setText(selectedAdmin.getContrasenia()); 
            
            txtNombre.setEnabled(true);
            txtCargo.setEnabled(true);
            txtContrasenia.setEnabled(true);
            
//            // Validación de seguridad para la contraseña
//            boolean esAdmin = (usuarioActual instanceof Administrativo);
//            txtContrasenia.setEnabled(esAdmin);
            
            cargarFotoEnLabel(selectedAdmin.getRutaFoto());
            nuevaRutaFotoTemp = null;
            
            btnModificar.setEnabled(true);
            btnEstado.setEnabled(true);
            
            btnModificar.setBackground(new Color(22, 163, 74));
            
            if (selectedAdmin.isActivo()) {
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
        txtCargo.setText("");
        txtUsuario.setText("");
        txtContrasenia.setText(""); 
        
        txtNombre.setEnabled(false);
        txtCargo.setEnabled(false);
        txtUsuario.setEnabled(false);
        txtContrasenia.setEnabled(false); 
        
        cargarFotoEnLabel(null);
        
        btnModificar.setEnabled(false);
        btnEstado.setEnabled(false);
        
        btnModificar.setBackground(new Color(150, 150, 150));
        btnEstado.setBackground(new Color(150, 150, 150));
        
        btnEstado.setText("DESHABILITAR");
        btnEstado.setBackground(new Color(220, 38, 38));
        selectedAdmin = null;
        nuevaRutaFotoTemp = null;
    }

    private Administrativo buscarAdminPorId(int id) {
        for (Administrativo a : Clinica.getInstance().getAdministrativos()) {
            if (a.getId() == id) return a;
        }
        return null;
    }

    private void cambiarFotoAdmin() {
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

    private void modificarAdmin() {
        if (selectedAdmin == null) return;
        
        String pass = new String(txtContrasenia.getText());
        if(txtNombre.getText().isEmpty() || txtCargo.getText().isEmpty() || pass.isEmpty()) {
        	mostrarMensajeError("Los campos no pueden estar vacíos.");
            return;
        }

        selectedAdmin.setNombre(txtNombre.getText());
        selectedAdmin.setCargo(txtCargo.getText());
        
        if (txtContrasenia.isEnabled()) {
            selectedAdmin.setContrasenia(pass); 
        }
        
        if (nuevaRutaFotoTemp != null) {
            try {
                String rutaAnterior = selectedAdmin.getRutaFoto();
                if (rutaAnterior != null && !rutaAnterior.isEmpty()) {
                    Path pathAnterior = Paths.get(rutaAnterior);
                    if (Files.exists(pathAnterior)) {
                        Files.delete(pathAnterior);
                    }
                }
                
                File carpeta = new File("FotosAdmin");
                if (!carpeta.exists()) carpeta.mkdirs();
                
                String extension = "";
                int i = nuevaRutaFotoTemp.lastIndexOf('.');
                if (i > 0) {
                    extension = nuevaRutaFotoTemp.substring(i);
                }
                
                String nombreArchivo = String.format("admin_%03d%s", selectedAdmin.getId(), extension);
                File destino = new File(carpeta, nombreArchivo);
                
                Path origenPath = Paths.get(nuevaRutaFotoTemp);
                Path destinoPath = destino.toPath();
                
                Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
                
                selectedAdmin.setRutaFoto(destino.getPath());
                nuevaRutaFotoTemp = null;
                
            } catch (Exception e) {
            	mostrarMensajeError("Error al actualizar la foto: " + e.getMessage());
                return;
            }
        }
        
        Clinica.getInstance().guardarDatos();
        mostrarMensajeExito("Administrador modificado correctamente.");

        loadAdmins(txtBuscador.getText());
    }

    private void cambiarEstadoAdmin() {
        if (selectedAdmin == null) return;

        if (usuarioActual != null && usuarioActual instanceof Administrativo && usuarioActual.getId() == selectedAdmin.getId()) {
        	mostrarMensajeError("No puedes inhabilitarte a ti mismo.");
            return;
        }

        if (selectedAdmin.isActivo()) {
            String razon = JOptionPane.showInputDialog(this, "Razón de inhabilitación:", "Confirmar", JOptionPane.WARNING_MESSAGE);
            if (razon != null && !razon.trim().isEmpty()) {
                selectedAdmin.setActivo(false);
                selectedAdmin.setCausaDeshabilitacion(razon);
                Clinica.getInstance().guardarDatos();
                mostrarMensajeExito("Administrador deshabilitado correctamente.");
                cargarDatosAdmin();
                loadAdmins(txtBuscador.getText());
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Razón anterior: " + selectedAdmin.getCausaDeshabilitacion() + "\n¿Desea habilitar nuevamente a este usuario?", 
                    "Confirmar Habilitación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                selectedAdmin.setActivo(true);
                selectedAdmin.setCausaDeshabilitacion("");
                Clinica.getInstance().guardarDatos();
                mostrarMensajeExito("Administrador habilitado correctamente.");
                cargarDatosAdmin();
                loadAdmins(txtBuscador.getText());
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