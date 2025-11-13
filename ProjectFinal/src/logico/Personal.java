package logico;

import java.io.Serializable;

public abstract class Personal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String usuario;
	private String contraseña;

	public Personal() {
	}

	public abstract String getNombre();

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
}