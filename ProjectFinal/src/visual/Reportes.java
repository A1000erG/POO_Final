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
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import Utilidades.FuenteUtil;
import logico.Cita;
import logico.Clinica;
import logico.Consulta;
import logico.Diagnostico;

public class Reportes extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Color colorHeader = new Color(4, 111, 67);
    private Color colorAzulData = new Color(21, 129, 191);

    public Reportes() {
        setTitle("Estadísticas de la Clínica");
        setModal(true);
        setBounds(100, 100, 1200, 750);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(colorHeader);
        panelSuperior.setLayout(null);
        panelSuperior.setPreferredSize(new Dimension(0, 100));
        contentPane.add(panelSuperior, BorderLayout.NORTH);
        
        ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Imagenes/logoBlanco.png"));
        JLabel lblLogo = new JLabel(new ImageIcon(iconLogo.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        lblLogo.setBounds(30, 20, 60, 60);
        panelSuperior.add(lblLogo);
        
        JLabel lblTitulo = new JLabel("ESTADÍSTICAS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Black.ttf", 30f));
        lblTitulo.setBounds(110, 25, 600, 50);
        panelSuperior.add(lblTitulo);
        
        crearBotonVolver(panelSuperior);

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelCentral.setLayout(new BorderLayout(0, 0));
        contentPane.add(panelCentral, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(FuenteUtil.cargarFuenteBold("/Fuentes/Roboto-Bold.ttf", 14f));
        panelCentral.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab("Enfermedades Frecuentes", crearPanelGrafico(crearGraficoEnfermedades()));
        tabbedPane.addTab("Estado de Citas", crearPanelGrafico(crearGraficoEstadoCitas()));
        tabbedPane.addTab("Rendimiento Doctores", crearPanelGrafico(crearGraficoActividadDoctores()));
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

    private JPanel crearPanelGrafico(JFreeChart chart) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        if(chart != null) {
            ChartPanel cp = new ChartPanel(chart);
            cp.setBackground(Color.WHITE);
            p.add(cp, BorderLayout.CENTER);
        } else {
            JLabel lbl = new JLabel("No hay datos disponibles", JLabel.CENTER);
            lbl.setFont(new Font("Arial", Font.PLAIN, 18));
            p.add(lbl, BorderLayout.CENTER);
        }
        return p;
    }

    private JFreeChart crearGraficoEnfermedades() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> conteo = new HashMap<>();
        ArrayList<Consulta> consultas = Clinica.getInstance().getConsultas();
        
        if (consultas != null) {
            for (Consulta cons : consultas) {
                if (cons.getDiagnosticos() != null) {
                    for (Diagnostico diag : cons.getDiagnosticos()) {
                        String nombre = diag.getNombre();
                        if (nombre != null) conteo.put(nombre, conteo.getOrDefault(nombre, 0) + 1);
                    }
                }
            }
        }

        if (!conteo.isEmpty()) {
            for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
                dataset.addValue(entry.getValue(), "Diagnósticos", entry.getKey());
            }
        }

        JFreeChart chart = ChartFactory.createBarChart("Top Enfermedades Diagnosticadas", "", "Cantidad", dataset, PlotOrientation.VERTICAL, false, true, false);
        estilizarGrafico(chart);
        return chart;
    }

    private JFreeChart crearGraficoEstadoCitas() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        int pen=0, rea=0, can=0;
        ArrayList<Cita> citas = Clinica.getInstance().getCitas();
        boolean hayDatos = false;
        
        if (citas != null) {
            for (Cita c : citas) {
                if ("Pendiente".equalsIgnoreCase(c.getEstado())) pen++;
                else if ("Realizada".equalsIgnoreCase(c.getEstado())) rea++;
                else if ("Cancelada".equalsIgnoreCase(c.getEstado()) || "Rechazada".equalsIgnoreCase(c.getEstado())) can++;
            }
            if (pen > 0 || rea > 0 || can > 0) hayDatos = true;
        }
        
        if (hayDatos) {
            dataset.setValue("Pendientes", pen);
            dataset.setValue("Realizadas", rea);
            dataset.setValue("Canceladas/Rechazadas", can);
        }

        JFreeChart chart = ChartFactory.createRingChart("Distribución de Citas", dataset, true, true, false);
        estilizarGrafico(chart);
        return chart;
    }
    
    private JFreeChart crearGraficoActividadDoctores() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> conteo = new HashMap<>();
        ArrayList<Cita> citas = Clinica.getInstance().getCitas();
        
        if (citas != null) {
            for (Cita c : citas) {
                if("Realizada".equalsIgnoreCase(c.getEstado()) && c.getDoctor() != null) {
                    String doc = c.getDoctor().getNombre();
                    conteo.put(doc, conteo.getOrDefault(doc, 0) + 1);
                }
            }
        }
        
        if (!conteo.isEmpty()) {
            for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
                dataset.addValue(entry.getValue(), "Consultas", entry.getKey());
            }
        }
        
        JFreeChart chart = ChartFactory.createBarChart("Consultas Atendidas por Doctor", "", "", dataset, PlotOrientation.HORIZONTAL, false, true, false);
        estilizarGrafico(chart);
        return chart;
    }
    
    private void estilizarGrafico(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 22));
        chart.setBorderVisible(false);
        
        if(chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlineVisible(false);
            plot.setRangeGridlinePaint(new Color(220, 220, 220));
            
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setAutoRangeIncludesZero(true);
            
            BarRenderer r = (BarRenderer) plot.getRenderer();
            r.setSeriesPaint(0, colorAzulData);
            r.setBarPainter(new StandardBarPainter());
            r.setShadowVisible(false);
            
        } else if (chart.getPlot() instanceof PiePlot) {
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlineVisible(false);
            plot.setShadowPaint(null);
            
            plot.setNoDataMessage("No hay datos para mostrar");
            plot.setNoDataMessageFont(new Font("SansSerif", Font.PLAIN, 18));
            plot.setNoDataMessagePaint(Color.GRAY);
            
            plot.setSectionPaint("Pendientes", new Color(255, 193, 7));
            plot.setSectionPaint("Realizadas", new Color(76, 175, 80));
            plot.setSectionPaint("Canceladas/Rechazadas", new Color(244, 67, 54));
        }
    }
}