package visual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class PanelRedondeado extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the panel.
	 */
	private int radio;
	private Color bgColor;
	private int ancho;
	private int largo;
	
	public PanelRedondeado(int radio, Color bgColor, int ancho, int largo) {
		super();
		this.radio = radio;
		this.bgColor = bgColor;
		this.ancho = ancho;
		this.largo = largo;
		setOpaque(false);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Activar Antialiasing para bordes suaves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar fondo redondeado
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, ancho, largo, radio, radio);
	}

}
