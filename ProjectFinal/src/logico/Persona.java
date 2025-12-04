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

	private boolean activo;
	private String causaDeshabilitacion;

	public Persona() {
		this.cedula = null;
		this.nombre = null;
		this.sexo = ' ';
		this.fechaNacimiento = null;
		this.telefono = null;
		this.activo = true;
		this.causaDeshabilitacion = "";
	}

	public Persona(String cedula, String nombre, char sexo, LocalDate fechaNacimiento, String telefono) {
		this.cedula = cedula;
		this.nombre = nombre;
		this.sexo = sexo;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.activo = true;
		this.causaDeshabilitacion = "";
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

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getCausaDeshabilitacion() {
		return causaDeshabilitacion;
	}

	public void setCausaDeshabilitacion(String causaDeshabilitacion) {
		this.causaDeshabilitacion = causaDeshabilitacion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}