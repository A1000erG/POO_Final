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
	 * Función: getNombre (Abstracta) Argumentos: Ninguno. Objetivo: Método
	 * abstracto para forzar a las clases hijas a implementar la obtención del
	 * nombre. Retorno: (String): El nombre del personal.
	 */
	public abstract String getNombre();

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

	public String getContrasenna() {
		return getContrasenia();
	}

	public void setContrasenna(String contrasenna) {
		setContrasenia(contrasenna);
	}
}