package logico;

import java.io.Serializable;

public abstract class Personal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String usuario;
<<<<<<< HEAD
	private String contrasenia;
=======
	private String contrasenna;
>>>>>>> branch 'main' of https://github.com/A1000erG/POO_Final.git

	public Personal() {
	}

	public abstract String getNombre();

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

<<<<<<< HEAD
	public String getContrasenia() {
		return contrasenia;
=======
	public String getContrasenna() {
		return contrasenna;
>>>>>>> branch 'main' of https://github.com/A1000erG/POO_Final.git
	}

<<<<<<< HEAD
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
=======
	public void setContrasenna(String contrasenna) {
		this.contrasenna = contrasenna;
>>>>>>> branch 'main' of https://github.com/A1000erG/POO_Final.git
	}
}