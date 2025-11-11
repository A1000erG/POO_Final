package logico;

public class Doctor extends Personal {

	private String especialidad;
	private int cupoDia;

	public Doctor(int id, String nombre, String usuario, String contrasena, String especialidad, int cupoDia) {
		super(id, nombre, usuario, contrasena);
		this.especialidad = especialidad;
		this.cupoDia = cupoDia;
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
}