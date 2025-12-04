package logico;

import java.io.Serializable;

public class Diagnostico implements Serializable {

	private static final long serialVersionUID = 13L;

	public enum TipoSeveridad {
		LEVE, MODERADO, GRAVE
	}

	private int idDiagnostico;
	private String notasObservacion;
	private TipoSeveridad severidad;
	private Enfermedad enfermedadDetectada;

	private String tratamiento;
	private String nombre;

	public Diagnostico() {
		this.idDiagnostico = 0;
		this.notasObservacion = "";
		this.severidad = null;
		this.enfermedadDetectada = null;
		this.tratamiento = "";
		this.nombre = "";
	}

	public Diagnostico(String notasObservacion, Enfermedad enfermedadDetectada, TipoSeveridad severidad) {
		this.idDiagnostico = 0;
		this.notasObservacion = notasObservacion;
		this.enfermedadDetectada = enfermedadDetectada;
		this.severidad = severidad;
		if (enfermedadDetectada != null) {
			this.nombre = enfermedadDetectada.getNombre();
		}
	}

	public boolean requiereNotificacion() {
		if (enfermedadDetectada != null) {
			return enfermedadDetectada.isEsBajoVigilancia();
		}
		return false;
	}

	public int getIdDiagnostico() {
		return idDiagnostico;
	}

	public void setIdDiagnostico(int idDiagnostico) {
		this.idDiagnostico = idDiagnostico;
	}

	public String getNotasObservacion() {
		return notasObservacion;
	}

	public void setNotasObservacion(String notasObservacion) {
		this.notasObservacion = notasObservacion;
	}

	public TipoSeveridad getSeveridad() {
		return severidad;
	}

	public void setSeveridad(TipoSeveridad severidad) {
		this.severidad = severidad;
	}

	public Enfermedad getEnfermedadDetectada() {
		return enfermedadDetectada;
	}

	public void setEnfermedadDetectada(Enfermedad enfermedadDetectada) {
		this.enfermedadDetectada = enfermedadDetectada;
		if (enfermedadDetectada != null) {
			this.nombre = enfermedadDetectada.getNombre();
		}
	}

	public void setEnfermedad(Enfermedad enfermedad) {
		setEnfermedadDetectada(enfermedad);
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}