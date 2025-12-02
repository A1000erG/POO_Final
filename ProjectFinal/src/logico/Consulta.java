package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Consulta implements Serializable {

	private static final long serialVersionUID = 9L;
	private int idConsulta;
	private String codigo; // Agregado para manejo visual de ID (ej: "CNS-001")
	private Cita citaAsociada;
	private Doctor doctor;
	private String sintomas;
	private Enfermedad enfermedad;

	// CAMBIO: Ahora soporta lista de diagnósticos para compatibilidad con RegConsulta
	private ArrayList<Diagnostico> diagnosticos;

	private boolean agregarAlHistorial;
	private boolean transferida;
	private LocalDate fecha;

	public Consulta() {
		this.idConsulta = 0;
		this.citaAsociada = null;
		this.doctor = null;
		this.sintomas = "";
		this.diagnosticos = new ArrayList<Diagnostico>(); // Inicializar lista
		this.agregarAlHistorial = true;
		this.transferida = false;
		this.fecha = LocalDate.now();
		this.codigo = "";
	}

	public int getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(int idConsulta) {
		this.idConsulta = idConsulta;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Cita getCitaAsociada() {
		return citaAsociada;
	}

	public void setCitaAsociada(Cita citaAsociada) {
		this.citaAsociada = citaAsociada;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getSintomas() {
		return sintomas;
	}

	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}
	
	// Alias para compatibilidad singular/plural
	public void setSintoma(String sintoma) {
		this.sintomas = sintoma;
	}
	
	public String getSintoma() {
		return this.sintomas;
	}

	public ArrayList<Diagnostico> getDiagnosticos() {
		return diagnosticos;
	}

	public void setDiagnosticos(ArrayList<Diagnostico> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	
	// Método de compatibilidad por si alguna parte antigua pide un solo diagnóstico
	// Retorna el primero o null
	public Diagnostico getDiagnostico() {
		if (diagnosticos != null && !diagnosticos.isEmpty()) {
			return diagnosticos.get(0);
		}
		return null;
	}

	public boolean isAgregarAlHistorial() {
		return agregarAlHistorial;
	}

	public void setAgregarAlHistorial(boolean agregarAlHistorial) {
		this.agregarAlHistorial = agregarAlHistorial;
	}

	public boolean isTransferida() {
		return transferida;
	}

	public void setTransferida(boolean transferida) {
		this.transferida = transferida;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Enfermedad getEnfermedad() {
		return enfermedad;
	}

	public void setEnfermedad(Enfermedad enfermedad) {
		this.enfermedad = enfermedad;
	}
}