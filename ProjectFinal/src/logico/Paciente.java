package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class Paciente extends Persona implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idPaciente;

	private ArrayList<Consulta> historialClinico;

	private ArrayList<Vacuna> vacunasAplicadas;

	public Paciente() {
		super();
		this.historialClinico = new ArrayList<>();
		this.vacunasAplicadas = new ArrayList<>();
	}

	public ArrayList<Consulta> consultarHistorial() {
		return this.historialClinico;
	}

	public int getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}

	public ArrayList<Consulta> getHistorialClinico() {
		return historialClinico;
	}

	public void setHistorialClinico(ArrayList<Consulta> historialClinico) {
		this.historialClinico = historialClinico;
	}

	public ArrayList<Vacuna> getVacunasAplicadas() {
		return vacunasAplicadas;
	}

	public void setVacunasAplicadas(ArrayList<Vacuna> vacunasAplicadas) {
		this.vacunasAplicadas = vacunasAplicadas;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Paciente paciente = (Paciente) obj;
		return idPaciente == paciente.idPaciente && getCedula().equals(paciente.getCedula());
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idPaciente, getCedula());
	}
}