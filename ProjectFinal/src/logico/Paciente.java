package logico;

import java.time.LocalDate;

public class Paciente extends Persona {

	private int id;

	public Paciente(int id, String nombre, String sexo, LocalDate fechaNacimiento, String telefono, String cedula) {
		super(nombre, sexo, fechaNacimiento, telefono, cedula);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
