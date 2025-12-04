package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class Cita implements Serializable {

	private static final long serialVersionUID = 8L;
	private int idCita;
	private LocalDate fecha;
	private String hora;
	private String estado; // "Pendiente", "Realizada", "Cancelada"
	private Solicitante solicitante;
	private Paciente paciente;
	private Doctor doctor;

	public Cita() {
		this.idCita = 0;
		this.fecha = null;
		this.hora = null;
		this.estado = "Pendiente";
		this.paciente = null;
		this.doctor = null;
	}

	/*
	 * Funci�n: Cita (Constructor) Argumentos: (LocalDate) fecha: D�a de la
	 * cita. (String) hora: Hora en texto. (Paciente) paciente: Quien asiste.
	 * (Doctor) doctor: Quien atiende. Objetivo: Crear una reserva de cita. Retorno:
	 * (Ninguno).
	 */
	public Cita(LocalDate fecha, String hora, Paciente paciente, Doctor doctor) {
		this.idCita = 0;
		this.fecha = fecha;
		this.hora = hora;
		this.estado = "Pendiente";
		this.paciente = paciente;
		this.doctor = doctor;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Solicitante getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Solicitante solicitante) {
		this.solicitante = solicitante;
	}
}