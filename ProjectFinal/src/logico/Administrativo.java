package logico;

import java.io.Serializable;

/**
 * Clase Administrativo (Modelo). Hereda de Personal e implementa Serializable.
 * Representa al usuario administrativo.
 */
public class Administrativo extends Personal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nombre;
	private String cargo;

	public Administrativo() {
		super();
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	@Override
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Administrativo that = (Administrativo) obj;
		return getUsuario().equals(that.getUsuario());
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(getUsuario());
	}
}