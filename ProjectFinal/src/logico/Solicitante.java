package logico;

import java.time.LocalDate;

public class Solicitante extends Persona {

	public Solicitante(String nombre, String sexo, LocalDate fechaNacimiento, String telefono, String cedula) {
		super(nombre, sexo, fechaNacimiento, telefono, cedula);
	}
}
