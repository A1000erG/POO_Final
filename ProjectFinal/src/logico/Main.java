package logico;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

	private static Scanner scanner = new Scanner(System.in);
	private static Clinica clinica;

	public static void main(String[] args) {

		System.out.println("Iniciando sistema de ClÃ­nica...");
		clinica = Clinica.getInstance();

		seedDataIfNeeded();

		iniciarMenuInteractivo();

		scanner.close();
		System.out.println("Programa terminado.");
	}

	/*
	 * FunciÃ³n: seedDataIfNeeded Argumentos: Ninguno (usa la variable 'clinica'
	 * global). Objetivo: Revisar si las listas de personal y pacientes estÃ¡n
	 * vacÃ­as. Si lo estÃ¡n, agrega datos de demostraciÃ³n (Dr. House, Juan Perez)
	 * para facilitar las pruebas. Retorno: (void): No retorna valor.
	 */
	private static void seedDataIfNeeded() {
		boolean datosNuevos = false;

		if (clinica.getDoctores().isEmpty() && clinica.getAdministrativos().isEmpty()) {
			System.out.println("No se detecta personal. Registrando personal de demostraciÃ³n...");

			Doctor drHouse = new Doctor("Dr. Gregory House", "dr.house", "lupus", "Nefrologa e Infectologa", 0);
			//drHouse.setNombre("Dr. Gregory House");
			//drHouse.setUsuario("dr.house");
			//drHouse.setContrasenia("lupus");
			//drHouse.setEspecialidad("NefrologÃ­a e InfectologÃ­a");
			drHouse.setCupoDia(5);
			clinica.registrarDoctor(drHouse);

			Administrativo adminLisa = new Administrativo("Lisa Cuddy", "admin.cuddy", "gestion123", "Directora");
//			adminLisa.setNombre("Lisa Cuddy");
//			adminLisa.setUsuario("admin.cuddy");
//			adminLisa.setContrasenia("gestion123");
//			adminLisa.setCargo("Directora");
			clinica.registrarAdministrativo(adminLisa);
			datosNuevos = true;
		}

		if (clinica.getPacientes().isEmpty() && clinica.getSolicitantes().isEmpty()) {
			System.out.println("No se detectaron pacientes. Registrando paciente de demostraciÃ³n...");

			Solicitante solicitanteNuevo = new Solicitante();
			solicitanteNuevo.setCedula("001-001001-1");
			solicitanteNuevo.setNombre("Juan Perez");
			solicitanteNuevo.setFechaNacimiento(LocalDate.of(1990, 5, 15));
			solicitanteNuevo.setSexo('M');
			solicitanteNuevo.setTelefono("809-123-4567");

			clinica.registrarSolicitante(solicitanteNuevo);
			clinica.convertirSolicitanteAPaciente(solicitanteNuevo);
			datosNuevos = true;
		}

		if (datosNuevos) {
			System.out.println("Datos de demostraciÃ³n cargados.");
			clinica.guardarDatos();
		} else {
			System.out.println("Datos existentes cargados correctamente.");
		}
	}

	/*
	 * FunciÃ³n: iniciarMenuInteractivo Argumentos: Ninguno. Objetivo: Mostrar el
	 * menÃº principal y gestionar la navegaciÃ³n del usuario. Contiene el bucle
	 * principal de la aplicaciÃ³n. Retorno: (void): No retorna valor.
	 */
	private static void iniciarMenuInteractivo() {
		boolean salir = false;

		while (!salir) {
			mostrarMenuPrincipal();
			int opcion = leerEntero("Seleccione una opciÃ³n: ");

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
				System.out.println("Â¡Hasta luego!");
				salir = true;
				break;
			default:
				System.out.println("OpciÃ³n no vÃ¡lida. Intente de nuevo.");
				break;
			}
			if (!salir) {
				System.out.println("\n(Presione Enter para continuar...)");
				scanner.nextLine();
			}
		}
	}

	/*
	 * FunciÃ³n: mostrarMenuPrincipal Argumentos: Ninguno. Objetivo: Imprimir las
	 * opciones del menÃº principal en la consola. Retorno: (void): No retorna valor.
	 */
	private static void mostrarMenuPrincipal() {
		System.out.println("\n--- BIENVENIDO A LA CLÃ�NICA ---");
		System.out.println("1. Iniciar SesiÃ³n (Login)");
		System.out.println("2. Registrar Nuevo Solicitante (Paciente)");
		System.out.println("3. Programar Nueva Cita");
		System.out.println("4. Registrar Consulta (Marcar cita como Realizada)");
		System.out.println("5. Ver Reporte General");
		System.out.println("6. Convertir Solicitante a Paciente");
		System.out.println("---------------------------------");
		System.out.println("0. Guardar y Salir");
	}

	/*
	 * FunciÃ³n: menuLogin Argumentos: Ninguno. Objetivo: Solicitar usuario y
	 * contraseÃ±a e intentar autenticar. Retorno: (void): No retorna valor.
	 */
	private static void menuLogin() {
		System.out.println("\n--- Iniciar SesiÃ³n ---");
		System.out.print("Usuario: ");
		//String usuario = scanner.nextLine();
		System.out.print("ContraseÃ±a: ");
		//String contrasena = scanner.nextLine();

		/*Personal personalLogueado = clinica.loginTipo(usuario, contrasena);

		if (personalLogueado != null) {
			System.out.println("Login exitoso. Bienvenido: " + personalLogueado.getNombre());
		} else {
			System.out.println("Error: Usuario o contraseÃ±a incorrectos.");
		}*/
	}

	/*
	 * FunciÃ³n: menuRegistrarSolicitante Argumentos: Ninguno. Objetivo: Pedir los
	 * datos para crear un nuevo Solicitante. Retorno: (void): No retorna valor.
	 */
	private static void menuRegistrarSolicitante() {
		System.out.println("\n--- Registrar Nuevo Solicitante ---");
		Solicitante nuevoSolicitante = new Solicitante();

		System.out.print("CÃ©dula (Ej: 001-001001-1): ");
		nuevoSolicitante.setCedula(scanner.nextLine());

		if (clinica.getPacientePorCedula(nuevoSolicitante.getCedula()) != null) {
			System.out.println("Error: Ya existe un paciente con esa cÃ©dula.");
			return;
		}

		System.out.print("Nombre completo: ");
		nuevoSolicitante.setNombre(scanner.nextLine());

		LocalDate fechaNac = leerFecha("Fecha de Nacimiento (YYYY-MM-DD): ");
		if (fechaNac == null)
			return;
		nuevoSolicitante.setFechaNacimiento(fechaNac);

		System.out.print("Sexo (M/F): ");
		nuevoSolicitante.setSexo(scanner.nextLine().charAt(0));

		System.out.print("TelÃ©fono: ");
		nuevoSolicitante.setTelefono(scanner.nextLine());

		clinica.registrarSolicitante(nuevoSolicitante);
		System.out.println("Â¡Solicitante registrado con Ã©xito!");
		System.out.println("Recuerde 'Convertir Solicitante a Paciente' (OpciÃ³n 6) para activar la cuenta.");
	}

	/*
	 * FunciÃ³n: menuProgramarCita Argumentos: Ninguno. Objetivo: Pedir los datos
	 * necesarios para registrar una nueva cita. Retorno: (void): No retorna valor.
	 */
	private static void menuProgramarCita() {
		System.out.println("\n--- Programar Nueva Cita ---");

		System.out.print("CÃ©dula del Paciente: ");
		String cedula = scanner.nextLine();
		Paciente paciente = clinica.getPacientePorCedula(cedula);

		if (paciente == null) {
			System.out.println("Error: No se encontrÃ³ un paciente con esa cÃ©dula.");
			System.out.println("AsegÃºrese de que el solicitante haya sido 'Convertido a Paciente'.");
			return;
		}

		System.out.print("Usuario (login) del Doctor (Ej: dr.house): ");
		String userDoctor = scanner.nextLine();
		Doctor doctor = clinica.getDoctorPorUsuario(userDoctor);

		if (doctor == null) {
			System.out.println("Error: No se encontrÃ³ un doctor con ese usuario.");
			return;
		}

		LocalDate fechaCita = leerFecha("Fecha de la Cita (YYYY-MM-DD): ");
		if (fechaCita == null)
			return;

		System.out.print("Hora de la Cita (HH:MM): ");
		String horaCita = scanner.nextLine();

		boolean exito = clinica.programarCita(paciente, doctor, fechaCita, horaCita);

		if (exito) {
			System.out.println("Â¡Cita programada con Ã©xito!");
		} else {
			System.out.println("No se pudo programar la cita. Revise los errores en la consola.");
		}
	}

	/*
	 * FunciÃ³n: menuRegistrarConsulta Argumentos: Ninguno. Objetivo: Pedir los datos
	 * para registrar una consulta (diagnÃ³stico) y marcar la cita como "Realizada".
	 * Retorno: (void): No retorna valor.
	 */
	private static void menuRegistrarConsulta() {
		System.out.println("\n--- Registrar Consulta ---");

		int idCita = leerEntero("ID de la Cita a la que corresponde esta consulta: ");
		Cita citaAsociada = clinica.getCitaPorId(idCita);

		if (citaAsociada == null) {
			System.out.println("Error: No se encontrÃ³ una cita con ese ID.");
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

		System.out.print("SÃ­ntomas del paciente: ");
		nuevaConsulta.setSintomas(scanner.nextLine());

		System.out.print("DiagnÃ³stico del doctor: ");
		nuevaConsulta.setDiagnostico(scanner.nextLine());

		System.out.print("Â¿Agregar al historial del paciente? (S/N): ");
		boolean agregar = scanner.nextLine().equalsIgnoreCase("S");
		nuevaConsulta.setAgregarAlHistorial(agregar);

		clinica.registrarConsulta(nuevaConsulta);
		System.out.println("Â¡Consulta registrada con Ã©xito!");
		System.out.println("La cita ID " + citaAsociada.getIdCita() + " ha sido marcada como 'Realizada'.");
	}

	/*
	 * FunciÃ³n: menuConvertirSolicitante Argumentos: Ninguno. Objetivo: Busca un
	 * solicitante por cÃ©dula y lo convierte en Paciente. Retorno: (void): No
	 * retorna valor.
	 */
	private static void menuConvertirSolicitante() {
		System.out.println("\n--- Convertir Solicitante a Paciente ---");
		System.out.print("CÃ©dula del Solicitante a convertir: ");
		String cedula = scanner.nextLine();

		Solicitante solicitante = null;
		for (Solicitante s : clinica.getSolicitantes()) {
			if (s.getCedula().equals(cedula)) {
				solicitante = s;
				break;
			}
		}

		if (solicitante == null) {
			System.out.println("Error: No se encontrÃ³ un solicitante con esa cÃ©dula.");
			return;
		}

		Paciente nuevoPaciente = clinica.convertirSolicitanteAPaciente(solicitante);

		if (nuevoPaciente != null) {
			System.out.println("Â¡ConversiÃ³n exitosa!");
			System.out.println(
					"Paciente " + nuevoPaciente.getNombre() + " (ID: " + nuevoPaciente.getIdPaciente() + ") activado.");
		} else {
			System.out.println("Error en la conversiÃ³n (revise si el paciente ya existÃ­a).");
		}
	}

	/*
	 * FunciÃ³n: leerEntero Argumentos: (String) mensaje: El mensaje a mostrar al
	 * usuario. Objetivo: Pedir un nÃºmero al usuario y asegurarse de que sea un
	 * entero vÃ¡lido. Retorno: (int): El nÃºmero entero leÃ­do.
	 */
	private static int leerEntero(String mensaje) {
		while (true) {
			try {
				System.out.print(mensaje);
				return Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Error: Debe ingresar un nÃºmero entero. Intente de nuevo.");
			}
		}
	}

	/*
	 * FunciÃ³n: leerFecha Argumentos: (String) mensaje: El mensaje a mostrar al
	 * usuario. Objetivo: Pedir una fecha al usuario y asegurarse de que tenga el
	 * formato YYYY-MM-DD. Retorno: (LocalDate): El objeto de fecha.
	 */
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