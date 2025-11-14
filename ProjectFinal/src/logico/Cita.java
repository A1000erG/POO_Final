package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class Cita implements Serializable {

	private static final long serialVersionUID = 8L;
	private int idCita;
	private LocalDate fecha;
	private String hora;
	private String estado; // Ej: "Pendiente", "Realizada", "Cancelada"
	private Paciente paciente;
	private Doctor doctor;

	public Cita() {
		this.idCita = 0;
		this.fecha = null;
		this.hora = null;
		this.estado = null;
		this.paciente = null;
		this.doctor = null;
	}

	public int getIdCita() {
		return idCita;
	}

	public void setIdCita(int idCita) {
		this.idCita = idCita;
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

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
}