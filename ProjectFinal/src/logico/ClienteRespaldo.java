package logico;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClienteRespaldo {

	private static final String HOST = "127.0.0.1";
	private static final int PUERTO = 7001;

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

			dataOutput.writeLong(ficheroBinario.length());

			byte[] buffer = new byte[4096];
			int leidos;
			while ((leidos = fileInput.read(buffer)) != -1) {
				dataOutput.write(buffer, 0, leidos);
			}
			dataOutput.flush();

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