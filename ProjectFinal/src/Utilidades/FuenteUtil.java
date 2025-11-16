package Utilidades;

import java.awt.Font;

public class FuenteUtil {

    public static Font cargarFuente(String ruta, float tamanio) {
        try {
            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    FuenteUtil.class.getResourceAsStream(ruta)
            );
            return font.deriveFont(Font.PLAIN, tamanio);

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int)tamanio);
        }
    }

    public static Font cargarFuenteBold(String ruta, float tamanio) {
        try {
            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    FuenteUtil.class.getResourceAsStream(ruta)
            );
            return font.deriveFont(Font.BOLD, tamanio);

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.BOLD, (int)tamanio);
        }
    }
}
