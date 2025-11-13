package logico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase controladora principal (Singleton).
 */
public class Clinica implements Serializable {

	private static final long serialVersionUID = 1L;

	private static volatile Clinica instance;

	private ArrayList<Doctor> doctores;
	private ArrayList<Paciente> pacientes;
	private ArrayList<Cita> citas;
	private ArrayList<Consulta> consultas;
	private ArrayList<Vacuna> vacunas;

	private ArrayList<Solicitante> solicitantes;

	private ArrayList<Administrativo> administrativos;

	private int proximoIdPaciente;
	private int proximoIdCita;
	private int proximoIdConsulta;
	private int proximoIdVacuna;

	private Clinica() {
		if (instance != null) {
			throw new IllegalStateException("Singleton ya instanciado.");
		}
		doctores = new ArrayList<>();
		pacientes = new ArrayList<>();
		citas = new ArrayList<>();
		consultas = new ArrayList<>();
		vacunas = new ArrayList<>();
		solicitantes = new ArrayList<>();
		administrativos = new ArrayList<>();

		proximoIdPaciente = 1;
		proximoIdCita = 1;
		proximoIdConsulta = 1;
		proximoIdVacuna = 1;
	}

	// Método de acceso global (Double-Checked Locking)
	public static Clinica getInstance() {
		Clinica result = instance;
		if (result == null) {
			synchronized (Clinica.class) {
				result = instance;
				if (result == null) {
					instance = result = new Clinica();
				}
			}
		}
		return result;
	}

	public Personal login(String usuario, String password) {
		for (Doctor d : this.doctores) {
			if (d.getUsuario().equals(usuario) && d.getContrasenna().equals(password)) {
				return d;
			}
		}
		for (Administrativo a : this.administrativos) {
			if (a.getUsuario().equals(usuario) && a.getContrasenna().equals(password)) {
				return a;
			}
		}
		return null;
	}

	public void registrarDoctor(Doctor d) {
		this.doctores.add(d);
	}

	public void registrarAdministrativo(Administrativo a) {
		this.administrativos.add(a);
	}

	public void registrarPaciente(Paciente p) {
		if (p.getIdPaciente() == 0) {
			p.setIdPaciente(this.proximoIdPaciente++);
		}
		this.pacientes.add(p);
	}

	public void registrarSolicitante(Solicitante s) {
		this.solicitantes.add(s);
	}

	public Paciente convertirSolicitanteAPaciente(Solicitante solicitante) {
		if (getPacientePorCedula(solicitante.getCedula()) != null) {
			System.err.println("Error: Ya existe un paciente con la cédula " + solicitante.getCedula());
			return null;
		}
		Paciente nuevoPaciente = new Paciente();
		nuevoPaciente.setIdPaciente(this.proximoIdPaciente++);
		nuevoPaciente.setCedula(solicitante.getCedula());
		nuevoPaciente.setNombre(solicitante.getNombre());
		nuevoPaciente.setFechaNacimiento(solicitante.getFechaNacimiento());
		nuevoPaciente.setSexo(solicitante.getSexo());
		nuevoPaciente.setTelefono(solicitante.getTelefono());
		this.pacientes.add(nuevoPaciente);
		this.solicitantes.remove(solicitante);
		return nuevoPaciente;
	}

	public boolean programarCita(Paciente paciente, Doctor doctor, Date fecha, String hora) {
		if (esFechaPasada(fecha)) {
			System.err.println("Error: No se puede programar cita en una fecha pasada.");
			return false;
		}

		int citasProgramadas = 0;
		for (Cita c : this.citas) {
			if (c.getDoctor().equals(doctor) && sonMismoDia(c.getFecha(), fecha) && c.getEstado().equals("Pendiente")) {
				citasProgramadas++;
			}
		}

		if (citasProgramadas >= doctor.getCupoDia()) {
			System.err.println("Error: El cupo del doctor " + doctor.getNombre() + " está lleno para ese día.");
			return false;
		}

		Cita nuevaCita = new Cita();
		nuevaCita.setIdCita(this.proximoIdCita++);
		nuevaCita.setPaciente(paciente);
		nuevaCita.setDoctor(doctor);
		nuevaCita.setFecha(fecha);
		nuevaCita.setHora(hora);
		nuevaCita.setEstado("Pendiente");
		this.citas.add(nuevaCita);
		return true;
	}

