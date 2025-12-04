package logico;

import java.util.Objects;

public class Doctor extends Personal {

	private static final long serialVersionUID = 3L;
	private int idDoctor;
	private String nombre;
	private String especialidad;
	private int cupoDia;
	private String rutaFoto;

	public Doctor() {
		super();
		this.idDoctor = 0;
		this.nombre = null;
		this.especialidad = null;
		this.cupoDia = 0;
		this.rutaFoto = null;
	}

	public Doctor(String usuario, String contrasenia, String nombre, String especialidad, int cupoDia) {
		super(usuario, contrasenia);
		this.idDoctor = 0;
		this.nombre = nombre;
		this.especialidad = especialidad;
		this.cupoDia = cupoDia;
	}

	@Override
	public int getId() {
		return this.idDoctor;
	}

	@Override
	public String getNombre() {
		return nombre;
	}

	public int getIdDoctor() {
		return idDoctor;
	}

	public void setIdDoctor(int idDoctor) {
		this.idDoctor = idDoctor;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getRutaFoto() {
		return rutaFoto;
	}

	public void setRutaFoto(String rutaFoto) {
		this.rutaFoto = rutaFoto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Doctor doctor = (Doctor) obj;
		return idDoctor == doctor.idDoctor && idDoctor != 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idDoctor);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}