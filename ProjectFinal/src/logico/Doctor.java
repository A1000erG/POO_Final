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
		super(usuario, contrasenia); // Llama al constructor de Personal
		this.idDoctor = 0; // El ID será asignado por la Clínica
		this.nombre = nombre;
		this.especialidad = especialidad;
		this.cupoDia = cupoDia;
	}

	/*
	 * Función: getId (Implementación) Argumentos: Ninguno. Objetivo: Obtener el ID
	 * único del doctor (implementa el método abstracto de Personal). Retorno:
	 * (int): El ID del doctor.
	 */
	@Override
	public int getId() {
		return this.idDoctor;
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

	/*
	 * Función: equals Argumentos: (Object) obj: El objeto a comparar. Objetivo:
	 * Compara si dos doctores son iguales basándose en su ID de Doctor. Retorno:
	 * (boolean): true si son iguales, false si no.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Doctor doctor = (Doctor) obj;
		// La comparación más robusta es por ID único.
		return idDoctor == doctor.idDoctor && idDoctor != 0;
	}

	/*
	 * Función: hashCode Argumentos: Ninguno. Objetivo: Generar un código hash
	 * basado en el ID del Doctor. Retorno: (int): El código hash.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(idDoctor);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}