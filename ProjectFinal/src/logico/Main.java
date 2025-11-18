package logico;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

	private static Scanner scanner = new Scanner(System.in);
	private static Clinica clinica;

	public static void main(String[] args) {

		System.out.println("Iniciando sistema de Clínica...");
		clinica = Clinica.getInstance();
		seedDataIfNeeded();
		iniciarMenuInteractivo();
		scanner.close();
		System.out.println("Programa terminado.");
	}

	private static void seedDataIfNeeded() {
		boolean datosNuevos = false;

		if (clinica.getDoctores().isEmpty() && clinica.getAdministrativos().isEmpty()) {
			System.out.println("No se detectó personal. Registrando personal de demostración...");

			Doctor drHouse = new Doctor();
			drHouse.setNombre("Dr. Gregory House");
			drHouse.setUsuario("dr.house");
			drHouse.setContrasenia("lupus");
			drHouse.setEspecialidad("Nefrología e Infectología");
			drHouse.setCupoDia(5); // 5 citas por día
			clinica.registrarDoctor(drHouse);

			Administrativo adminLisa = new Administrativo();
			adminLisa.setNombre("Lisa Cuddy");
			adminLisa.setUsuario("admin.cuddy");
			adminLisa.setContrasenia("gestion123");
			adminLisa.setCargo("Directora");
			clinica.registrarAdministrativo(adminLisa);
			datosNuevos = true;
		}

		if (clinica.getPacientes().isEmpty() && clinica.getSolicitantes().isEmpty()) {
			System.out.println("No se detectaron pacientes. Registrando paciente de demostración...");

			Solicitante solicitanteNuevo = new Solicitante();
			solicitanteNuevo.setCedula("001-001001-1");
			solicitanteNuevo.setNombre("Juan Perez");
			solicitanteNuevo.setFechaNacimiento(LocalDate.of(1990, 5, 15));
			solicitanteNuevo.setSexo('M');
			solicitanteNuevo.setTelefono("809-123-4567");

			clinica.registrarSolicitante(solicitanteNuevo);

			try {
				clinica.convertirSolicitanteAPaciente(solicitanteNuevo);
			} catch (ClinicaException e) {
				System.err.println("Error inesperado al crear datos de demo: " + e.getMessage());
			}
			datosNuevos = true;
		}

		if (datosNuevos) {
			System.out.println("Datos de demostración cargados.");
			clinica.guardarDatos();
		} else {
			System.out.println("Datos existentes cargados correctamente.");
		}
	}

	private static void iniciarMenuInteractivo() {
		boolean salir = false;

		while (!salir) {
			mostrarMenuPrincipal();
			int opcion = leerEntero("Seleccione una opción: ");

			switch (opcion) {
			case 1:
				menuLogin();
				break;
			case 2:
				menuRegistrarSolicitante();
				break;
			case 3:
				menuProgramarCita();
				break;
			case 4:
				menuRegistrarConsulta();
				break;
			case 5:
				clinica.generarReporteGeneral();
				break;
			case 6:
				menuConvertirSolicitante();
				break;
			case 0:
				System.out.println("Guardando datos antes de salir...");
				clinica.guardarDatos();
				System.out.println("¡Hasta luego!");
				salir = true;
				break;
			default:
				System.out.println("Opción no válida. Intente de nuevo.");
				break;
			}
			if (!salir) {
				System.out.println("\n(Presione Enter para continuar...)");
				scanner.nextLine(); // Pausa
			}
		}
	}

	private static void mostrarMenuPrincipal() {
		System.out.println("\n--- BIENVENIDO A LA CLÍNICA ---");
		System.out.println("1. Iniciar Sesión (Login)");
		System.out.println("2. Registrar Nuevo Solicitante (Paciente)");
		System.out.println("3. Programar Nueva Cita");
		System.out.println("4. Registrar Consulta (Marcar cita como Realizada)");
		System.out.println("5. Ver Reporte General");
		System.out.println("6. Convertir Solicitante a Paciente");
		System.out.println("---------------------------------");
		System.out.println("0. Guardar y Salir");
	}

	private static void menuLogin() {
		System.out.println("\n--- Iniciar Sesión ---");
		System.out.print("Usuario: ");
		String usuario = scanner.nextLine();
		System.out.print("Contraseña: ");
		String contrasena = scanner.nextLine();

		try {
			Personal personalLogueado = clinica.login(usuario, contrasena);
			System.out.println("Login exitoso. Bienvenido: " + personalLogueado.getNombre());
		} catch (ClinicaException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void menuRegistrarSolicitante() {
		System.out.println("\n--- Registrar Nuevo Solicitante ---");

		System.out.print("Cédula (Ej: 001-001001-1): ");
		String cedula = scanner.nextLine();

		if (clinica.getPacientePorCedula(cedula) != null) {
			System.out.println("Error: Ya existe un paciente con esa cédula.");
			return;
		}

		Solicitante nuevoSolicitante = new Solicitante();
		nuevoSolicitante.setCedula(cedula);

		System.out.print("Nombre completo: ");
		nuevoSolicitante.setNombre(scanner.nextLine());

		LocalDate fechaNac = leerFecha("Fecha de Nacimiento (YYYY-MM-DD): ");
		if (fechaNac == null)
			return; // Error en la fecha
		nuevoSolicitante.setFechaNacimiento(fechaNac);

		System.out.print("Sexo (M/F): ");
		nuevoSolicitante.setSexo(scanner.nextLine().charAt(0));

		System.out.print("Teléfono: ");
		nuevoSolicitante.setTelefono(scanner.nextLine());

		clinica.registrarSolicitante(nuevoSolicitante);
		System.out.println("¡Solicitante registrado con éxito!");
		System.out.println("Recuerde 'Convertir Solicitante a Paciente' (Opción 6) para activar la cuenta.");
	}

	private static void menuProgramarCita() {
		System.out.println("\n--- Programar Nueva Cita ---");

		try {
			System.out.print("Cédula del Paciente: ");
			String cedula = scanner.nextLine();
			Paciente paciente = clinica.getPacientePorCedula(cedula);

			if (paciente == null) {
				System.out.println("Error: No se encontró un paciente con esa cédula.");
				System.out.println("Asegúrese de que el solicitante haya sido 'Convertido a Paciente'.");
				return;
			}

			System.out.print("Usuario (login) del Doctor (Ej: dr.house): ");
			String userDoctor = scanner.nextLine();
			Doctor doctor = clinica.getDoctorPorUsuario(userDoctor);

			if (doctor == null) {
				System.out.println("Error: No se encontró un doctor con ese usuario.");
				return;
			}

			LocalDate fechaCita = leerFecha("Fecha de la Cita (YYYY-MM-DD): ");
			if (fechaCita == null)
				return;

			System.out.print("Hora de la Cita (HH:MM): ");
			String horaCita = scanner.nextLine();

			clinica.programarCita(paciente, doctor, fechaCita, horaCita);

			System.out.println("¡Cita programada con éxito!");

		} catch (ClinicaException e) {
			System.out.println("No se pudo programar la cita: " + e.getMessage());
		}
	}

	private static void menuRegistrarConsulta() {
		System.out.println("\n--- Registrar Consulta ---");

		try {
			int idCita = leerEntero("ID de la Cita a la que corresponde esta consulta: ");
			Cita citaAsociada = clinica.getCitaPorId(idCita);

			if (citaAsociada == null) {
				System.out.println("Error: No se encontró una cita con ese ID.");
				return;
			}

			if (citaAsociada.getEstado().equals("Realizada")) {
				System.out.println("Error: Esta cita ya fue marcada como Realizada.");
				return;
			}

			System.out.println("Paciente: " + citaAsociada.getPaciente().getNombre());
			System.out.println("Doctor: " + citaAsociada.getDoctor().getNombre());

			Consulta nuevaConsulta = new Consulta();
			nuevaConsulta.setCitaAsociada(citaAsociada);
			nuevaConsulta.setDoctor(citaAsociada.getDoctor());
			nuevaConsulta.setFecha(LocalDate.now());

			System.out.print("Síntomas del paciente: ");
			nuevaConsulta.setSintomas(scanner.nextLine());

			System.out.print("Diagnóstico del doctor: ");
			nuevaConsulta.setDiagnostico(scanner.nextLine());

			System.out.print("¿Agregar al historial del paciente? (S/N): ");
			boolean agregar = scanner.nextLine().equalsIgnoreCase("S");
			nuevaConsulta.setAgregarAlHistorial(agregar);

			clinica.registrarConsulta(nuevaConsulta);

			System.out.println("¡Consulta registrada con éxito!");
			System.out.println("La cita ID " + citaAsociada.getIdCita() + " ha sido marcada como 'Realizada'.");

		} catch (Exception e) {
			System.out.println("Error al registrar la consulta: " + e.getMessage());
		}
	}

	private static void menuConvertirSolicitante() {
		System.out.println("\n--- Convertir Solicitante a Paciente ---");

		try {
			System.out.print("Cédula del Solicitante a convertir: ");
			String cedula = scanner.nextLine();

			Solicitante solicitante = null;
			for (Solicitante s : clinica.getSolicitantes()) {
				if (s.getCedula().equals(cedula)) {
					solicitante = s;
					break;
				}
			}

			if (solicitante == null) {
				System.out.println("Error: No se encontró un solicitante con esa cédula.");
				return;
			}

			Paciente nuevoPaciente = clinica.convertirSolicitanteAPaciente(solicitante);

			System.out.println("¡Conversión exitosa!");
			System.out.println(
					"Paciente " + nuevoPaciente.getNombre() + " (ID: " + nuevoPaciente.getIdPaciente() + ") activado.");

		} catch (ClinicaException e) {
			System.out.println("Error en la conversión: " + e.getMessage());
		}
	}

	private static int leerEntero(String mensaje) {
		while (true) {
			try {
				System.out.print(mensaje);
				return Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Error: Debe ingresar un número entero. Intente de nuevo.");
			}
		}
	}

	private static LocalDate leerFecha(String mensaje) {
		while (true) {
			try {
				System.out.print(mensaje);
				return LocalDate.parse(scanner.nextLine());
			} catch (DateTimeParseException e) {
				System.out.println("Error: Formato de fecha incorrecto. Use YYYY-MM-DD. Intente de nuevo.");
			}
		}
	}
}