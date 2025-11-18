package logico;

import java.util.Objects;

public class Administrativo extends Personal {

	private static final long serialVersionUID = 4L;
	private int idAdmin;
	private String nombre;
	private String cargo;

	public Administrativo() {
		super();
		this.idAdmin = 0;
		this.nombre = null;
		this.cargo = null;
	}

	public Administrativo(String usuario, String contrasenia, String nombre, String cargo) {
		super(usuario, contrasenia);
		this.idAdmin = 0;
		this.nombre = nombre;
		this.cargo = cargo;
	}

	/*
	 * Función: getId (Implementación) Argumentos: Ninguno. Objetivo: Obtener el ID
	 * único del administrativo (implementa el método abstracto de Personal).
	 * Retorno: (int): El ID del administrativo.
	 */
	@Override
	public int getId() {
		return this.idAdmin;
	}

	/*
	 * Función: getNombre (Implementación) Argumentos: Ninguno. Objetivo: Obtener el
	 * nombre del administrativo (implementa el método abstracto de Personal).
	 * Retorno: (String): El nombre del administrativo.
	 */
	@Override
	public String getNombre() {
		return nombre;
	}

	public int getIdAdmin() {
		return idAdmin;
	}

	public void setIdAdmin(int idAdmin) {
		this.idAdmin = idAdmin;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	/*
	 * Función: equals Argumentos: (Object) obj: El objeto a comparar. Objetivo:
	 * Compara si dos administrativos son iguales basándose en su ID. Retorno:
	 * (boolean): true si son iguales, false si no.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Administrativo that = (Administrativo) obj;
		return idAdmin == that.idAdmin && idAdmin != 0;
	}

	/*
	 * Función: hashCode Argumentos: Ninguno. Objetivo: Generar un código hash
	 * basado en el ID del Administrativo. Retorno: (int): El código hash.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(idAdmin);
	}
}