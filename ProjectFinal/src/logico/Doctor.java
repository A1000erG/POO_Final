package logico;

import java.io.Serializable;

public class Doctor extends Personal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nombre;
	private String especialidad;
	private int cupoDia;

	public Doctor() {
		super();
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public int getCupoDia() {
		return cupoDia;
	}

	public void setCupoDia(int cupoDia) {
		this.cupoDia = cupoDia;
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
		Doctor doctor = (Doctor) obj;
		return getUsuario().equals(doctor.getUsuario()) && especialidad.equals(doctor.especialidad);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(getUsuario(), especialidad);
	}
}