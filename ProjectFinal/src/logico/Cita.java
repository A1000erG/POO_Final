package logico;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase Cita (Modelo). Implementa Serializable.
 */
public class Cita implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idCita;
	private Date fecha;
	private String hora;
	private String estado;

	private Paciente paciente;
	private Doctor doctor;

	public Cita() {
	}

	public int getIdCita() {
		return idCita;
	}

	public void setIdCita(int idCita) {
		this.idCita = idCita;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
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