package logico;

import java.util.ArrayList;

public class Paciente extends Persona {

	private static final long serialVersionUID = 6L;
	private int idPaciente;
	private ArrayList<Consulta> historialClinico;
	private ArrayList<Vacuna> vacunasAplicadas;

	public Paciente() {
		super();
		this.idPaciente = 0; // Se asignará un ID real en la clase Clinica
		// Importante inicializar las colecciones
		this.historialClinico = new ArrayList<Consulta>();
		this.vacunasAplicadas = new ArrayList<Vacuna>();
	}

	public ArrayList<Consulta> consultarHistorial() {
		return historialClinico;
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

	/*
	 * Función: equals Argumentos: (Object) obj: El objeto a comparar. Objetivo:
	 * Compara si dos pacientes son iguales basándose en su cédula. Retorno:
	 * (boolean): true si son iguales, false si no.
	 */
	@Override
	public boolean equals(Object objeto) {
		if (this == objeto) {
			return true;
		}
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		Paciente paciente = (Paciente) objeto;
		// La lógica de negocio indica que un paciente es único por su cédula
		return getCedula() != null ? getCedula().equals(paciente.getCedula()) : paciente.getCedula() == null;
	}

	/*
	 * Función: hashCode Argumentos: Ninguno. Objetivo: Generar un código hash
	 * basado en la cédula. Retorno: (int): El código hash.
	 */
	@Override
	public int hashCode() {
		// Se usa el hashCode de la cédula (String)
		return getCedula() != null ? getCedula().hashCode() : 0;
	}
}