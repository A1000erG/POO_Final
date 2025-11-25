package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class Consulta implements Serializable {

	private static final long serialVersionUID = 9L;
	private int idConsulta;
	private Cita citaAsociada;
	private Doctor doctor;
	private String sintomas;

	// CAMBIO: Ahora es objeto Diagnostico, NO String
	private Diagnostico diagnostico;

	private boolean agregarAlHistorial;
	private boolean transferida;
	private LocalDate fecha;

	public Consulta() {
		this.idConsulta = 0;
		this.citaAsociada = null;
		this.doctor = null;
		this.sintomas = null;
		this.diagnostico = null;
		this.agregarAlHistorial = true;
		this.transferida = false;
		this.fecha = LocalDate.now();
	}

	public int getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(int idConsulta) {
		this.idConsulta = idConsulta;
	}

	public Cita getCitaAsociada() {
		return citaAsociada;
	}

	public void setCitaAsociada(Cita citaAsociada) {
		this.citaAsociada = citaAsociada;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getSintomas() {
		return sintomas;
	}

	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}

	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
	}

	public boolean isAgregarAlHistorial() {
		return agregarAlHistorial;
	}

	public void setAgregarAlHistorial(boolean agregarAlHistorial) {
		this.agregarAlHistorial = agregarAlHistorial;
	}

	public boolean isTransferida() {
		return transferida;
	}

	public void setTransferida(boolean transferida) {
		this.transferida = transferida;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}