package logico;

public class Doctor extends Personal {

	private static final long serialVersionUID = 3L;
	private String nombre;
	private String especialidad;
	private int cupoDia;

	public Doctor(String usuario, String contrasenia, String nombre, String especialidad, int cupoDia) {
		super(usuario, contrasenia);
		this.nombre = nombre;
		this.especialidad = especialidad;
		this.cupoDia = 0;
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

	/*
	 * Función: setCupoDia Argumentos: (int) cupoDia: El nuevo límite de citas
	 * diarias. Objetivo: Establecer el límite de citas diarias del doctor. Retorno:
	 * (void): No retorna valor.
	 */
	public void setCupoDia(int cupoDia) {
		this.cupoDia = cupoDia;
	}

	/*
	 * Función: getNombre (Implementación) Argumentos: Ninguno. Objetivo: Obtener el
	 * nombre del doctor (implementa el método abstracto de Personal). Retorno:
	 * (String): El nombre del doctor.
	 */
	@Override
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/*
	 * Función: equals Argumentos: (Object) obj: El objeto a comparar. Objetivo:
	 * Compara si dos doctores son iguales basándose en su nombre de usuario.
	 * Retorno: (boolean): true si son iguales, false si no.
	 */
	@Override
	public boolean equals(Object objeto) {
		if (this == objeto) {
			return true;
		}
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		Doctor doctor = (Doctor) objeto;
		// La lógica de negocio más común es comparar por el login (usuario)
		return getUsuario() != null ? getUsuario().equals(doctor.getUsuario()) : doctor.getUsuario() == null;
	}

	@Override
	public int hashCode() {
		return getUsuario() != null ? getUsuario().hashCode() : 0;
	}
}