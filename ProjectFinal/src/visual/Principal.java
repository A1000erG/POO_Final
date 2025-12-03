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
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import Utilidades.FuenteUtil;
import logico.Administrativo;
import logico.Cita;
import logico.Clinica;
import logico.Consulta;
import logico.Doctor;
import logico.Enfermedad;
import logico.Vacuna;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;

    // Constantes de diseño
    private static Color paletaAzulBonito = new Color(21, 129, 191);
    private static Color paletaAzulOscuro = new Color(15, 90, 140);
    private static Color paletaGrisClaro = new Color(240, 245, 250);
    private static Color paletaBlanco = new Color(255, 255, 255);
    private static Color paletaRojo = new Color(220, 38, 38);
    private static Font fuenteTituloGrafico = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 18f);
    private static Font fuenteNumeroIndicativo = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 20f);
    private static Font fuenteUsoNormal = FuenteUtil.cargarFuente("/Fuentes/Roboto-Light.ttf", 11f);
    private static Font fuenteNombreUsuario = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 13f);
    private static Font fuenteBoton = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 12f);
    private static Font fuenteTituloPanel = FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 24f);
    private static Font fuenteInfoPanel = FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 14f);

    // Componentes globales
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
    private static JPanel panelGraficoVacunas;

    // Paneles de Listados
    private static JPanel panelListaDoctores;
    private static JPanel panelListaAdministradores;
    private static JPanel panelListaVacunas;
    private static JPanel panelListaPacientes;
    private static JPanel panelListaEnfermedades;

    private JLabel etiquetaImagenDoctorGrande;
    private static Clinica clinicaInstancia = Clinica.getInstance();
    private Timer timerNotificacion;

    /*
       Función: main
       Argumentos: (String[]) argumentosConsola: Argumentos pasados por línea de comandos al iniciar.
       Objetivo: Punto de entrada de la aplicación, inicia la interfaz gráfica en el hilo de despacho de eventos.
       Retorno: (void): No retorna valores, solo inicia la ejecución.
    */
    public static void main(String[] argumentosConsola) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ejecutarAplicacionSegura();
            }
        });
    }

    /*
       Función: Principal (Constructor)
       Argumentos: (int) modoOperacion: Define si es Admin (0) o Doctor (1).
                   (String) idUsuario: Identificador único del usuario logueado.
       Objetivo: Inicializar la ventana principal, configurar dimensiones y cargar componentes.
       Retorno: (Instancia de Principal): Retorna el objeto creado.
    */
    public Principal(int modoOperacion, String idUsuario) {
        getToolkit().getScreenSize();
        setResizable(false);
        setTitle("Compile Salud - Gestión Clínica");
        setBounds(100, 100, 1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        configurarEventosDeVentana();
        inicializarComponentesVisuales(modoOperacion, idUsuario);

        if (modoOperacion != 0) {
            iniciarNotificacionesDoctor(idUsuario);
        }
    }

    /*
       Función: configurarEventosDeVentana
       Argumentos: Ninguno.
       Objetivo: Asignar los listeners para el cierre y la activación de la ventana.
       Retorno: (void): Configura los listeners internamente.
    */
    private void configurarEventosDeVentana() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent eventoCierre) {
                confirmarCierreAplicacion();
            }

            @Override
            public void windowActivated(WindowEvent eventoActivacion) {
                refrescarGraficos();
            }
        });
    }

    /*
       Función: refrescarGraficos
       Argumentos: Ninguno.
       Objetivo: Redibujar los gráficos si los paneles existen, para reflejar cambios en los datos.
       Retorno: (void): Actualiza la interfaz visual.
    */
    private void refrescarGraficos() {
        if (panelGraficoEnfermedades != null) {
            panelGraficoEnfermedades.removeAll();
            configurarGraficoBarrasEnfermedades();
            panelGraficoEnfermedades.revalidate();
            panelGraficoEnfermedades.repaint();
        }
        if (panelGraficoVacunas != null) {
            panelGraficoVacunas.removeAll();
            configurarGraficoInventarioVacunas();
            panelGraficoVacunas.revalidate();
            panelGraficoVacunas.repaint();
        }
        if (panelCantidadCitasHoy != null) {
             // Actualizar contador tarjeta cita
             actualizarTarjetaCita();
        }
    }

    /*
       Función: actualizarTarjetaCita
       Argumentos: Ninguno.
       Objetivo: Actualiza el número visual de citas para hoy.
       Retorno: (void): Modifica el componente visual directamente.
    */
    private void actualizarTarjetaCita() {
        panelCantidadCitasHoy.removeAll();
        JLabel img = new JLabel(cargarIcono("/Imagenes/stethoscope.png", 108, 108));
        img.setBounds(171, 11, 108, 108);
        panelCantidadCitasHoy.add(img);

        JLabel lblTitulo = new JLabel("Citas para hoy");
        lblTitulo.setFont(fuenteUsoNormal);
        lblTitulo.setBounds(10, 105, 180, 14);
        panelCantidadCitasHoy.add(lblTitulo);

        JLabel lblCant = new JLabel(String.valueOf(contarCitasDeHoy()));
        lblCant.setFont(fuenteNumeroIndicativo);
        lblCant.setBounds(10, 11, 80, 50);
        panelCantidadCitasHoy.add(lblCant);
        panelCantidadCitasHoy.revalidate();
        panelCantidadCitasHoy.repaint();
    }

    /*
       Función: iniciarNotificacionesDoctor
       Argumentos: (String) idUsuario: El ID del doctor para filtrar sus citas.
       Objetivo: Iniciar un Timer que verifica citas próximas cada minuto.
       Retorno: (void): Inicia el hilo del Timer.
    */
    private void iniciarNotificacionesDoctor(String idUsuario) {
        timerNotificacion = new Timer(60000, eventoTiempo -> verificarCitasProximas(idUsuario));
        timerNotificacion.start();
    }

    /*
       Función: verificarCitasProximas
       Argumentos: (String) idUsuario: El ID del doctor logueado.
       Objetivo: Buscar citas pendientes en los próximos 10 minutos y mostrar alerta.
       Retorno: (void): Muestra un JOptionPane si hay coincidencia.
    */
    private void verificarCitasProximas(String idUsuario) {
        Doctor doctorActual = clinicaInstancia.getDoctorPorUsuario(idUsuario);
        if (doctorActual == null) return;

        LocalDate fechaHoy = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        ArrayList<Cita> listaCitas = clinicaInstancia.getCitas();

        if (listaCitas == null) return;

        for (Cita citaActual : listaCitas) {
            if (esCitaProxima(citaActual, doctorActual, fechaHoy, horaActual)) {
                mostrarAlertaCita(citaActual);
            }
        }
    }

    /*
       Función: esCitaProxima
       Argumentos: (Cita) cita: La cita a evaluar.
                   (Doctor) doctor: El doctor dueño de la sesión.
                   (LocalDate) hoy: Fecha actual.
                   (LocalTime) ahora: Hora actual.
       Objetivo: Determinar si la cita cumple todas las condiciones para ser notificada.
       Retorno: (boolean): True si la cita es del doctor, es hoy, pendiente y falta poco tiempo.
    */
    private boolean esCitaProxima(Cita cita, Doctor doctor, LocalDate hoy, LocalTime ahora) {
        if (cita.getDoctor() == null) return false;
        if (cita.getDoctor().getId() != doctor.getId()) return false;
        if (!cita.getFecha().isEqual(hoy)) return false;
        if (!"Pendiente".equalsIgnoreCase(cita.getEstado())) return false;

        return calcularDiferenciaMinutos(ahora, cita.getHora()) == 10;
    }

    /*
       Función: mostrarAlertaCita
       Argumentos: (Cita) cita: La cita que generó la alerta.
       Objetivo: Desplegar el mensaje visual al usuario.
       Retorno: (void): Ejecuta JOptionPane.
    */
    private void mostrarAlertaCita(Cita cita) {
        JOptionPane.showMessageDialog(this,
            "RECORDATORIO: Tienes una cita con el paciente " + cita.getPaciente().getNombre() +
            "\nen 10 minutos (" + cita.getHora() + ").",
            "Cita Próxima", JOptionPane.INFORMATION_MESSAGE);
    }

    /*
       Función: inicializarComponentesVisuales
       Argumentos: (int) modoOperacion: Rol del usuario.
                   (String) idUsuario: ID del usuario.
       Objetivo: Orquestar la creación de todos los paneles y elementos de la interfaz.
       Retorno: (void): Construye la UI.
    */
    private void inicializarComponentesVisuales(int modoOperacion, String idUsuario) {
        panelContenido = new JPanel();
        panelContenido.setPreferredSize(new Dimension(820, 1100)); // Ajustado para evitar scroll innecesario si no hay muchos datos
        setContentPane(panelContenido);
        panelContenido.setLayout(new BorderLayout(0, 0));

        panelFondo = new JPanel();
        panelFondo.setBackground(paletaGrisClaro);
        panelContenido.add(panelFondo, BorderLayout.CENTER);
        panelFondo.setLayout(null);

        panelInformacion = new JPanel();
        panelInformacion.setPreferredSize(new Dimension(1121, 1050));
        panelInformacion.setLayout(null);
        panelInformacion.setOpaque(false);

        JScrollPane panelDesplazamiento = new JScrollPane(panelInformacion);
        panelDesplazamiento.setBounds(240, 0, 1120, 735);
        panelDesplazamiento.setBorder(null);
        panelDesplazamiento.getViewport().setOpaque(false);
        panelDesplazamiento.setOpaque(false);
        panelDesplazamiento.getVerticalScrollBar().setUnitIncrement(16);

        panelFondo.add(panelDesplazamiento);

        construirMenuLateral(modoOperacion, idUsuario);
        construirCabeceraUsuario(modoOperacion, idUsuario);
        construirDashboardCentral(modoOperacion);

        if (modoOperacion == 0) {
            construirSeccionListados(modoOperacion);
        }
    }

    /*
       Función: construirMenuLateral
       Argumentos: (int) modoOperacion: Rol.
                   (String) idUsuario: ID.
       Objetivo: Crear la barra lateral azul con botones de navegación.
       Retorno: (void): Agrega componentes al panelFondo.
    */
    private void construirMenuLateral(int modoOperacion, String idUsuario) {
        panelOpciones = new JPanel();
        panelOpciones.setBackground(paletaAzulBonito);
        panelOpciones.setLayout(null);
        panelOpciones.setBounds(0, 0, 240, 1000);
        panelFondo.add(panelOpciones);

        JLabel etiquetaLogo = new JLabel(cargarIcono("/Imagenes/logoBlanco.png", 76, 76));
        etiquetaLogo.setBounds(82, 26, 76, 76);
        panelOpciones.add(etiquetaLogo);

        JLabel etiquetaNombreApp = new JLabel("COMPILE SALUD");
        etiquetaNombreApp.setForeground(paletaBlanco);
        etiquetaNombreApp.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 20f));
        etiquetaNombreApp.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaNombreApp.setBounds(0, 125, 240, 30);
        panelOpciones.add(etiquetaNombreApp);

        int posicionVertical = 177;
        int separacion = 47;

        String textoBotonPrimario = (modoOperacion == 0) ? "Registrar Admin" : "Mis Consultas";
        agregarBotonMenu(textoBotonPrimario, posicionVertical, e -> accionBotonPrimario(modoOperacion, idUsuario));
        posicionVertical += separacion;

        if (modoOperacion == 0) {
            agregarBotonMenu("Registrar Doctor", posicionVertical, e -> abrirVentanaRegistroDoctor(modoOperacion, idUsuario));
            posicionVertical += separacion;
        }

        agregarBotonMenu("Nueva Cita", posicionVertical, e -> abrirVentanaRegistroCita());
        posicionVertical += separacion;

        if (modoOperacion == 0) {
            // --- NUEVO BOTÓN: Gestión de Vacunas/Recursos (Solo Admin) ---
            agregarBotonMenu("Gestión de Recursos", posicionVertical, e -> abrirVentanaGestionRecursos());
            posicionVertical += separacion;
            
            agregarBotonMenu("Gestionar Citas", posicionVertical, e -> abrirVentanaListarCitas(null));
            posicionVertical += separacion;

            agregarBotonMenu("Estadísticas", posicionVertical, e -> abrirVentanaReportes());
            posicionVertical += separacion;

            agregarBotonMenu("Listados", posicionVertical, e -> cambiarVistaAListados(true));
            posicionVertical += separacion;
        }

        agregarBotonCerrarSesion(650);
    }

    /*
       Función: agregarBotonMenu
       Argumentos: (String) texto: Etiqueta del botón.
                   (int) y: Posición vertical.
                   (java.awt.event.ActionListener) accion: Lógica al hacer clic.
       Objetivo: Crear y añadir un botón estandarizado al menú.
       Retorno: (void): Añade el botón al panelOpciones.
    */
    private void agregarBotonMenu(String texto, int y, java.awt.event.ActionListener accion) {
        JButton botonNuevo = crearBotonEstiloMenu(texto, y);
        botonNuevo.addActionListener(accion);
        panelOpciones.add(botonNuevo);
    }

    /*
       Función: agregarBotonCerrarSesion
       Argumentos: (int) y: Posición vertical.
       Objetivo: Crear el botón rojo de salida.
       Retorno: (void): Añade el botón.
    */
    private void agregarBotonCerrarSesion(int y) {
        JButton botonCerrar = crearBotonEstiloMenu("Cerrar Sesión", y);
        botonCerrar.setBackground(paletaRojo);
        botonCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { cerrarSesion(); }
            @Override
            public void mouseEntered(MouseEvent e) { botonCerrar.setBackground(new Color(180, 0, 0)); }
            @Override
            public void mouseExited(MouseEvent e) { botonCerrar.setBackground(paletaRojo); }
        });
        panelOpciones.add(botonCerrar);
    }

    /*
       Función: construirCabeceraUsuario
       Argumentos: (int) modoOperacion: Rol.
                   (String) idUsuario: ID.
       Objetivo: Mostrar la información y foto del usuario en la parte superior.
       Retorno: (void): Añade componentes al panelInfoUsuario.
    */
    private void construirCabeceraUsuario(int modoOperacion, String idUsuario) {
        panelInfoUsuario = new JPanel();
        panelInfoUsuario.setBackground(paletaAzulOscuro);
        panelInfoUsuario.setBounds(0, 0, 1131, 120);
        panelInfoUsuario.setLayout(null);
        panelInformacion.add(panelInfoUsuario);

        String nombreUsuario = obtenerNombreUsuario(modoOperacion, idUsuario);
        String rolUsuario = (modoOperacion == 0) ? "Administrativo" : "Doctor";

        JLabel etiquetaNombre = new JLabel(nombreUsuario);
        etiquetaNombre.setForeground(paletaBlanco);
        etiquetaNombre.setFont(fuenteNombreUsuario);
        etiquetaNombre.setHorizontalAlignment(SwingConstants.RIGHT);
        etiquetaNombre.setBounds(680, 37, 250, 20);
        panelInfoUsuario.add(etiquetaNombre);

        JLabel etiquetaRol = new JLabel(rolUsuario);
        etiquetaRol.setForeground(paletaBlanco);
        etiquetaRol.setFont(fuenteUsoNormal);
        etiquetaRol.setHorizontalAlignment(SwingConstants.RIGHT);
        etiquetaRol.setBounds(680, 62, 250, 20);
        panelInfoUsuario.add(etiquetaRol);

        ImageIcon iconoUsuario = cargarImagenUsuarioSegura(modoOperacion, idUsuario);
        JLabel etiquetaFoto = new JLabel(iconoUsuario);
        etiquetaFoto.setBounds(940, 11, 109, 98);
        panelInfoUsuario.add(etiquetaFoto);
    }

    /*
       Función: construirDashboardCentral
       Argumentos: (int) modoOperacion: Rol.
       Objetivo: Crear los paneles de estadísticas y gráficos del centro.
       Retorno: (void): Añade componentes al panelInformacion.
    */
    private void construirDashboardCentral(int modoOperacion) {
        if (modoOperacion != 0) {
            construirBienvenidaDoctor();
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
        configurarGraficoBarrasEnfermedades();
        panelInformacion.add(panelGraficoEnfermedades);

        panelGraficoVacunas = crearPanelRedondeado(paletaBlanco, 20);
        panelGraficoVacunas.setBounds(57, 685, 1014, 318);
        panelGraficoVacunas.setLayout(null);
        configurarGraficoInventarioVacunas();
        panelInformacion.add(panelGraficoVacunas);
    }

    /*
       Función: construirBienvenidaDoctor
       Argumentos: Ninguno.
       Objetivo: Mostrar un panel especial si el usuario es doctor.
       Retorno: (void): Añade el panel de bienvenida.
    */
    private void construirBienvenidaDoctor() {
        JPanel panelBienvenida = crearPanelRedondeado(paletaBlanco, 30);
        panelBienvenida.setBounds(57, 177, 1014, 250);
        panelBienvenida.setLayout(null); 

        JLabel etiquetaDoctora = new JLabel(cargarIcono("/Imagenes/doctora.png", 200, 220));
        etiquetaDoctora.setBounds(30, 15, 200, 220);

        JLabel etiquetaTitulo = new JLabel("¡Bienvenido al Panel Médico!");
        etiquetaTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 32f));
        etiquetaTitulo.setForeground(paletaAzulOscuro);
        etiquetaTitulo.setBounds(250, 60, 500, 40);

        JLabel etiquetaSubtitulo = new JLabel("Gestiona tus citas y pacientes desde aquí.");
        etiquetaSubtitulo.setFont(FuenteUtil.cargarFuente("/Fuentes/Roboto-Regular.ttf", 16f));
        etiquetaSubtitulo.setForeground(new Color(100, 100, 100));
        etiquetaSubtitulo.setBounds(250, 105, 500, 25);

        JLabel etiquetaMarca = new JLabel("<html><font color='#16A34A'>COMPILE</font> <span style='background-color:#16A34A; color:white'>&nbsp;SALUD&nbsp;</span></html>");
        etiquetaMarca.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 36f));
        etiquetaMarca.setBounds(680, 60, 350, 300);

        panelBienvenida.add(etiquetaDoctora);
        panelBienvenida.add(etiquetaTitulo);
        panelBienvenida.add(etiquetaSubtitulo);
        panelBienvenida.add(etiquetaMarca);

        panelInformacion.add(panelBienvenida);
    }

    /*
       Función: construirSeccionListados
       Argumentos: (int) modoOperacion: Rol.
       Objetivo: Crear los botones cuadrados de acceso directo a listas (ocultos inicialmente).
       Retorno: (void): Añade los paneles al panelInformacion.
    */
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

        JLabel etiquetaTituloListado = new JLabel("Panel de Gestión");
        etiquetaTituloListado.setFont(fuenteTituloPanel);
        etiquetaTituloListado.setForeground(paletaBlanco);
        etiquetaTituloListado.setBounds(40, 30, 300, 30);
        panelListadoInfo.add(etiquetaTituloListado);

        JLabel etiquetaSubTituloListado = new JLabel("Seleccione una categoría para visualizar.");
        etiquetaSubTituloListado.setFont(fuenteInfoPanel);
        etiquetaSubTituloListado.setForeground(paletaBlanco);
        etiquetaSubTituloListado.setBounds(45, 75, 300, 25);
        panelListadoInfo.add(etiquetaSubTituloListado);

        JButton botonVolver = new JButton("Volver al Dashboard");
        botonVolver.setFont(fuenteBoton);
        botonVolver.setForeground(paletaAzulBonito);
        botonVolver.setBackground(paletaBlanco);
        botonVolver.setBounds(40, 125, 250, 35);
        botonVolver.setBorderPainted(false);
        botonVolver.setFocusPainted(false);
        botonVolver.addActionListener(e -> cambiarVistaAListados(false));
        panelListadoInfo.add(botonVolver);

        int anchoBoton = 210;
        int espacio = 50;
        int xInicial = 57;
        int yFila1 = 450;
        int yFila2 = 690;

        panelListaPacientes = crearBotonCuadradoListado("Lista de pacientes", "/Imagenes/pacientList.png", xInicial, yFila1, anchoBoton);
        panelListaPacientes.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { abrirVentanaListado("Pacientes"); } });
        panelInformacion.add(panelListaPacientes);

        panelListaVacunas = crearBotonCuadradoListado("Lista de vacunas", "/Imagenes/vacinList.png", xInicial + anchoBoton + espacio, yFila1, anchoBoton);
        panelListaVacunas.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { abrirVentanaListado("Vacunas"); } });
        panelInformacion.add(panelListaVacunas);

        panelListaEnfermedades = crearBotonCuadradoListado("Lista Enfermedades", "/Imagenes/thermometer.png", xInicial + (anchoBoton + espacio)*2, yFila1, anchoBoton);
        panelListaEnfermedades.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { abrirVentanaListado("Enfermedades"); } });
        panelInformacion.add(panelListaEnfermedades);

        panelListaAdministradores = crearBotonCuadradoListado("Lista de admins", "/Imagenes/adminList.png", xInicial, yFila2, anchoBoton);
        panelListaAdministradores.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { abrirVentanaListado("Administradores"); } });
        panelInformacion.add(panelListaAdministradores);

        panelListaDoctores = crearBotonCuadradoListado("Lista de doctores", "/Imagenes/doctorList.png", xInicial + anchoBoton + espacio, yFila2, anchoBoton);
        panelListaDoctores.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { abrirVentanaListado("Doctores"); } });
        panelInformacion.add(panelListaDoctores);
    }

    // =========================================================================================
    //                                  GENERADORES DE COMPONENTES
    // =========================================================================================

    /*
       Función: cargarIcono
       Argumentos: (String) ruta: Ruta relativa del recurso.
                   (int) ancho: Ancho deseado.
                   (int) alto: Alto deseado.
       Objetivo: Cargar una imagen desde recursos, escalarla y devolverla como ImageIcon.
       Retorno: (ImageIcon): El icono listo para usar o uno vacío si falla.
    */
    private ImageIcon cargarIcono(String ruta, int ancho, int alto) {
        java.net.URL urlRecurso = getClass().getResource(ruta);
        if (urlRecurso == null) {
            urlRecurso = getClass().getResource("/Recursos" + ruta);
        }
        if (urlRecurso != null) {
            ImageIcon iconoBase = new ImageIcon(urlRecurso);
            if (iconoBase.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                return new ImageIcon(iconoBase.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
            }
        }
        return new ImageIcon();
    }

    /*
       Función: crearBotonEstiloMenu
       Argumentos: (String) texto: Texto del botón.
                   (int) posicionY: Coordenada Y.
       Objetivo: Generar un JButton con el estilo visual del menú lateral.
       Retorno: (JButton): El botón configurado.
    */
    private JButton crearBotonEstiloMenu(String texto, int posicionY) {
        JButton botonEstilizado = new JButton(texto);
        botonEstilizado.setForeground(paletaBlanco);
        botonEstilizado.setFont(fuenteBoton);
        botonEstilizado.setBounds(0, posicionY, 240, 47);
        botonEstilizado.setBackground(paletaAzulBonito);
        botonEstilizado.setBorderPainted(false);
        botonEstilizado.setFocusPainted(false);
        botonEstilizado.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!botonEstilizado.getBackground().equals(paletaRojo)) botonEstilizado.setBackground(paletaAzulOscuro);
            }
            public void mouseExited(MouseEvent e) {
                if (!botonEstilizado.getBackground().equals(paletaRojo) && !botonEstilizado.getBackground().equals(new Color(180, 0, 0))) botonEstilizado.setBackground(paletaAzulBonito);
            }
        });
        return botonEstilizado;
    }

    /*
       Función: crearPanelRedondeado
       Argumentos: (Color) colorFondo: Color de relleno.
                   (int) radio: Radio de curvatura de las esquinas.
       Objetivo: Generar un JPanel personalizado con bordes redondeados.
       Retorno: (JPanel): El panel personalizado.
    */
    private JPanel crearPanelRedondeado(Color colorFondo, int radio) {
        return new JPanel() {
            private static final long serialVersionUID = 1L;
            { setOpaque(false); }
            @Override
            protected void paintComponent(Graphics grafico) {
                Graphics2D graficos2D = (Graphics2D) grafico;
                graficos2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graficos2D.setColor(colorFondo);
                graficos2D.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                graficos2D.setColor(Color.BLACK);
                graficos2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                super.paintComponent(grafico);
            }
        };
    }

    /*
       Función: crearTarjetaEstadistica
       Argumentos: (String) titulo: Texto superior.
                   (int) cantidad: Valor numérico a mostrar.
                   (String) rutaIcono: Icono decorativo.
                   (int) x: Posición X.
                   (int) y: Posición Y.
       Objetivo: Generar un panel pequeño tipo "Tarjeta" con estadísticas rápidas.
       Retorno: (JPanel): La tarjeta construida.
    */
    private JPanel crearTarjetaEstadistica(String titulo, int cantidad, String rutaIcono, int x, int y) {
        JPanel tarjeta = crearPanelRedondeado(paletaBlanco, 20);
        tarjeta.setBounds(x, y, 300, 130);
        tarjeta.setLayout(null);

        JLabel etiquetaIcono = new JLabel(cargarIcono(rutaIcono, 108, 108));
        etiquetaIcono.setBounds(171, 11, 108, 108);
        tarjeta.add(etiquetaIcono);

        JLabel etiquetaTitulo = new JLabel(titulo);
        etiquetaTitulo.setFont(fuenteUsoNormal);
        etiquetaTitulo.setBounds(10, 105, 180, 14);
        tarjeta.add(etiquetaTitulo);

        JLabel etiquetaCantidad = new JLabel(String.valueOf(cantidad));
        etiquetaCantidad.setFont(fuenteNumeroIndicativo);
        etiquetaCantidad.setBounds(10, 11, 80, 50);
        tarjeta.add(etiquetaCantidad);
        return tarjeta;
    }

    /*
       Función: crearBotonCuadradoListado
       Argumentos: (String) texto: Texto inferior.
                   (String) rutaIcono: Icono central.
                   (int) x: Posición X.
                   (int) y: Posición Y.
                   (int) width: Ancho y alto (es cuadrado).
       Objetivo: Generar los botones grandes para la sección de listados.
       Retorno: (JPanel): El panel interactivo.
    */
    private JPanel crearBotonCuadradoListado(String texto, String rutaIcono, int x, int y, int width) {
        JPanel panelBoton = crearPanelRedondeado(paletaBlanco, 30);
        panelBoton.setBounds(x, y, width, width);
        panelBoton.setVisible(false);
        panelBoton.setLayout(null);

        JLabel etiquetaIcono = new JLabel(cargarIcono(rutaIcono, 90, 90));
        int posicionIconoX = (width - 90) / 2;
        etiquetaIcono.setBounds(posicionIconoX, 30, 90, 90);
        panelBoton.add(etiquetaIcono);

        JLabel etiquetaTexto = new JLabel(texto);
        etiquetaTexto.setFont(fuenteUsoNormal);
        etiquetaTexto.setForeground(paletaAzulBonito);
        etiquetaTexto.setBounds(10, width - 40, width - 20, 20);
        etiquetaTexto.setHorizontalAlignment(SwingConstants.CENTER);
        panelBoton.add(etiquetaTexto);

        panelBoton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { panelBoton.setBackground(new Color(220, 235, 255)); panelBoton.repaint(); }
            public void mouseExited(MouseEvent e) { panelBoton.setBackground(paletaBlanco); panelBoton.repaint(); }
        });
        return panelBoton;
    }

    // =========================================================================================
    //                                  LÓGICA DE GRÁFICOS
    // =========================================================================================

    /*
       Función: configurarGraficoBarrasEnfermedades
       Argumentos: Ninguno.
       Objetivo: Calcular la frecuencia de enfermedades en consultas y generar un gráfico de barras.
       Retorno: (void): Agrega el ChartPanel al panel contenedor.
    */
    private void configurarGraficoBarrasEnfermedades() {
        DefaultCategoryDataset conjuntoDatos = new DefaultCategoryDataset();
        ArrayList<Enfermedad> listaEnfermedades = clinicaInstancia.getCatalogoEnfermedades();
        ArrayList<Consulta> listaConsultas = clinicaInstancia.getConsultas();

        if (listaEnfermedades != null && listaConsultas != null) {
            for (Enfermedad enfermedadIterada : listaEnfermedades) {
                int contador = 0;
                for (Consulta consultaIterada : listaConsultas) {
                    if (consultaIterada.getEnfermedad() != null && consultaIterada.getEnfermedad().getIdEnfermedad() == enfermedadIterada.getIdEnfermedad()) {
                        contador++;
                    }
                }
                if (contador > 0) {
                    conjuntoDatos.addValue(contador, "Pacientes", enfermedadIterada.getNombre());
                }
            }
        }

        JFreeChart graficoBarras = ChartFactory.createBarChart("Pacientes por Enfermedad", "Enfermedad", "Cant", conjuntoDatos, PlotOrientation.VERTICAL, false, true, false);
        aplicarEstiloGrafico(graficoBarras);

        ChartPanel panelDelGrafico = new ChartPanel(graficoBarras);
        panelDelGrafico.setBounds(30, 5, 954, 308);
        panelGraficoEnfermedades.add(panelDelGrafico);
    }

    /*
       Función: configurarGraficoInventarioVacunas
       Argumentos: Ninguno.
       Objetivo: Contar el stock de vacunas agrupado por nombre y generar gráfico. También verifica stock bajo.
       Retorno: (void): Agrega el ChartPanel y lanza alertas si es necesario.
    */
    private void configurarGraficoInventarioVacunas() {
        DefaultCategoryDataset conjuntoDatos = new DefaultCategoryDataset();
        ArrayList<Vacuna> listaVacunas = clinicaInstancia.getVacunas();
        Map<String, Integer> mapaConteo = new HashMap<>();
        ArrayList<String> vacunasConPocoStock = new ArrayList<>();
        int limiteStockBajo = 5; 

        // Lógica de conteo (Divide y Vencerás: Agrupación)
        if (listaVacunas != null) {
            for (Vacuna vacunaActual : listaVacunas) {
                String nombreVacuna = vacunaActual.getNombre();
                if (mapaConteo.containsKey(nombreVacuna)) {
                    mapaConteo.put(nombreVacuna, mapaConteo.get(nombreVacuna) + 1);
                } else {
                    mapaConteo.put(nombreVacuna, 1);
                }
            }
        }

        // Llenado del dataset y verificación de alertas
        for (Map.Entry<String, Integer> entrada : mapaConteo.entrySet()) {
            conjuntoDatos.setValue(entrada.getValue(), "Stock", entrada.getKey());
            if (entrada.getValue() < limiteStockBajo) {
                vacunasConPocoStock.add(entrada.getKey());
            }
        }

        JFreeChart graficoInventario = ChartFactory.createBarChart("Inventario de Vacunas", "Vacuna", "Dosis Disponibles", conjuntoDatos, PlotOrientation.HORIZONTAL, false, true, false);
        aplicarEstiloGrafico(graficoInventario);
        
        // Colorear barras de alerta si fuera posible (avanzado) o simplemente mostrar el gráfico
        ChartPanel panelDelGrafico = new ChartPanel(graficoInventario);
        panelDelGrafico.setBounds(30, 5, 954, 308);
        panelGraficoVacunas.add(panelDelGrafico);

        // Disparar alerta visual si hay escasez
        if (!vacunasConPocoStock.isEmpty()) {
            mostrarAlertaStockBajo(vacunasConPocoStock);
        }
    }

    /*
       Función: mostrarAlertaStockBajo
       Argumentos: (ArrayList<String>) nombresVacunas: Lista de nombres de vacunas escasas.
       Objetivo: Mostrar un mensaje de advertencia al usuario administrativo.
       Retorno: (void): Muestra JOptionPane.
    */
    private void mostrarAlertaStockBajo(ArrayList<String> nombresVacunas) {
        StringBuilder mensaje = new StringBuilder("¡ATENCIÓN! Stock Crítico detectado:\n");
        for (String nombre : nombresVacunas) {
            mensaje.append("- ").append(nombre).append("\n");
        }
        mensaje.append("Se recomienda reabastecer inventario.");
        
        // Usamos un hilo separado visual suave o un dialogo directo
        JLabel etiquetaAlerta = new JLabel("STOCK BAJO");
        etiquetaAlerta.setForeground(paletaRojo);
        etiquetaAlerta.setFont(fuenteTituloGrafico);
        etiquetaAlerta.setBounds(800, 10, 150, 30);
        panelGraficoVacunas.add(etiquetaAlerta); // Indicador visual en el gráfico
        panelGraficoVacunas.repaint();
    }

    /*
       Función: aplicarEstiloGrafico
       Argumentos: (JFreeChart) grafico: El objeto gráfico a estilizar.
       Objetivo: Unificar la estética de los gráficos con el tema de la aplicación.
       Retorno: (void): Modifica propiedades del objeto grafico.
    */
    private void aplicarEstiloGrafico(JFreeChart grafico) {
        grafico.setBackgroundPaint(paletaBlanco);
        grafico.getTitle().setFont(fuenteTituloGrafico);
        CategoryPlot trama = (CategoryPlot) grafico.getPlot();
        trama.setBackgroundPaint(paletaBlanco);
        trama.setOutlineVisible(false);
        ((BarRenderer) trama.getRenderer()).setSeriesPaint(0, paletaAzulBonito);
        ((BarRenderer) trama.getRenderer()).setBarPainter(new StandardBarPainter());
    }

    // =========================================================================================
    //                                  LÓGICA AUXILIAR Y NAVEGACIÓN
    // =========================================================================================

    /*
       Función: contarCitasDeHoy
       Argumentos: Ninguno.
       Objetivo: Filtrar y contar las citas cuya fecha coincide con la actual.
       Retorno: (int): Cantidad de citas.
    */
    private int contarCitasDeHoy() {
        LocalDate hoy = LocalDate.now();
        int contador = 0;
        if (clinicaInstancia.getCitas() != null) {
            for (Cita citaIterada : clinicaInstancia.getCitas()) {
                if (citaIterada.getFecha().isEqual(hoy)) contador++;
            }
        }
        return contador;
    }

    /*
       Función: obtenerNombreUsuario
       Argumentos: (int) modo: Rol.
                   (String) id: ID.
       Objetivo: Buscar el nombre real del usuario en la base de datos (Clinica).
       Retorno: (String): El nombre encontrado o "Usuario" por defecto.
    */
    private String obtenerNombreUsuario(int modo, String id) {
        String nombre = "Usuario";
        if (modo == 0) {
            Administrativo admin = clinicaInstancia.getAdministrativoPorUsuario(id);
            if (admin != null) nombre = admin.getNombre();
        } else {
            Doctor doctor = clinicaInstancia.getDoctorPorUsuario(id);
            if (doctor != null) nombre = doctor.getNombre();
        }
        return nombre;
    }

    /*
       Función: accionBotonPrimario
       Argumentos: (int) modo: Rol.
                   (String) id: ID.
       Objetivo: Determinar qué ventana abrir con el primer botón del menú según el rol.
       Retorno: (void): Abre la ventana correspondiente.
    */
    private void accionBotonPrimario(int modo, String id) {
        if (modo == 0) {
            abrirVentanaRegistroAdmin(modo, id);
        } else {
            Doctor doctor = clinicaInstancia.getDoctorPorUsuario(id);
            if (doctor != null) abrirVentanaListarCitas(doctor);
        }
    }

    /*
       Función: cambiarVistaAListados
       Argumentos: (boolean) mostrarListados: True para ver listados, False para ver dashboard.
       Objetivo: Alternar la visibilidad entre los paneles de gráficos y los de botones cuadrados.
       Retorno: (void): Cambia setVisible de los componentes.
    */
    private void cambiarVistaAListados(boolean mostrarListados) {
        // Ocultar Dashboard
        panelCantidadEnfermedades.setVisible(!mostrarListados);
        panelCantidadVacunas.setVisible(!mostrarListados);
        panelCantidadCitasHoy.setVisible(!mostrarListados);
        panelGraficoEnfermedades.setVisible(!mostrarListados);
        if (panelGraficoVacunas != null) panelGraficoVacunas.setVisible(!mostrarListados);

        // Mostrar Listados
        panelListadoInfo.setVisible(mostrarListados);
        panelListaAdministradores.setVisible(mostrarListados);
        panelListaVacunas.setVisible(mostrarListados);
        panelListaDoctores.setVisible(mostrarListados);
        panelListaPacientes.setVisible(mostrarListados);
        if (panelListaEnfermedades != null) panelListaEnfermedades.setVisible(mostrarListados);
        etiquetaImagenDoctorGrande.setVisible(mostrarListados);
    }

    // =========================================================================================
    //                                  MANEJO DE EXCEPCIONES Y SEGURIDAD
    // =========================================================================================

    /*
       Función: ejecutarAplicacionSegura
       Argumentos: Ninguno.
       Objetivo: Iniciar la ventana capturando errores fatales de inicio.
       Retorno: (void): Instancia la ventana o muestra error.
    */
    private static void ejecutarAplicacionSegura() {
        try {
            Principal ventanaPrincipal = new Principal(0, "admin");
            ventanaPrincipal.setVisible(true);
        } catch (Exception excepcionFatal) {
            excepcionFatal.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error crítico al iniciar.", "Error Fatal", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
       Función: calcularDiferenciaMinutos
       Argumentos: (LocalTime) inicio: Hora base.
                   (String) finStr: Hora destino en String.
       Objetivo: Calcular minutos de diferencia de forma segura ante formatos inválidos.
       Retorno: (long): Minutos de diferencia o -1 si falla.
    */
    private long calcularDiferenciaMinutos(LocalTime inicio, String finStr) {
        try {
            LocalTime fin = LocalTime.parse(finStr);
            return ChronoUnit.MINUTES.between(inicio, fin);
        } catch (Exception e) {
            return -1;
        }
    }

    /*
       Función: cargarImagenUsuarioSegura
       Argumentos: (int) modo: Rol.
                   (String) idUsuario: ID.
       Objetivo: Buscar la foto de perfil en el sistema de archivos manejando errores de IO.
       Retorno: (ImageIcon): La foto encontrada o una por defecto.
    */
    private ImageIcon cargarImagenUsuarioSegura(int modo, String idUsuario) {
        try {
            String baseNombre = "";
            File carpeta = null;
            if (modo == 0) {
                Administrativo admin = clinicaInstancia.getAdministrativoPorUsuario(idUsuario);
                if (admin != null) {
                    baseNombre = String.format("A-%03d", admin.getIdAdmin());
                    carpeta = new File("FotosAdmin");
                }
            } else {
                Doctor doc = clinicaInstancia.getDoctorPorUsuario(idUsuario);
                if (doc != null) {
                    baseNombre = String.format("doctor_%03d", doc.getId());
                    carpeta = new File("FotosDoctores");
                }
            }

            if (carpeta != null && carpeta.exists() && carpeta.isDirectory()) {
                File[] archivosEncontrados = carpeta.listFiles();
                if (archivosEncontrados != null) {
                    for (File archivoIterado : archivosEncontrados) {
                        String nombreArchivo = archivoIterado.getName();
                        if (nombreArchivo.startsWith(baseNombre) && (nombreArchivo.toLowerCase().endsWith(".png") || nombreArchivo.toLowerCase().endsWith(".jpg"))) {
                            return new ImageIcon(new ImageIcon(archivoIterado.getPath()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Fallo silencioso, se retorna imagen por defecto
        }
        return cargarIcono("/Imagenes/useResi.png", 80, 80);
    }

    // Funciones envoltorio para abrir ventanas (Try-Catch encapsulado)
    private void abrirVentanaRegistroAdmin(int m, String u) { try { new RegAdmin(m, u).setVisible(true); } catch(Exception e) { e.printStackTrace(); } }
    private void abrirVentanaRegistroDoctor(int m, String u) { try { new RegDoctor(m, u).setVisible(true); } catch(Exception e) { e.printStackTrace(); } }
    private void abrirVentanaRegistroCita() { try { new RegCita().setVisible(true); } catch(Exception e) { e.printStackTrace(); } }
    private void abrirVentanaReportes() { try { if (clinicaInstancia.getConsultas() == null) clinicaInstancia.setConsultas(new ArrayList<>()); new Reportes().setVisible(true); } catch(Throwable e) { JOptionPane.showMessageDialog(null, "No hay suficientes datos.", "Info", JOptionPane.INFORMATION_MESSAGE); } }
    private void abrirVentanaListarCitas(Doctor d) { try { new ListarCitas(d).setVisible(true); } catch(Exception e) { e.printStackTrace(); } }
    private void abrirVentanaListado(String t) {
        try {
            switch (t) {
                case "Doctores": new ListarDoctores().setVisible(true); break;
                case "Administradores": new ListarAdministradores().setVisible(true); break;
                case "Vacunas": new ListarVacunas().setVisible(true); break;
                case "Pacientes": new ListarPacientes().setVisible(true); break;
                case "Enfermedades": new ListarEnfermedades().setVisible(true); break;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    // --- NUEVO MÉTODO PARA ABRIR GESTIÓN DE RECURSOS ---
    private void abrirVentanaGestionRecursos() {
        try {
            new GestionRecursos().setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirmarCierreAplicacion() {
        if(JOptionPane.showConfirmDialog(null, "¿Salir de la aplicación?", "Cierre", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clinicaInstancia.guardarDatosLocal();
            dispose();
            System.exit(0);
        }
    }

    private void cerrarSesion() {
        if (JOptionPane.showConfirmDialog(null, "¿Cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clinicaInstancia.guardarDatosLocal();
            dispose();
            try { new Login().setVisible(true); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}