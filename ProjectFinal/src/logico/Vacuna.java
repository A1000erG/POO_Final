package logico;

import java.time.LocalDate;

public class Vacuna {

	private int id;
	private String nombre;
	private LocalDate fechaAplicacion;
	private int numDosis;

	public Vacuna(int id, String nombre, LocalDate fechaAplicacion, int numDosis) {
		this.id = id;
		this.nombre = nombre;
		this.fechaAplicacion = fechaAplicacion;
		this.numDosis = numDosis;
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

	public void setFechaAplicacion(LocalDate fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public int getNumDosis() {
		return numDosis;
	}

	public void setNumDosis(int numDosis) {
		this.numDosis = numDosis;
	}
}