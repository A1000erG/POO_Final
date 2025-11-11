package logico;

import java.time.LocalDate;

public class Cita {

	private int id;
	private LocalDate fecha;
	private String hora;
	private String estado;

	public Cita(int id, LocalDate fecha, String hora, String estado) {
		this.id = id;
		this.fecha = fecha;
		this.hora = hora;
		this.estado = estado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}