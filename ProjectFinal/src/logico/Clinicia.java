package logico;

import java.util.ArrayList;

public class Clinicia {

	private ArrayList<Doctor> doctores;
	private ArrayList<Paciente> pacientes;
	private ArrayList<Cita> citas;
	private ArrayList<Consulta> consultas;
	private ArrayList<Vacuna> vacunas;

	public Clinicia() {
		this.doctores = new ArrayList<>();
		this.pacientes = new ArrayList<>();
		this.citas = new ArrayList<>();
		this.consultas = new ArrayList<>();
		this.vacunas = new ArrayList<>();
	}

	public ArrayList<Doctor> getDoctores() {
		return doctores;
	}

	public void setDoctores(ArrayList<Doctor> doctores) {
		this.doctores = doctores;
	}

	public ArrayList<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(ArrayList<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public ArrayList<Cita> getCitas() {
		return citas;
	}

	public void setCitas(ArrayList<Cita> citas) {
		this.citas = citas;
	}

	public ArrayList<Consulta> getConsultas() {
		return consultas;
	}

	public void setConsultas(ArrayList<Consulta> consultas) {
		this.consultas = consultas;
	}

	public ArrayList<Vacuna> getVacunas() {
		return vacunas;
	}

	public void setVacunas(ArrayList<Vacuna> vacunas) {
		this.vacunas = vacunas;
	}
}
