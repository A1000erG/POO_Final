package logico;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	private String cedula;
	private String nombre;
	private char sexo;
	private LocalDate fechaNacimiento;
	private String telefono;

	public Persona() {
		this.cedula = null;
		this.nombre = null;
		this.sexo = ' ';
		this.fechaNacimiento = null;
		this.telefono = null;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public char getSexo() {
		return sexo;
	}

	public void setSexo(char sexo) {
		this.sexo = sexo;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}