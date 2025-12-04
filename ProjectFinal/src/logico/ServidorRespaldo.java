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

	public ServidorRespaldo(int puerto) {
		this.puerto = puerto;
		this.running = true;
	}

	@Override
	public void run() {
		try {
			socketServidor = new ServerSocket(puerto);
			while (running) {
				Socket socketCliente = socketServidor.accept();
				recibirDatos(socketCliente);
			}
		} catch (IOException excepcionIO) {
		}
	}

	private void recibirDatos(Socket socketCliente) {
		DataInputStream dataInput = null;
		FileOutputStream fileOutBin = null;
		FileOutputStream fileOutTxt = null;

		try {
			dataInput = new DataInputStream(socketCliente.getInputStream());

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

			String reporteTexto = dataInput.readUTF();
			fileOutTxt = new FileOutputStream("reporte_enfermedades.txt");
			fileOutTxt.write(reporteTexto.getBytes());

		} catch (IOException e) {
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