package logico;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Clinica implements Serializable {

	private static final long serialVersionUID = 10L;
	private static Clinica instance;

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

	private static final String ARCHIVO_DATOS = "clinica.dat";

	private Clinica() {
		this.doctores = new ArrayList<Doctor>();
		this.pacientes = new ArrayList<Paciente>();
		this.citas = new ArrayList<Cita>();
		this.consultas = new ArrayList<Consulta>();
		this.vacunas = new ArrayList<Vacuna>();
		this.solicitantes = new ArrayList<Solicitante>();
		this.administrativos = new ArrayList<Administrativo>();

		this.proximoIdPaciente = 1;
		this.proximoIdCita = 1;
		this.proximoIdConsulta = 1;
		this.proximoIdVacuna = 1;
	}


	public static Clinica getInstance() {
		if (instance == null) {
			instance = cargarDatos();
			if (instance == null) {
				instance = new Clinica();
				System.out.println("Nueva instancia de Clinica creada.");
			} else {
				System.out.println("Instancia de Clinica cargada desde " + ARCHIVO_DATOS);
			}
			
			instance.crearAdminPorDefecto();
		}
		return instance;
	}


	public void registrarDoctor(Doctor doctor) {
		this.doctores.add(doctor);
	}

	public void registrarAdministrativo(Administrativo admin) {
		this.administrativos.add(admin);
	}

	public void registrarPaciente(Paciente paciente) {
		paciente.setIdPaciente(this.proximoIdPaciente);
		this.proximoIdPaciente++;
		this.pacientes.add(paciente);
	}

	public void registrarSolicitante(Solicitante solicitante) {
		this.solicitantes.add(solicitante);
	}

	/*
	 * Función: getPacientePorCedula Argumentos: (String) cedula: La cédula a
	 * buscar. Objetivo: Encontrar un paciente en la lista de pacientes usando su
	 * cédula. Retorno: (Paciente): El objeto Paciente si se encuentra, o null si no
	 * existe.
	 */
	public Paciente getPacientePorCedula(String cedula) {
		for (Paciente pacienteActual : pacientes) {
			if (pacienteActual.getCedula().equals(cedula)) {
				return pacienteActual;
			}
		}
		return null;
	}

	/*
	 * Función: convertirSolicitanteAPaciente Argumentos: (Solicitante) solicitante:
	 * El solicitante a convertir. Objetivo: Convertir un Solicitante en un
	 * Paciente. Verifica que no exista ya un paciente con esa cédula. Si no existe,
	 * crea un nuevo Paciente, copia los datos, lo registra y elimina al solicitante
	 * de la lista. Retorno: (Paciente): El nuevo objeto Paciente si la conversión
	 * fue exitosa, o null si ya existía un paciente con esa cédula.
	 */
	public Paciente convertirSolicitanteAPaciente(Solicitante solicitante) {
		Paciente pacienteExistente = getPacientePorCedula(solicitante.getCedula());
		if (pacienteExistente != null) {
			return null;
		}

		Paciente nuevoPaciente = new Paciente();
		nuevoPaciente.setIdPaciente(proximoIdPaciente++);
		nuevoPaciente.setCedula(solicitante.getCedula());
		nuevoPaciente.setNombre(solicitante.getNombre());
		nuevoPaciente.setFechaNacimiento(solicitante.getFechaNacimiento());
		nuevoPaciente.setSexo(solicitante.getSexo());
		nuevoPaciente.setTelefono(solicitante.getTelefono());

		pacientes.add(nuevoPaciente);
		solicitantes.remove(solicitante);

		return nuevoPaciente;
	}

	/*
	 * Función: programarCita Argumentos: (Paciente) paciente: El paciente para la
	 * cita. (Doctor) doctor: El doctor para la cita. (LocalDate) fecha: La fecha
	 * deseada. (String) hora: La hora deseada. Objetivo: Crear y registrar una
	 * nueva cita, validando las reglas de negocio (fecha no pasada, cupo del
	 * doctor). Retorno: (boolean): true si la cita se programó con éxito, false si
	 * no.
	 */
	public boolean programarCita(Paciente paciente, Doctor doctor, LocalDate fecha, String hora) {
		if (paciente == null || doctor == null || fecha == null || hora == null) {
			return false;
		}

		if (esFechaPasada(fecha)) {
			return false;
		}

		int citasProgramadas = 0;
		for (Cita c : citas) {
			if (c.getDoctor().equals(doctor) && c.getFecha().isEqual(fecha)) {
				citasProgramadas++;
			}
		}

		if (citasProgramadas >= doctor.getCupoDia()) {
			return false;
		}

		Cita nuevaCita = new Cita();
		nuevaCita.setIdCita(proximoIdCita++);
		nuevaCita.setPaciente(paciente);
		nuevaCita.setDoctor(doctor);
		nuevaCita.setFecha(fecha);
		nuevaCita.setHora(hora);
		nuevaCita.setEstado("Pendiente");
		citas.add(nuevaCita);

		return true;
	}

	public boolean cancelarCita(Cita cita) {
		if (esFechaPasada(cita.getFecha())) {
			return false;
		}

		cita.setEstado("Cancelada");
		return true;
	}

	/*
	 * Función: modificarEstadoCita Argumentos: (Cita) cita: La cita a modificar.
	 * (String) nuevoEstado: El nuevo estado (Ej. "Realizada"). Objetivo: Modificar
	 * el estado de una cita. Se usa principalmente para marcar una cita como
	 * "Realizada" al crear una consulta. Retorno: (boolean): true si se modificó,
	 * false si no.
	 */
	public boolean modificarEstadoCita(Cita cita, String nuevoEstado) {
		if (cita.getEstado().equals("Realizada")) {
			return false;
		}
		if (esFechaPasada(cita.getFecha()) && !cita.getEstado().equals("Pendiente")) {
			return false;
		}

		cita.setEstado(nuevoEstado);
		return true;
	}

	/*
	 * Función: registrarConsulta Argumentos: (Consulta) consulta: La consulta a
	 * registrar. Objetivo: Añadir una nueva consulta al sistema. Asigna un ID único
	 * y la agrega al historial del paciente si corresponde. También marca la cita
	 * asociada como "Realizada". Retorno: (void): No retorna valor.
	 */
	public void registrarConsulta(Consulta consulta) {
		consulta.setIdConsulta(this.proximoIdConsulta);
		this.proximoIdConsulta++;
		this.consultas.add(consulta);

		if (consulta.isAgregarAlHistorial() && consulta.getCitaAsociada() != null) {
			Paciente pacienteDeLaConsulta = consulta.getCitaAsociada().getPaciente();
			if (pacienteDeLaConsulta != null) {
				pacienteDeLaConsulta.getHistorialClinico().add(consulta);
			}
		}

		if (consulta.getCitaAsociada() != null) {
			modificarEstadoCita(consulta.getCitaAsociada(), "Realizada");
		}
	}

	/*
	 * Función: registrarVacuna Argumentos: (Paciente) paciente: El paciente que
	 * recibe la vacuna. (Vacuna) vacuna: La vacuna a registrar. Objetivo: Asignar
	 * un ID a la vacuna y añadirla al sistema y al historial del paciente. Retorno:
	 * (void): No retorna valor.
	 */
	public void registrarVacuna(Paciente paciente, Vacuna vacuna) {
		vacuna.setId(this.proximoIdVacuna);
		this.proximoIdVacuna++;

		this.vacunas.add(vacuna);
		paciente.getVacunasAplicadas().add(vacuna);
	}

	/*
	 * Función: getDoctorPorUsuario Argumentos: (String) usuario: El nombre de
	 * usuario (login) del doctor. Objetivo: Encontrar un doctor usando su nombre de
	 * usuario. Retorno: (Doctor): El objeto Doctor si se encuentra, o null si no.
	 */
	public Doctor getDoctorPorUsuario(String usuario) {
		for (Doctor doctor : doctores) {
			if (doctor.getUsuario().equalsIgnoreCase(usuario)) {
				return doctor;
			}
		}
		return null;
	}

	/*
	 * Función: getCitaPorId Argumentos: (int) idCita: El ID único de la cita.
	 * Objetivo: Encontrar una cita usando su ID. Retorno: (Cita): El objeto Cita si
	 * se encuentra, o null si no.
	 */
	public Cita getCitaPorId(int idCita) {
		for (Cita cita : citas) {
			if (cita.getIdCita() == idCita) {
				return cita;
			}
		}
		return null;
	}

	public ArrayList<Consulta> historialClinico(Paciente paciente) {
		return paciente.consultarHistorial();
	}

	/*
	 * Función: generarReporteGeneral Argumentos: Ninguno. Objetivo: Imprimir en
	 * consola un resumen de la actividad de la clínica. (Esta función sí puede usar
	 * System.out.println). Retorno: (void): No retorna valor.
	 */
	public String generarReporteGeneral() {
		StringBuilder reporte = new StringBuilder();

		reporte.append("--- Reporte General ---\n");
		reporte.append("Total Pacientes: ").append(pacientes.size()).append("\n");
		reporte.append("Total Doctores: ").append(doctores.size()).append("\n");
		reporte.append("Total Administrativos: ").append(administrativos.size()).append("\n");
		reporte.append("Total Citas Programadas: ").append(citas.size()).append("\n");
		reporte.append("Total Consultas Atendidas: ").append(consultas.size()).append("\n");

		int totalVacunas = 0;
		for (Paciente p : pacientes) {
			totalVacunas += p.getVacunasAplicadas().size();
		}
		reporte.append("Total Vacunas Aplicadas (General): ").append(totalVacunas).append("\n");
		reporte.append("-----------------------\n");

		return reporte.toString();
	}

	public static boolean esFechaPasada(LocalDate fecha) {
		LocalDate hoy = LocalDate.now();
		return fecha.isBefore(hoy);
	}

	public static boolean sonMismoDia(LocalDate fecha1, LocalDate fecha2) {
		if (fecha1 == null || fecha2 == null) {
			return false;
		}
		return fecha1.isEqual(fecha2);
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

	/*
	 * Función: guardarDatos Argumentos: Ninguno. Objetivo: Guardar (Serializar) la
	 * instancia completa de la Clínica en un archivo binario (clinica.dat).
	 * Retorno: (void): No retorna valor.
	 */
	public void guardarDatos() {
		try {
			FileOutputStream archivoSalida = new FileOutputStream(ARCHIVO_DATOS);
			ObjectOutputStream flujoSalida = new ObjectOutputStream(archivoSalida);
			flujoSalida.writeObject(instance);
			flujoSalida.close();
			archivoSalida.close();
			System.out.println("Datos de la Clinica guardados en " + ARCHIVO_DATOS);
		} catch (IOException excepcionIO) {
			System.err.println("Error al guardar los datos: " + excepcionIO.getMessage());
		}
	}

	/*
	 * Función: cargarDatos (static) Argumentos: Ninguno. Objetivo: Cargar
	 * (Deserializar) la instancia de la Clínica desde un archivo binario
	 * (clinica.dat). Es 'private static' porque solo debe ser llamada por
	 * getInstance(). Retorno: (Clinica): La instancia cargada desde el archivo, o
	 * null si ocurre un error (Ej. archivo no encontrado).
	 */
	private static Clinica cargarDatos() {
		try {
			FileInputStream archivoEntrada = new FileInputStream(ARCHIVO_DATOS);
			ObjectInputStream flujoEntrada = new ObjectInputStream(archivoEntrada);
			Clinica instanciaCargada = (Clinica) flujoEntrada.readObject();
			flujoEntrada.close();
			archivoEntrada.close();
			return instanciaCargada;
		} catch (IOException excepcionIO) {
			System.out.println("No se encontra el archivo " + ARCHIVO_DATOS + ". Se crea uno nuevo.");
			return null;
		} catch (ClassNotFoundException excepcionClase) {
			System.err.println("Error al cargar los datos (Clase no encontrada): " + excepcionClase.getMessage());
			return null;
		}
	}
	
	// ------------- LOGIN / AUTENTICACION -------------
	/*
	 Crear usuario ADMIN por defecto si la clinica esta completamente vacia.
	 Se ejecuta cuando se carga o inicia el programa.
	 */
	public void crearAdminPorDefecto() {
	    if (administrativos.isEmpty() && doctores.isEmpty()) {
	        Administrativo admin = new Administrativo("admi", "admi","Administrador", "Administrador General");
	        administrativos.add(admin);
	        System.out.println("Usuario admin/admin creado por defecto.");
	    }
	}
	
	
	
	/*
	Retorna 1 = admin
			2 = doctor
			0 = error
	*/
	
	public int loginTipo(String usuario, String password) {

	    for (Doctor doc : doctores) {
	        if (doc.getUsuario().equalsIgnoreCase(usuario)
	                && doc.getContrasenia().equals(password)) {
	            return 2; // doctor
	        }
	    }

	    for (Administrativo admin : administrativos) {
	        if (admin.getUsuario().equalsIgnoreCase(usuario)
	                && admin.getContrasenia().equals(password)) {
	            return 1; // administrativo
	        }
	    }

	    return 0;
	}
	
}