package logico;

import java.time.LocalDate;

public class Solicitante extends Persona {

	private static final long serialVersionUID = 5L;

	public Solicitante() {
		super();
	}

	public Solicitante(String cedula, String nombre, char sexo, LocalDate fechaNacimiento, String telefono) {
		super(cedula, nombre, sexo, fechaNacimiento, telefono);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}