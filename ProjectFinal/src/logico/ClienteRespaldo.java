package logico;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClienteRespaldo {

	private static final String HOST = "127.0.0.1";
	private static final int PUERTO = 7001;

	/*
	 * Función: enviarRespaldo Argumentos: (File) ficheroBinario: El archivo local
	 * clinica.dat a enviar. (String) reporteTexto: El resumen estadístico generado.
	 * Objetivo: Conectar al socket del servidor y transmitir los datos. Retorno:
	 * (boolean): true si el envío fue exitoso, false si falló.
	 */
	public static boolean enviarRespaldo(File ficheroBinario, String reporteTexto) {
		Socket socket = null;
		DataOutputStream dataOutput = null;
		FileInputStream fileInput = null;

		if (!ficheroBinario.exists()) {
			return false;
		}

		try {
			socket = new Socket(HOST, PUERTO);
			dataOutput = new DataOutputStream(socket.getOutputStream());
			fileInput = new FileInputStream(ficheroBinario);

			// 1. Enviar tamaño del archivo
			dataOutput.writeLong(ficheroBinario.length());

			// 2. Enviar bytes del archivo
			byte[] buffer = new byte[4096];
			int leidos;
			while ((leidos = fileInput.read(buffer)) != -1) {
				dataOutput.write(buffer, 0, leidos);
			}
			dataOutput.flush();

			// 3. Enviar reporte de texto
			dataOutput.writeUTF(reporteTexto);
			dataOutput.flush();

			return true;

		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (fileInput != null)
					fileInput.close();
				if (dataOutput != null)
					dataOutput.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getHost() {
		return HOST;
	}

	public static int getPuerto() {
		return PUERTO;
	}
}