package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame; // IMPORTANTE: Usamos JFrame
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import Utilidades.FuenteUtil;
import logico.Administrativo;
import logico.Cita;
import logico.Clinica;
import logico.Consulta; // IMPORTANTE: Importar Consulta para contar enfermedades reales
import logico.Doctor;
import logico.Enfermedad;
import logico.Vacuna;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;
    
    // Paleta de colores
    private static Color paletaAzulBonito = new Color(21, 129, 191);
    private static Color paletaAzulOscuro = new Color(15, 90, 140);
    private static Color paletaGrisClaro = new Color(240, 245, 250);
    private static Color paletaBlanco = new Color(255, 255, 255);
    private static Color paletaRojo = new Color(220, 38, 38);
    // Fuentes
    private static Font fuenteTituloGrafico = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f);
    private static Font fuenteNumeroIndicativo = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 20f);
    private static Font fuenteUsoNormal = FuenteUtil.cargarFuente("/Fuentes/Roboto-Light.ttf", 11f);
    private static Font fuenteNombreUsuario = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 13f);
    private static Font fuenteBoton = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 12f);
    private static Font fuenteTituloPanel = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 24f);
    private static Font fuenteInfoPanel = FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 14f);

    // Paneles Globales
    private JPanel panelContenido;
    private JPanel panelFondo;
    private JPanel panelOpciones;      
    private JPanel panelInfoUsuario;   
    
    private static JPanel panelInformacion;
    private static JPanel panelListadoInfo; 
    
    // Tarjetas informativas
    private static JPanel panelCantidadEnfermedades;
    private static JPanel panelCantidadVacunas;
    private static JPanel panelCantidadCitasHoy;
    
    // Paneles de Gráficos
    private static JPanel panelGraficoEnfermedades;
    private static JPanel panelGraficoCircular; 
    
    // Paneles de Listados (Botones cuadrados)
    private static JPanel panelListaDoctores;   
    private static JPanel panelListaAdministradores;    
    private static JPanel panelListaVacunas;    
    private static JPanel panelListaPacientes;
    private static JPanel panelListaEnfermedades; // Nuevo panel
    
    private JLabel etiquetaImagenDoctorGrande;
    
    // Instancia Lógica
    private static Clinica clinicaInstancia = Clinica.getInstance();
    
    // Control de Notificaciones
    private Timer timerNotificacion;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // PRUEBA: Asegúrate de que este usuario exista en tus datos
                    Principal framePrincipal = new Principal(0, "admin"); 
                    framePrincipal.setVisible(true);
                } catch (Exception excepcion) {
                    excepcion.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error crítico al iniciar.", "Error Fatal", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public Principal(int modoOperacion, String idUsuario) {
        getToolkit().getScreenSize();
        // --- CONFIGURACIÓN CORREGIDA: JFRAME ESTÁNDAR ---
        // Ya NO usamos setModal(true) porque bloquea todo.
        setResizable(false);
        setTitle("Compile Salud - Gestión Clínica");
        
        // Mantenemos las dimensiones y posición solicitadas
        setBounds(100, 100, 1366, 768);
        setLocationRelativeTo(null); 
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 

        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent eventoVentana) {
                confirmarCierreAplicacion();
            }
            
            // --- ACTUALIZACIÓN AUTOMÁTICA AL VOLVER EL FOCO ---
            @Override
            public void windowActivated(WindowEvent e) {
                // Al volver a la ventana principal, refrescamos los gráficos para ver cambios recientes
                if(panelGraficoEnfermedades != null) {
                    panelGraficoEnfermedades.removeAll();
                    configurarGraficoBarras();
                    panelGraficoEnfermedades.revalidate();
                    panelGraficoEnfermedades.repaint();
                }
                // También podríamos refrescar las tarjetas informativas aquí si fuera necesario
            }
        });

        inicializarComponentesVisuales(modoOperacion, idUsuario);
        
        // Iniciar sistema de notificaciones si es doctor
        if (modoOperacion != 0) {
            iniciarNotificacionesDoctor(idUsuario);
        }
    }
    
    private void iniciarNotificacionesDoctor(String idUsuario) {
        timerNotificacion = new Timer(60000, e -> verificarCitasProximas(idUsuario));
        timerNotificacion.start();
    }
    
    private void verificarCitasProximas(String idUsuario) {
        try {
            Doctor doc = clinicaInstancia.getDoctorPorUsuario(idUsuario);
            if (doc == null) return;

            LocalDate hoy = LocalDate.now();
            LocalTime ahora = LocalTime.now();

            ArrayList<Cita> citas = clinicaInstancia.getCitas();
            if (citas != null) {
                for (Cita c : citas) {
                    if (c.getDoctor() != null && 
                        c.getDoctor().getId() == doc.getId() && 
                        c.getFecha().isEqual(hoy) && 
                        "Pendiente".equalsIgnoreCase(c.getEstado())) {
                        
                        try {
                            LocalTime horaCita = LocalTime.parse(c.getHora());
                            long minutosRestantes = ChronoUnit.MINUTES.between(ahora, horaCita);
                            
                            if (minutosRestantes == 10) {
                                JOptionPane.showMessageDialog(this, 
                                    "RECORDATORIO: Tienes una cita con el paciente " + c.getPaciente().getNombre() + 
                                    "\nen 10 minutos (" + c.getHora() + ").", 
                                    "Cita Próxima", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception ex) { }
                    }
                }
            }
        } catch (Exception e) {
            // Silencioso para no interrumpir flujo
        }
    }

    private void inicializarComponentesVisuales(int modoOperacion, String idUsuario) {
        panelContenido = new JPanel();
        panelContenido.setPreferredSize(new Dimension(820, 3100)); 
        setContentPane(panelContenido);
        panelContenido.setLayout(new BorderLayout(0, 0));

        panelFondo = new JPanel();
        panelFondo.setBackground(paletaGrisClaro); 
        panelContenido.add(panelFondo, BorderLayout.CENTER);
        panelFondo.setLayout(null);

        // Panel Derecho (Información dinámica)
        panelInformacion = new JPanel();
        panelInformacion.setPreferredSize(new Dimension(1121, 1050)); 
        panelInformacion.setLayout(null);
        panelInformacion.setOpaque(false);

        // ScrollPane para el contenido derecho
        JScrollPane scrollPane = new JScrollPane(panelInformacion);
        scrollPane.setBounds(240, 0, 1120, 735); 
        scrollPane.setBorder(null); 
        scrollPane.getViewport().setOpaque(false); 
        scrollPane.setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        
        panelFondo.add(scrollPane);

        // Construir Menú Lateral
        construirMenuLateral(modoOperacion, idUsuario);

        // Construir Cabecera
        construirCabeceraUsuario(modoOperacion, idUsuario);

        // Construir Paneles Centrales
        construirDashboardCentral(modoOperacion);
        
        // Construir Paneles de Listados (Solo Admin)
        if(modoOperacion == 0) {
            construirSeccionListados(modoOperacion);
        }
    }

    private void construirMenuLateral(int modoOperacion, String idUsuario) {
        panelOpciones = new JPanel(); 
        panelOpciones.setBackground(paletaAzulBonito);
        panelOpciones.setLayout(null);
        panelOpciones.setBounds(0, 0, 240, 1000); 
        panelFondo.add(panelOpciones);

        // Logo
        JLabel etiquetaLogo = new JLabel(cargarIcono("/Imagenes/logoBlanco.png", 76, 76));
        etiquetaLogo.setBounds(82, 26, 76, 76);
        panelOpciones.add(etiquetaLogo);
        
        // --- TEXTO COMPILE SALUD ---
        JLabel lblNombreApp = new JLabel("COMPILE SALUD");
        lblNombreApp.setForeground(paletaBlanco);
        lblNombreApp.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 20f)); // Fuente gruesa
        lblNombreApp.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombreApp.setBounds(0, 110, 240, 30); // Centrado en el menú lateral
        panelOpciones.add(lblNombreApp);

        // --- Botones del Menú ---

        int yPos = 177;
        int gap = 47;

        // 1. Botón Superior
        String textoBoton1 = (modoOperacion == 0) ? "Registrar Admin" : "Mis Consultas";
        JButton boton1 = crearBotonMenu(textoBoton1, yPos);
        boton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(modoOperacion == 0) {
                    abrirVentanaRegistroAdmin(modoOperacion, idUsuario);
                } else {
                    Doctor doc = clinicaInstancia.getDoctorPorUsuario(idUsuario);
                    if(doc != null) abrirVentanaListarCitas(doc);
                }
            }
        });
        panelOpciones.add(boton1);
        yPos += gap;

        // 2. Botón Medio
        if(modoOperacion == 0) {
            JButton boton2 = crearBotonMenu("Registrar Doctor", yPos);
            boton2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    abrirVentanaRegistroDoctor(modoOperacion, idUsuario);
                }
            });
            panelOpciones.add(boton2);
            yPos += gap;
        }

        // 3. Botón Registro Cita
        JButton boton3 = crearBotonMenu("Nueva Cita", yPos);
        boton3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirVentanaRegistroCita();
            }
        });
        panelOpciones.add(boton3);
        yPos += gap;
        
        // --- BOTONES ELIMINADOS DE AQUÍ (Vacunas y Enfermedades) ---
        
        // 4. Botón GESTIONAR CITAS (Solo Admin)
        if(modoOperacion == 0) {
            JButton botonGestionCitas = crearBotonMenu("Gestionar Citas", yPos);
            botonGestionCitas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) { 
                    abrirVentanaListarCitas(null); 
                }
            });
            panelOpciones.add(botonGestionCitas);
            yPos += gap;
        }

        // 5. Botón Reportes (Solo Admin)
        if (modoOperacion == 0) {
            JButton botonReportes = crearBotonMenu("Estadísticas", yPos);
            botonReportes.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    abrirVentanaReportes();
                }
            });
            panelOpciones.add(botonReportes);
            yPos += gap;
        }

        // 6. Botón Listados (Solo Admin)
        if (modoOperacion == 0) {
            JButton botonListados = crearBotonMenu("Listados", yPos);
            botonListados.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cambiarVistaAListados(true);
                }
            });
            panelOpciones.add(botonListados);
            yPos += gap;
        }
        
        // 7. Botón Cerrar Sesión
        yPos = 650;
        JButton botonCerrar = crearBotonMenu("Cerrar Sesión", yPos);
        botonCerrar.setBackground(paletaRojo);
        botonCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cerrarSesion();
            }
            @Override
            public void mouseEntered(MouseEvent e) { botonCerrar.setBackground(new Color(180, 0, 0)); }
            @Override
            public void mouseExited(MouseEvent e) { botonCerrar.setBackground(paletaRojo); }
        });
        panelOpciones.add(botonCerrar);
    }

    private void construirCabeceraUsuario(int modoOperacion, String idUsuario) {
        panelInfoUsuario = new JPanel(); 
        panelInfoUsuario.setBackground(paletaAzulOscuro);
        panelInfoUsuario.setBounds(0, 0, 1131, 120);
        panelInfoUsuario.setLayout(null);
        panelInformacion.add(panelInfoUsuario);
        
        String nombreUsuario = "Usuario";
        String rolUsuario = (modoOperacion == 0) ? "Administrativo" : "Doctor";

        if(modoOperacion == 0) {
            try {
                if(clinicaInstancia.getAdministrativos() != null && clinicaInstancia.getAdministrativos().size() > 0) {
                    Administrativo admin = clinicaInstancia.getAdministrativoPorUsuario(idUsuario);
                    if (admin != null) nombreUsuario = admin.getNombre();
                } 
            } catch (Exception e) { }
        } else {
            try {
                if(clinicaInstancia.getDoctores() != null && clinicaInstancia.getDoctores().size() > 0) {
                    Doctor doc = clinicaInstancia.getDoctorPorUsuario(idUsuario);
                    if (doc != null) nombreUsuario = doc.getNombre();
                }
            } catch (Exception e) { }
        }

        JLabel etiquetaNombre = new JLabel(nombreUsuario);
        etiquetaNombre.setForeground(paletaBlanco);
        etiquetaNombre.setFont(fuenteNombreUsuario);
        etiquetaNombre.setBounds(846, 37, 153, 14);
        panelInfoUsuario.add(etiquetaNombre);

        JLabel etiquetaRol = new JLabel(rolUsuario);
        etiquetaRol.setForeground(paletaBlanco);
        etiquetaRol.setFont(fuenteUsoNormal);
        etiquetaRol.setBounds(846, 62, 119, 14);
        panelInfoUsuario.add(etiquetaRol);
        
        String rutaImagen = buscarImagenUsuario(modoOperacion, idUsuario);
        ImageIcon iconoUsuario = null;
        
        boolean imagenCargada = false;
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            File f = new File(rutaImagen);
            if (f.exists()) {
                iconoUsuario = new ImageIcon(rutaImagen);
                if (iconoUsuario.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                    imagenCargada = true;
                }
            }
        }

        if (!imagenCargada) {
            iconoUsuario = cargarIcono("/Imagenes/useResi.png", 80, 80);
        } else {
            if(iconoUsuario.getImage() != null) {
                Image img = iconoUsuario.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                iconoUsuario = new ImageIcon(img);
            }
        }
        
        JLabel etiquetaFoto = new JLabel(iconoUsuario);
        etiquetaFoto.setBounds(1020, 11, 80, 80);
        panelInfoUsuario.add(etiquetaFoto);
    }

    private void construirDashboardCentral(int modoOperacion) {
        if(modoOperacion != 0) {
            JPanel panelBienvenidaDoctor = new JPanel(null);
            panelBienvenidaDoctor.setOpaque(false);
            panelBienvenidaDoctor.setBounds(57, 177, 1014, 250); 
            
            // 1. Imagen Doctora
            JLabel etiquetaDoctora = new JLabel(cargarIcono("/Imagenes/doctora.png", 200, 220));
            etiquetaDoctora.setBounds(30, 15, 200, 220);
            
            // 2. Textos
            JLabel lblBienvenida = new JLabel("¡Bienvenido al Panel Médico!");
            lblBienvenida.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 32f)); 
            lblBienvenida.setForeground(paletaAzulOscuro);
            lblBienvenida.setBounds(250, 60, 500, 40);
            
            JLabel lblSubtitulo = new JLabel("Gestiona tus citas y pacientes desde aquí.");
            lblSubtitulo.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 16f));
            lblSubtitulo.setForeground(new Color(100, 100, 100)); 
            lblSubtitulo.setBounds(250, 105, 500, 25);

            // 3. Logo COMPILE SALUD a la derecha (Estilo Banner)
            JLabel lblMarca = new JLabel("<html><font color='#16A34A'>COMPILE</font> <span style='background-color:#16A34A; color:white'>&nbsp;SALUD&nbsp;</span></html>");
            lblMarca.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 36f));
            lblMarca.setBounds(680, 60, 320, 50);
            // Icono de cruz opcional si se desea
            JLabel lblCruz = new JLabel(cargarIcono("/Imagenes/cruzVerde.png", 50, 50)); 
            lblCruz.setBounds(620, 60, 50, 50);
            

            // Orden de adición
            panelBienvenidaDoctor.add(etiquetaDoctora); 
            panelBienvenidaDoctor.add(lblBienvenida);   
            panelBienvenidaDoctor.add(lblSubtitulo);
            panelBienvenidaDoctor.add(lblMarca); // Agregamos el logo

            panelInformacion.add(panelBienvenidaDoctor);
            return; 
        }

        panelCantidadEnfermedades = crearTarjetaEstadistica("Enfermedades Controladas", 
                clinicaInstancia.getCatalogoEnfermedades().size(), "/Imagenes/thermometer.png", 57, 177);
        panelInformacion.add(panelCantidadEnfermedades);

        panelCantidadVacunas = crearTarjetaEstadistica("Vacunas Existentes", 
                clinicaInstancia.getVacunas().size(), "/Imagenes/syringe.png", 414, 177);
        panelInformacion.add(panelCantidadVacunas);

        panelCantidadCitasHoy = crearTarjetaEstadistica("Citas para hoy", 
                contarCitasDeHoy(), "/Imagenes/stethoscope.png", 771, 177);
        panelInformacion.add(panelCantidadCitasHoy);

        panelGraficoEnfermedades = crearPanelRedondeado(paletaBlanco, 20);
        panelGraficoEnfermedades.setBounds(57, 355, 1014, 318);
        panelGraficoEnfermedades.setLayout(null);
        configurarGraficoBarras();
        panelInformacion.add(panelGraficoEnfermedades);
        
        panelGraficoCircular = crearPanelRedondeado(paletaBlanco, 20);
        panelGraficoCircular.setBounds(57, 685, 1014, 318);
        panelGraficoCircular.setLayout(null);
        configurarGraficoPastel();
        panelInformacion.add(panelGraficoCircular);
    }

    private void construirSeccionListados(int modoOperacion) {
        etiquetaImagenDoctorGrande = new JLabel(cargarIcono("/Imagenes/doctorPanel2.png", 300, 286));
        etiquetaImagenDoctorGrande.setBounds(680, 115, 300, 286);
        etiquetaImagenDoctorGrande.setVisible(false);
        panelInformacion.add(etiquetaImagenDoctorGrande);

        panelListadoInfo = crearPanelRedondeado(paletaAzulBonito, 30);
        panelListadoInfo.setBounds(57, 177, 1014, 225);
        panelListadoInfo.setVisible(false);
        panelListadoInfo.setLayout(null);
        panelInformacion.add(panelListadoInfo);

        JLabel lblTitulo = new JLabel("Panel de Gestión");
        lblTitulo.setFont(fuenteTituloPanel);
        lblTitulo.setForeground(paletaBlanco);
        lblTitulo.setBounds(40, 30, 300, 30);
        panelListadoInfo.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Seleccione una categoría para visualizar.");
        lblSubtitulo.setFont(fuenteInfoPanel);
        lblSubtitulo.setForeground(paletaBlanco);
        lblSubtitulo.setBounds(45, 75, 300, 25);
        panelListadoInfo.add(lblSubtitulo);

        JButton botonVolver = new JButton("Volver al Dashboard");
        botonVolver.setFont(fuenteBoton);
        botonVolver.setForeground(paletaAzulBonito);
        botonVolver.setBackground(paletaBlanco);
        // CORREGIDO: Botón más arriba para que no se corte
        botonVolver.setBounds(40, 125, 250, 35);
        botonVolver.setBorderPainted(false);
        botonVolver.setFocusPainted(false);
        botonVolver.addActionListener(e -> cambiarVistaAListados(false));
        panelListadoInfo.add(botonVolver);

        // --- DISEÑO DE LISTADOS EN DOS FILAS ---
        int wBoton = 210;
        int gap = 50;
        int xStart = 57;
        
        // FILA 1 (y = 450)
        int yFila1 = 450;
        
        // 1. Pacientes
        panelListaPacientes = crearBotonCuadradoListado("Lista de pacientes", "/Imagenes/pacientList.png", xStart, yFila1, wBoton);
        panelListaPacientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { abrirVentanaListado("Pacientes"); }
        });
        panelInformacion.add(panelListaPacientes);
        
        // 2. Vacunas
        panelListaVacunas = crearBotonCuadradoListado("Lista de vacunas", "/Imagenes/vacinList.png", xStart + wBoton + gap, yFila1, wBoton);
        panelListaVacunas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { abrirVentanaListado("Vacunas"); }
        });
        panelInformacion.add(panelListaVacunas);
        
        // 3. Enfermedades (NUEVO)
        panelListaEnfermedades = crearBotonCuadradoListado("Lista Enfermedades", "/Imagenes/thermometer.png", xStart + (wBoton + gap)*2, yFila1, wBoton);
        panelListaEnfermedades.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { abrirVentanaListado("Enfermedades"); }
        });
        panelInformacion.add(panelListaEnfermedades);

        // FILA 2 (y = 450 + 210 + 30 = 690)
        int yFila2 = 690;

        // 4. Administradores
        panelListaAdministradores = crearBotonCuadradoListado("Lista de admins", "/Imagenes/adminList.png", xStart, yFila2, wBoton);
        panelListaAdministradores.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { abrirVentanaListado("Administradores"); }
        });
        panelInformacion.add(panelListaAdministradores);

        // 5. Doctores
        panelListaDoctores = crearBotonCuadradoListado("Lista de doctores", "/Imagenes/doctorList.png", xStart + wBoton + gap, yFila2, wBoton);
        panelListaDoctores.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { abrirVentanaListado("Doctores"); }
        });
        panelInformacion.add(panelListaDoctores);
    }

    // =========================================================================================
    //                                  FUNCIONES GENERADORAS
    // =========================================================================================

    private ImageIcon cargarIcono(String ruta, int w, int h) {
        java.net.URL url = getClass().getResource(ruta);
        if (url == null) {
            url = getClass().getResource("/Recursos" + ruta); 
        }
        
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            if (icon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
            }
        }
        return new ImageIcon(); 
    }

    private JButton crearBotonMenu(String texto, int posicionY) {
        JButton boton = new JButton(texto);
        boton.setForeground(paletaBlanco);
        boton.setFont(fuenteBoton);
        boton.setBounds(0, posicionY, 240, 47);
        boton.setBackground(paletaAzulBonito);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                if(!boton.getBackground().equals(paletaRojo)) boton.setBackground(paletaAzulOscuro); 
            }
            public void mouseExited(MouseEvent e) { 
                if(!boton.getBackground().equals(paletaRojo) && !boton.getBackground().equals(new Color(180,0,0))) boton.setBackground(paletaAzulBonito); 
            }
        });
        return boton;
    }

    private JPanel crearPanelRedondeado(Color colorFondo, int radio) {
        return new JPanel() {
            private static final long serialVersionUID = 1L;
            { setOpaque(false); }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo
                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radio, radio);
                
                // Borde (Marco)
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radio, radio);
                
                super.paintComponent(g);
            }
        };
    }

    private JPanel crearTarjetaEstadistica(String titulo, int cantidad, String rutaIcono, int x, int y) {
        JPanel panel = crearPanelRedondeado(paletaBlanco, 20);
        panel.setBounds(x, y, 300, 130);
        panel.setLayout(null);

        JLabel img = new JLabel(cargarIcono(rutaIcono, 108, 108));
        img.setBounds(171, 11, 108, 108);
        panel.add(img);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(fuenteUsoNormal);
        lblTitulo.setBounds(10, 105, 180, 14);
        panel.add(lblTitulo);

        JLabel lblCant = new JLabel(String.valueOf(cantidad));
        lblCant.setFont(fuenteNumeroIndicativo);
        lblCant.setBounds(10, 11, 80, 50);
        panel.add(lblCant);
        return panel;
    }

    private JPanel crearBotonCuadradoListado(String texto, String rutaIcono, int x, int y, int width) {
        JPanel panel = crearPanelRedondeado(paletaBlanco, 30);
        panel.setBounds(x, y, width, width);
        panel.setVisible(false);
        panel.setLayout(null);

        JLabel img = new JLabel(cargarIcono(rutaIcono, 90, 90));
        int imgX = (width - 90) / 2;
        img.setBounds(imgX, 30, 90, 90); 
        panel.add(img);

        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuenteUsoNormal);
        lbl.setForeground(paletaAzulBonito);
        lbl.setBounds(10, width - 40, width - 20, 20);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lbl);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { panel.setBackground(new Color(220, 235, 255)); panel.repaint(); }
            public void mouseExited(MouseEvent e) { panel.setBackground(paletaBlanco); panel.repaint(); }
        });
        return panel;
    }

    // --- CORRECCIÓN: Usar datos reales de Consultas ---
    private void configurarGraficoBarras() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<Enfermedad> enfermedades = clinicaInstancia.getCatalogoEnfermedades();
        ArrayList<Consulta> consultas = clinicaInstancia.getConsultas();
        
        if (enfermedades != null && consultas != null) {
            for (Enfermedad enf : enfermedades) {
                int cantidad = 0;
                for (Consulta c : consultas) {
                    if (c.getEnfermedad() != null && c.getEnfermedad().getIdEnfermedad() == enf.getIdEnfermedad()) {
                        cantidad++;
                    }
                }
                if (cantidad > 0) {
                    dataset.addValue(cantidad, "Pacientes", enf.getNombre());
                }
            }
        }

        JFreeChart chart = ChartFactory.createBarChart("Pacientes por Enfermedad", "Enfermedad", "Cant", dataset, PlotOrientation.VERTICAL, false, true, false);
        aplicarEstiloGrafico(chart);
        
        ChartPanel panel = new ChartPanel(chart);
        panel.setBounds(30, 5, 954, 308);
        panelGraficoEnfermedades.add(panel);
    }
    
    private void configurarGraficoPastel() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        ArrayList<Vacuna> list = clinicaInstancia.getVacunas();
        if (!list.isEmpty()) {
            int max = Math.min(list.size(), 5);
            for (int i=0; i<max; i++) dataset.setValue(list.get(i).getNombre(), 1);
            JFreeChart chart = ChartFactory.createPieChart("Distribución Vacunas", dataset, true, true, false);
            aplicarEstiloGrafico(chart);
            ChartPanel panel = new ChartPanel(chart);
            panel.setBounds(30, 5, 954, 308);
            panelGraficoCircular.add(panel);
        }
    }
    
    private void aplicarEstiloGrafico(JFreeChart chart) {
        chart.setBackgroundPaint(paletaBlanco);
        chart.getTitle().setFont(fuenteTituloGrafico);
        if(chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setBackgroundPaint(paletaBlanco);
            plot.setOutlineVisible(false);
            ((BarRenderer)plot.getRenderer()).setSeriesPaint(0, paletaAzulBonito);
            ((BarRenderer)plot.getRenderer()).setBarPainter(new StandardBarPainter());
        } else {
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setBackgroundPaint(paletaBlanco);
            plot.setOutlineVisible(false);
        }
    }

    private int contarCitasDeHoy() {
        LocalDate hoy = LocalDate.now();
        int c = 0;
        for (Cita cita : clinicaInstancia.getCitas()) {
            if (cita.getFecha().isEqual(hoy)) c++;
        }
        return c;
    }

    private String buscarImagenUsuario(int modo, String idUsuario) {
        String baseName = "";
        File carpeta = null;
        
        if (modo == 0) { // Admin
            Administrativo admin = clinicaInstancia.getAdministrativoPorUsuario(idUsuario);
            if (admin != null) {
                baseName = String.format("admin_%03d", admin.getIdAdmin());
                carpeta = new File("FotosAdmin");
            }
        } else { // Doctor
            Doctor doc = clinicaInstancia.getDoctorPorUsuario(idUsuario);
            if (doc != null) {
                baseName = String.format("doctor_%03d", doc.getId());
                carpeta = new File("FotosDoctores");
            }
        }

        if (carpeta != null) {
            if (carpeta.exists() && carpeta.isDirectory()) {
                File[] archivos = carpeta.listFiles();
                if (archivos != null) {
                    for (File f : archivos) {
                        String name = f.getName();
                        if (name.startsWith(baseName) && (name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"))) {
                            return f.getPath();
                        }
                    }
                }
            }
        }
        return "";
    }

    private void confirmarCierreAplicacion() {
        if(JOptionPane.showConfirmDialog(null, "¿Salir de la aplicación?", "Cierre", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clinicaInstancia.guardarDatosLocal();
            dispose();
            System.exit(0);
        }
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(null, "¿Desea cerrar sesión y volver al inicio?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            clinicaInstancia.guardarDatosLocal(); 
            dispose();
            try {
                new Login().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cambiarVistaAListados(boolean mostrar) {
        panelCantidadEnfermedades.setVisible(!mostrar);
        panelCantidadVacunas.setVisible(!mostrar);
        panelCantidadCitasHoy.setVisible(!mostrar);
        panelGraficoEnfermedades.setVisible(!mostrar);
        if(panelGraficoCircular != null) panelGraficoCircular.setVisible(!mostrar);

        panelListadoInfo.setVisible(mostrar);
        panelListaAdministradores.setVisible(mostrar);
        panelListaVacunas.setVisible(mostrar);
        panelListaDoctores.setVisible(mostrar);
        panelListaPacientes.setVisible(mostrar);
        if(panelListaEnfermedades != null) panelListaEnfermedades.setVisible(mostrar);
        etiquetaImagenDoctorGrande.setVisible(mostrar);
    }

    private void abrirVentanaRegistroAdmin(int m, String u) {
        try { new RegAdmin(m, u).setVisible(true); } catch(Exception e) { e.printStackTrace(); }
    }
    private void abrirVentanaRegistroDoctor(int m, String u) {
        try { new RegDoctor(m, u).setVisible(true); } catch(Exception e) { e.printStackTrace(); }
    }
    
    private void abrirVentanaRegistroCita() {
        try { 
            new RegCita().setVisible(true); 
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    private void abrirVentanaReportes() {
        try { 
            if (clinicaInstancia.getConsultas() == null) clinicaInstancia.setConsultas(new ArrayList<>());
            new Reportes().setVisible(true); 
        } catch(Throwable e) { 
            JOptionPane.showMessageDialog(null, "No hay suficientes datos para generar estadísticas.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void abrirVentanaListarCitas(Doctor doctor) {
        try { new ListarCitas(doctor).setVisible(true); } catch(Exception e) { e.printStackTrace(); }
    }
    
    private void abrirVentanaListado(String tipo) {
        try {
            switch (tipo) {
                case "Doctores": new ListarDoctores().setVisible(true); break;
                case "Administradores": new ListarAdministradores().setVisible(true); break;
                case "Vacunas": new ListarVacunas().setVisible(true); break;
                case "Pacientes": new ListarPacientes().setVisible(true); break;
                case "Enfermedades": new ListarEnfermedades().setVisible(true); break;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}