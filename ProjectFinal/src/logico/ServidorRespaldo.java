package logico;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorRespaldo extends Thread {

	private ServerSocket socketServidor;
	private boolean running;
	private int puerto;

	/*
	 * Función: ServidorRespaldo (Constructor) Argumentos: (int) puerto: El puerto
	 * TCP donde escuchar (ej. 7001). Objetivo: Configurar el servidor de respaldo.
	 * Retorno: Ninguno.
	 */
	public ServidorRespaldo(int puerto) {
		this.puerto = puerto;
		this.running = true;
	}

	/*
	 * Función: run (De Thread) Argumentos: Ninguno. Objetivo: Bucle principal que
	 * acepta conexiones entrantes de la clínica. Retorno: (void).
	 */
	@Override
	public void run() {
		try {
			socketServidor = new ServerSocket(puerto);
			while (running) {
				Socket socketCliente = socketServidor.accept();
				recibirDatos(socketCliente);
			}
		} catch (IOException excepcionIO) {
			// Manejo interno silencioso o log
		}
	}

	/*
	 * Función: recibirDatos Argumentos: (Socket) socketCliente: La conexión con la
	 * aplicación Cliente. Objetivo: Leer el archivo binario y el reporte de texto
	 * enviados por la red y guardarlos en disco. Retorno: (void).
	 */
	private void recibirDatos(Socket socketCliente) {
		DataInputStream dataInput = null;
		FileOutputStream fileOutBin = null;
		FileOutputStream fileOutTxt = null;

		try {
			dataInput = new DataInputStream(socketCliente.getInputStream());

			// Paso 1: Leer tamaño y datos del binario
			long tamanoArchivo = dataInput.readLong();
			byte[] buffer = new byte[4096];
			fileOutBin = new FileOutputStream("empresa_respaldo.dat");

			int leidos;
			long totalLeido = 0;
			while (totalLeido < tamanoArchivo && (leidos = dataInput.read(buffer, 0,
					(int) Math.min(buffer.length, tamanoArchivo - totalLeido))) != -1) {
				fileOutBin.write(buffer, 0, leidos);
				totalLeido += leidos;
			}

			// Paso 2: Leer reporte de texto (Puntos extra)
			String reporteTexto = dataInput.readUTF();
			fileOutTxt = new FileOutputStream("reporte_enfermedades.txt");
			fileOutTxt.write(reporteTexto.getBytes());

		} catch (IOException e) {
			// Error en transmisión
		} finally {
			try {
				if (fileOutBin != null)
					fileOutBin.close();
				if (fileOutTxt != null)
					fileOutTxt.close();
				if (dataInput != null)
					dataInput.close();
				socketCliente.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ServerSocket getSocketServidor() {
		return socketServidor;
	}

	public void setSocketServidor(ServerSocket socketServidor) {
		this.socketServidor = socketServidor;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
}