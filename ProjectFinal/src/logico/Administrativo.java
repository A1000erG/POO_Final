package logico;

public class Administrativo extends Personal {

	private static final long serialVersionUID = 4L;
	private String nombre;
	private String cargo;

	/*
	 * Función: Administrativo (Constructor) Argumentos: Ninguno. Objetivo:
	 * Inicializar un objeto Administrativo con valores por defecto. Retorno:
	 * Ninguno (es un constructor).
	 */
	public Administrativo() {
		super(); // Llama al constructor de Personal
		this.nombre = null;
		this.cargo = null;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
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

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/*
	 * Función: equals Argumentos: (Object) obj: El objeto a comparar. Objetivo:
	 * Compara si dos administrativos son iguales basándose en su nombre de usuario.
	 * Retorno: (boolean): true si son iguales, false si no.
	 */
	@Override
	public boolean equals(Object objeto) {
		if (this == objeto) {
			return true;
		}
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		Administrativo that = (Administrativo) objeto;
		// La lógica de negocio más común es comparar por el login (usuario)
		return getUsuario() != null ? getUsuario().equals(that.getUsuario()) : that.getUsuario() == null;
	}

	/*
	 * Función: hashCode Argumentos: Ninguno. Objetivo: Generar un código hash
	 * basado en el nombre de usuario. Retorno: (int): El código hash.
	 */
	@Override
	public int hashCode() {
		return getUsuario() != null ? getUsuario().hashCode() : 0;
	}
}