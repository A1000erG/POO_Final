package logico;

import java.io.Serializable;

public abstract class Personal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String usuario;
	private String contrasenia;
	private String contrasenna;

	public Personal() {
	}

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

	public String getContrasenna() {
		return contrasenna;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public void setContrasenna(String contrasenna) {
		this.contrasenna = contrasenna;
	}
}