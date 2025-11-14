package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class Vacuna implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String nombre;
	private LocalDate fechaAplicacion;
	private int numDosis;

	public Vacuna() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(LocalDate localDate) {
		this.fechaAplicacion = localDate;
	}

	public int getNumDosis() {
		return numDosis;
	}

	public void setNumDosis(int numDosis) {
		this.numDosis = numDosis;
	}
}