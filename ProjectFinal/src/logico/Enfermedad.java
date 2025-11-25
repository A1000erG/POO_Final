package logico;

import java.io.Serializable;

public class Enfermedad implements Serializable {

	private static final long serialVersionUID = 12L;
	private int idEnfermedad;
	private String nombre;
	private boolean esBajoVigilancia; // true = Controlada/Epidémica

	public Enfermedad() {
		this.idEnfermedad = 0;
		this.nombre = "";
		this.esBajoVigilancia = false;
	}

	/*
	 * Función: Enfermedad (Constructor) Argumentos: (int) id, (String) nombre,
	 * (boolean) vigilancia Objetivo: Crear una enfermedad para el catálogo.
	 */
	public Enfermedad(int idEnfermedad, String nombre, boolean esBajoVigilancia) {
		this.idEnfermedad = idEnfermedad;
		this.nombre = nombre;
		this.esBajoVigilancia = esBajoVigilancia;
	}

	public int getIdEnfermedad() {
		return idEnfermedad;
	}

	public void setIdEnfermedad(int idEnfermedad) {
		this.idEnfermedad = idEnfermedad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isEsBajoVigilancia() {
		return esBajoVigilancia;
	}

	public void setEsBajoVigilancia(boolean esBajoVigilancia) {
		this.esBajoVigilancia = esBajoVigilancia;
	}

	@Override
	public String toString() {
		return nombre; // Para que se vea bien en los JComboBox
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}