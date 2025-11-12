package logico;

import java.io.Serializable;
import java.util.Date;

public class Consulta implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idConsulta;
	private Cita citaAsociada;
	private Doctor doctor;
	private String sintomas;
	private String diagnostico;
	private boolean agregarAlHistorial;
	private boolean transferida;

	private Date fecha;

	public Consulta() {
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

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}