package logico;

import java.io.Serializable;

public abstract class Personal implements Serializable {

	private static final long serialVersionUID = 2L;
	private String usuario;
	private String contrasenia;
	private boolean activo;
	private String causaDeshabilitacion;
	private String rutaFoto;

	public Personal() {
		this.usuario = null;
		this.contrasenia = null;
		this.activo = true;
		this.causaDeshabilitacion = "";
		this.rutaFoto = "";
	}

	public Personal(String usuario, String contrasenia) {
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.activo = true;
		this.causaDeshabilitacion = "";
		this.rutaFoto = "";
	}

	public abstract String getNombre();

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

	public String getRutaFoto() {
		return rutaFoto;
	}

	public void setRutaFoto(String rutaFoto) {
		this.rutaFoto = rutaFoto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}