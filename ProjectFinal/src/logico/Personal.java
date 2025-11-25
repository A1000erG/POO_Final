package logico;

import java.io.Serializable;

public abstract class Personal implements Serializable {

	private static final long serialVersionUID = 2L;
	private String usuario;
	private String contrasenia;

	public Personal() {
		this.usuario = null;
		this.contrasenia = null;
	}

	/*
	 * Función: Personal (Constructor) Argumentos: (String) usuario: Credencial de
	 * acceso (login). (String) contrasenia: Contraseña de acceso. Objetivo:
	 * Inicializar las credenciales del personal. Retorno: (Ninguno): Es un
	 * constructor.
	 */
	public Personal(String usuario, String contrasenia) {
		this.usuario = usuario;
		this.contrasenia = contrasenia;
	}

	/*
	 * Función: getNombre (Abstracto) Argumentos: Ninguno. Objetivo: Obligar a las
	 * clases hijas a devolver el nombre real del personal. Retorno: (String): El
	 * nombre.
	 */
	public abstract String getNombre();

	/*
	 * Función: getId (Abstracto) Argumentos: Ninguno. Objetivo: Obligar a las
	 * clases hijas a devolver el ID numérico. Retorno: (int): El identificador
	 * único.
	 */
	public abstract int getId();

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}