package logico;

import java.time.LocalDate;

public class Solicitante extends Persona {

	private static final long serialVersionUID = 5L;

	public Solicitante() {
		super();
	}

	/*
	 * Función: Solicitante (Constructor con parámetros) Argumentos: (String)
	 * cedula: Cédula de la persona. (String) nombre: Nombre de la persona. (char)
	 * sexo: Sexo (ej. 'M' o 'F'). (LocalDate) fechaNacimiento: Fecha de nacimiento.
	 * (String) telefono: Número de teléfono. Objetivo: Inicializar un objeto
	 * Solicitante con valores específicos. Retorno: Ninguno (es un constructor).
	 */
	public Solicitante(String cedula, String nombre, char sexo, LocalDate fechaNacimiento, String telefono) {
		super(cedula, nombre, sexo, fechaNacimiento, telefono);
	}
}