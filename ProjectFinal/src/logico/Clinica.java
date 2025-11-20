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
		}
		return instance;
	}

	/*
	 * Función: login Argumentos: (String) usuario: El nombre de usuario. (String)
	 * password: La contraseña. Objetivo: Autenticar a un miembro del personal
	 * (Doctor o Administrativo). Retorno: (Personal): El objeto Personal (Doctor o
	 * Admin) si el login es exitoso. Lanza: (ClinicaException): Si las credenciales
	 * son incorrectas.
	 */
	public Personal login(String usuario, String password) throws ClinicaException {
		// ESTO ES MOMENTANEO
        if (usuario.equals("admin") && password.equals("admin")) {
            Administrativo adminEstandar = new Administrativo();
            adminEstandar.setNombre("Administrador (Estándar)");
            adminEstandar.setUsuario("admin");
            adminEstandar.setCargo("Super Admin");
            return adminEstandar;
        }
		
		// Buscar en Doctores
		for (Doctor doctorActual : doctores) {
			if (doctorActual.getUsuario().equals(usuario) && doctorActual.getContrasenia().equals(password)) {
				return doctorActual; // Login exitoso
			}
		}
		// Buscar en Administrativos
		for (Administrativo adminActual : administrativos) {
			if (adminActual.getUsuario().equals(usuario) && adminActual.getContrasenia().equals(password)) {
				return adminActual; // Login exitoso
			}
		}
		// Si no se encontró a nadie, lanzar excepción
		throw new ClinicaException("Credenciales incorrectas. Verifica tus datos.");
	}

	/*
	 * Autenticar a un miembro del personal y devolver un String de su tipo.
	 */
	public String loginTipo(String usuario, String password) throws ClinicaException {
		Personal logueado = login(usuario, password);

		if (logueado instanceof Doctor) {
			return "Doctor";
		}
		if (logueado instanceof Administrativo) {
			return "Administrativo";
		}

		throw new ClinicaException("Tipo de personal desconocido.");
	}

	/*
	 * Función: registrarDoctor Argumentos: (Doctor) doctor: El objeto Doctor a
	 * registrar. Objetivo: Añadir un nuevo doctor a la lista de doctores. Retorno:
	 * (void): No retorna valor.
	 */
	public void registrarDoctor(Doctor doctor) {
		this.doctores.add(doctor);
	}

	/*
	 * Función: registrarAdministrativo Argumentos: (Administrativo) admin: El
	 * objeto Administrativo a registrar. Objetivo: Añadir un nuevo administrativo a
	 * la lista de administrativos. Retorno: (void): No retorna valor.
	 */
	public void registrarAdministrativo(Administrativo admin) {
		this.administrativos.add(admin);
	}

	/*
	 * Función: registrarPaciente Argumentos: (Paciente) paciente: El objeto
	 * Paciente a registrar. Objetivo: Añadir un nuevo paciente a la lista de
	 * pacientes y asignarle un ID único. Retorno: (void): No retorna valor.
	 */
	public void registrarPaciente(Paciente paciente) {
		paciente.setIdPaciente(this.proximoIdPaciente);
		this.proximoIdPaciente++; // Incrementar el contador
		this.pacientes.add(paciente);
	}

	/*
	 * Función: registrarSolicitante Argumentos: (Solicitante) solicitante: El
	 * objeto Solicitante a registrar. Objetivo: Añadir un nuevo solicitante a la
	 * lista de espera. Retorno: (void): No retorna valor.
	 */
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
		return null; // No se encontró
	}

	/*
	 * Función: convertirSolicitanteAPaciente Argumentos: (Solicitante) solicitante:
	 * El solicitante a convertir. Objetivo: Convertir un Solicitante en un
	 * Paciente. Retorno: (Paciente): El nuevo objeto Paciente. Lanza:
	 * (ClinicaException): Si ya existía un paciente con esa cédula.
	 */
	public Paciente convertirSolicitanteAPaciente(Solicitante solicitante) throws ClinicaException {
		Paciente pacienteExistente = getPacientePorCedula(solicitante.getCedula());
		if (pacienteExistente != null) {
			throw new ClinicaException("Error: Ya existe un paciente con la cédula " + solicitante.getCedula());
		}

		Paciente nuevoPaciente = new Paciente();
		nuevoPaciente.setCedula(solicitante.getCedula());
		nuevoPaciente.setNombre(solicitante.getNombre());
		nuevoPaciente.setFechaNacimiento(solicitante.getFechaNacimiento());
		nuevoPaciente.setSexo(solicitante.getSexo());
		nuevoPaciente.setTelefono(solicitante.getTelefono());

		registrarPaciente(nuevoPaciente);

		this.solicitantes.remove(solicitante);

		return nuevoPaciente;
	}

	/*
	 * Función: programarCita Argumentos: (Paciente) paciente: El paciente para la
	 * cita. (Doctor) doctor: El doctor para la cita. (LocalDate) fecha: La fecha
	 * deseada. (String) hora: La hora deseada. Objetivo: Crear y registrar una
	 * nueva cita, validando las reglas de negocio. Retorno: (void): No retorna
	 * valor. Lanza: (ClinicaException): Si falla alguna validación (fecha, cupo,
	 * datos).
	 */
	public void programarCita(Paciente paciente, Doctor doctor, LocalDate fecha, String hora) throws ClinicaException {
		if (paciente == null || doctor == null || fecha == null || hora == null) {
			throw new ClinicaException("Error: Faltan datos en la cita para programarla.");
		}

		if (esFechaPasada(fecha)) {
			throw new ClinicaException("Error: No se puede programar cita en una fecha pasada.");
		}

		int citasProgramadas = 0;
		for (Cita citaExistente : this.citas) {
			// Contar citas del MISMO doctor en el MISMO día
			if (citaExistente.getDoctor().equals(doctor) && citaExistente.getFecha().isEqual(fecha)) {
				citasProgramadas++;
			}
		}

		if (citasProgramadas >= doctor.getCupoDia()) {
			throw new ClinicaException("Error: El cupo del doctor " + doctor.getNombre() + " está lleno para ese día.");
		}

		Cita nuevaCita = new Cita();
		nuevaCita.setIdCita(this.proximoIdCita);
		this.proximoIdCita++;
		nuevaCita.setPaciente(paciente);
		nuevaCita.setDoctor(doctor);
		nuevaCita.setFecha(fecha);
		nuevaCita.setHora(hora);
		nuevaCita.setEstado("Pendiente"); // Estado inicial

		this.citas.add(nuevaCita);
		// No se retorna 'true', si no hubo excepción, se asume éxito.
	}

	/*
	 * Función: cancelarCita Argumentos: (Cita) cita: La cita a cancelar. Objetivo:
	 * Cambiar el estado de una cita a "Cancelada". Retorno: (void): No retorna
	 * valor. Lanza: (ClinicaException): Si la cita ya pasó.
	 */
	public void cancelarCita(Cita cita) throws ClinicaException {
		if (esFechaPasada(cita.getFecha())) {
			throw new ClinicaException("No se puede cancelar una cita pasada.");
		}
		cita.setEstado("Cancelada");
	}

	/*
	 * Función: modificarEstadoCita Argumentos: (Cita) cita: La cita a modificar.
	 * (String) nuevoEstado: El nuevo estado (Ej. "Realizada"). Objetivo: Modificar
	 * el estado de una cita. Retorno: (void): No retorna valor. Lanza:
	 * (ClinicaException): Si la cita ya está realizada o no se puede modificar.
	 */
	public void modificarEstadoCita(Cita cita, String nuevoEstado) throws ClinicaException {
		if (cita.getEstado().equals("Realizada")) {
			throw new ClinicaException("Error: No se puede modificar una cita ya realizada.");
		}
		if (esFechaPasada(cita.getFecha()) && !cita.getEstado().equals("Pendiente")) {
			throw new ClinicaException("Error: No se puede modificar una cita pasada que no esté pendiente.");
		}

		cita.setEstado(nuevoEstado);
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

		// Agregar al historial del paciente
		if (consulta.isAgregarAlHistorial() && consulta.getCitaAsociada() != null) {
			Paciente pacienteDeLaConsulta = consulta.getCitaAsociada().getPaciente();
			if (pacienteDeLaConsulta != null) {
				pacienteDeLaConsulta.getHistorialClinico().add(consulta);
			}
		}

		// Marcar la cita asociada como "Realizada"
		if (consulta.getCitaAsociada() != null) {
			try {
				// Internamente, esto puede fallar, pero para el flujo de
				// registrarConsulta, lo forzamos.
				modificarEstadoCita(consulta.getCitaAsociada(), "Realizada");
			} catch (ClinicaException e) {
				// Esto no debería pasar si la cita viene de
				// menuRegistrarConsulta,
				// pero es buena práctica manejarlo.
				System.err.println("Advertencia: " + e.getMessage());
			}
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

		this.vacunas.add(vacuna); // Añadir a la lista general
		paciente.getVacunasAplicadas().add(vacuna); // Añadir al paciente
	}

	public ArrayList<Consulta> historialClinico(Paciente paciente) {
		return paciente.consultarHistorial(); // Llama al método del paciente
	}

	/*
	 * Función: generarReporteGeneral Argumentos: Ninguno. Objetivo: Imprimir en
	 * consola un resumen de la actividad de la clínica. (Esta función sí puede usar
	 * System.out.println, es un REPORTE). Retorno: (void): No retorna valor.
	 */
	public void generarReporteGeneral() {
		System.out.println("--- Reporte General ---");
		System.out.println("Total Pacientes: " + pacientes.size());
		System.out.println("Total Doctores: " + doctores.size());
		System.out.println("Total Administrativos: " + administrativos.size());
		System.out.println("Total Citas Programadas: " + citas.size());
		System.out.println("Total Consultas Atendidas: " + consultas.size());
		System.out.println("Total Vacunas Aplicadas (General): " + vacunas.size());
		System.out.println("-----------------------");
	}

	/*
	 * Función: esFechaPasada Argumentos: (LocalDate) fecha: La fecha a verificar.
	 * Objetivo: Comprobar si una fecha es anterior al día de hoy. Es 'static' para
	 * ser adaptable (Request 3). Retorno: (boolean): true si la fecha ya pasó,
	 * false si es hoy o futura.
	 */
	public static boolean esFechaPasada(LocalDate fecha) {
		LocalDate hoy = LocalDate.now();
		return fecha.isBefore(hoy);
	}

	/*
	 * Función: sonMismoDia Argumentos: (LocalDate) fecha1: Primera fecha.
	 * (LocalDate) fecha2: Segunda fecha. Objetivo: Comprobar si dos fechas son
	 * exactamente el mismo día. Es 'static' para ser adaptable (Request 3).
	 * Retorno: (boolean): true si son el mismo día, false si no.
	 */
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

	public Cita getCitaPorId(int idCita) {
		for (Cita cita : citas) {
			if (cita.getIdCita() == idCita) {
				return cita;
			}
		}
		return null;
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
			System.out.println("No se encontró el archivo " + ARCHIVO_DATOS + ". Iniciando con datos nuevos.");
			return null;
		} catch (ClassNotFoundException excepcionClase) {
			System.err.println("Error al cargar los datos (Clase no encontrada): " + excepcionClase.getMessage());
			return null;
		}
	}
}