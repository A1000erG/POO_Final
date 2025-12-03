package logico;

import java.util.ArrayList;
import java.util.Objects;

public class Paciente extends Persona {

	private static final long serialVersionUID = 6L;
	private int idPaciente;
	private ArrayList<Consulta> historialClinico;
	private ArrayList<Vacuna> vacunasAplicadas;
	//private String tipoSangre;
	//private Float peso;
	//private Float estatura;
	//private Float IMC;

	public Paciente() {
		super();
		this.idPaciente = 0;
		this.historialClinico = new ArrayList<Consulta>();
		this.vacunasAplicadas = new ArrayList<Vacuna>();
		//this.peso=0.0f;
		//this.estatura=0.0f;
		//this.tipoSangre="O+";
		//this.IMC=0.0f;
	}

	/*
	 * Función: getHistorialClinico Argumentos: Ninguno. Objetivo: Obtener la lista
	 * de consultas pasadas. Retorno: (ArrayList<Consulta>): La lista de consultas.
	 */
	public ArrayList<Consulta> getHistorialClinico() {
		return historialClinico;
	}

	public int getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}

	/*public String getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public Float getPeso() {
		return peso;
	}

	public void setPeso(Float peso) {
		this.peso = peso;
	}

	public Float getEstatura() {
		return estatura;
	}

	public void setEstatura(Float estatura) {
		this.estatura = estatura;
	}

	public Float getIMC() {
		return IMC;
	}

	public void setIMC(Float iMC) {
		IMC = iMC;
	}*/

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
	 * Función: equals Argumentos: (Object) obj: Objeto a verificar. Objetivo:
	 * Comparar pacientes por Cédula (Regla de Negocio). Retorno: (boolean):
	 * Verdadero si tienen la misma cédula.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Paciente paciente = (Paciente) obj;
		return Objects.equals(getCedula(), paciente.getCedula());
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}