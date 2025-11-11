package logico;

import java.time.LocalDate;

public class Consulta {

	private int id;
	private LocalDate fecha;
	private String sintomas;
	private String diagnostico;
	private int agregarAlHistorial;

	public Consulta(int id, LocalDate fecha, String sintomas, String diagnostico, int agregarAlHistorial) {
		this.id = id;
		this.fecha = fecha;
		this.sintomas = sintomas;
		this.diagnostico = diagnostico;
		this.agregarAlHistorial = agregarAlHistorial;
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

	public int getAgregarAlHistorial() {
		return agregarAlHistorial;
	}

	public void setAgregarAlHistorial(int agregarAlHistorial) {
		this.agregarAlHistorial = agregarAlHistorial;
	}
}