	public void programarCita(Cita c) {
		if (c.getDoctor() != null && c.getPaciente() != null && c.getFecha() != null) {
			boolean exito = programarCita(c.getPaciente(), c.getDoctor(), c.getFecha(), c.getHora());
			if (exito && c.getIdCita() == 0) {
				// Si se pasó un objeto Cita sin ID, se lo asignamos
				c.setIdCita(this.proximoIdCita - 1); // Asigna el ID que se acaba de generar
			}
		} else {
			System.err.println("Error: Faltan datos en la cita para programarla.");
		}
	}

	public void cancelarCita(int idCita) {
		for (Cita c : this.citas) {
			if (c.getIdCita() == idCita) {
				if (esFechaPasada(c.getFecha())) {
					System.err.println("No se puede cancelar una cita pasada.");
					return;
				}
				c.setEstado("Cancelada");
				return;
			}
		}
	}

	public boolean modificarEstadoCita(Cita cita, String nuevoEstado) {
		if (esFechaPasada(cita.getFecha()) && !cita.getEstado().equals("Pendiente")) {
			System.err.println("Error: No se puede modificar una cita pasada que no esté pendiente.");
			return false;
		}
		if (cita.getEstado().equals("Realizada")) {
			System.err.println("Error: No se puede modificar una cita ya realizada.");
			return false;
		}
		cita.setEstado(nuevoEstado);
		return true;
	}

	public void registrarConsulta(Consulta consulta) {
		consulta.setIdConsulta(this.proximoIdConsulta++);
		this.consultas.add(consulta);

		if (consulta.isAgregarAlHistorial()) {
			Cita citaAsociada = consulta.getCitaAsociada();
			if (citaAsociada != null) {
				Paciente paciente = citaAsociada.getPaciente();
				if (paciente != null) {
					paciente.getHistorialClinico().add(consulta);
				}
			}
		}

		if (consulta.getCitaAsociada() != null) {
			consulta.getCitaAsociada().setEstado("Realizada");
		}
	}

	public void registrarVacuna(Paciente p, Vacuna v) {
		if (v.getId() == 0) {
			v.setId(this.proximoIdVacuna++);
		}
		p.getVacunasAplicadas().add(v);
		this.vacunas.add(v);
	}

	public ArrayList<Cita> verCitas() {
		return this.citas;
	}

	public ArrayList<Consulta> historialClinico(Paciente p) {
		return p.consultarHistorial();
	}

	public void generarReporteGeneral() {
		System.out.println("--- Reporte General ---");
		System.out.println("Total Pacientes: " + pacientes.size());
		System.out.println("Total Doctores: " + doctores.size());
		System.out.println("Total Administrativos: " + administrativos.size());
		System.out.println("Total Citas Programadas: " + citas.size());
		System.out.println("Total Consultas Atendidas: " + consultas.size());
		System.out.println("Total Vacunas Aplicadas: " + vacunas.size());
		System.out.println("-----------------------");
	}

	public Paciente getPacientePorCedula(String cedula) {
		for (Paciente p : this.pacientes) {
			if (p.getCedula().equals(cedula)) {
				return p;
			}
		}
		return null;
	}

	private boolean sonMismoDia(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}

	private boolean esFechaPasada(Date fecha) {
		Calendar hoy = Calendar.getInstance();
		hoy.set(Calendar.HOUR_OF_DAY, 0);
		hoy.set(Calendar.MINUTE, 0);
		hoy.set(Calendar.SECOND, 0);
		hoy.set(Calendar.MILLISECOND, 0);

		Calendar calFecha = Calendar.getInstance();
		calFecha.setTime(fecha);
		calFecha.set(Calendar.HOUR_OF_DAY, 0);
		calFecha.set(Calendar.MINUTE, 0);
		calFecha.set(Calendar.SECOND, 0);
		calFecha.set(Calendar.MILLISECOND, 0);

		return calFecha.before(hoy);
	}

	public ArrayList<Doctor> getDoctores() {
		return doctores;
	}

	public ArrayList<Paciente> getPacientes() {
		return pacientes;
	}

	public ArrayList<Cita> getCitas() {
		return citas;
	}

	public ArrayList<Consulta> getConsultas() {
		return consultas;
	}

	public ArrayList<Vacuna> getVacunas() {
		return vacunas;
	}

	public ArrayList<Solicitante> getSolicitantes() {
		return solicitantes;
	}

	public ArrayList<Administrativo> getAdministrativos() {
		return administrativos;
	}
}