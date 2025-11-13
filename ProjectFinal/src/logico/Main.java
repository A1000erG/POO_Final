package logico;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {

	public static void main(String[] args) {
		Clinica clinica = Clinica.getInstance();
		System.out.println("Prueba de Singleton: Obtenida instancia de Clinica.");

		System.out.println("\n--- Prueba de Login (Fallido) ---");
		Personal p = clinica.login("admin", "123");
		System.out.println("Usuario logueado: " + (p == null ? "Nadie" : p.getNombre()));

		System.out.println("\n--- Registrando Personal ---");

		Administrativo admin = new Administrativo();
		admin.setNombre("Admin Principal");
		admin.setCargo("Gerente");
		admin.setUsuario("admin");
		admin.setContrasenia("123");
		clinica.registrarAdministrativo(admin);
		System.out.println("Administrativo registrado: " + admin.getNombre());

		Doctor drHouse = new Doctor();
		drHouse.setNombre("Gregory House");
		drHouse.setEspecialidad("Diagnóstico");
		drHouse.setCupoDia(1);
		drHouse.setUsuario("ghouse");
		drHouse.setContrasenia("vicodin");
		clinica.registrarDoctor(drHouse);
		System.out.println("Doctor registrado: " + drHouse.getNombre());

		Doctor drWilson = new Doctor();
		drWilson.setNombre("James Wilson");
		drWilson.setEspecialidad("Oncología");
		drWilson.setCupoDia(10);
		drWilson.setUsuario("jwilson");
		drWilson.setContrasenia("friend");
		clinica.registrarDoctor(drWilson);
		System.out.println("Doctor registrado: " + drWilson.getNombre());

		System.out.println("\n--- Prueba de Login (Exitoso) ---");
		Personal adminLogueado = clinica.login("admin", "123");
		System.out.println("Usuario logueado: " + (adminLogueado == null ? "Nadie" : adminLogueado.getNombre()));

		System.out.println("\n--- Prueba de Flujo de Paciente ---");
		Solicitante sol = new Solicitante();
		sol.setCedula("123456");
		sol.setNombre("Juan Perez (Solicitante)");
		sol.setFechaNacimiento(new GregorianCalendar(1990, Calendar.JANUARY, 1).getTime());
		sol.setSexo('M');
		clinica.registrarSolicitante(sol);
		System.out.println("Solicitante registrado.");

		Paciente pacienteJuan = clinica.convertirSolicitanteAPaciente(sol);
		System.out.println("Solicitante convertido a Paciente: " + pacienteJuan.getNombre() + " (ID: "
				+ pacienteJuan.getIdPaciente() + ")");

		System.out.println("\n--- Prueba de Citas (Cupo) ---");
		Date hoy = new Date();

		boolean cita1 = clinica.programarCita(pacienteJuan, drHouse, hoy, "09:00");
		System.out.println("Programando cita 1 con Dr. House (cupo 1): " + (cita1 ? "ÉXITO" : "FALLO"));

		boolean cita2 = clinica.programarCita(pacienteJuan, drHouse, hoy, "10:00");
		System.out.println("Programando cita 2 con Dr. House (cupo 1): " + (cita2 ? "ÉXITO" : "FALLO"));

		boolean cita3 = clinica.programarCita(pacienteJuan, drWilson, hoy, "09:00");
		System.out.println("Programando cita 3 con Dr. Wilson (cupo 10): " + (cita3 ? "ÉXITO" : "FALLO"));

		System.out.println("\n--- Prueba de Consulta e Historial ---");
		System.out.println("Historial de " + pacienteJuan.getNombre() + " ANTES: "
				+ clinica.historialClinico(pacienteJuan).size() + " entradas.");

		Cita citaParaConsulta = clinica.verCitas().get(0);

		Consulta consulta = new Consulta();
		consulta.setCitaAsociada(citaParaConsulta);
		consulta.setDoctor(citaParaConsulta.getDoctor());
		consulta.setFecha(hoy);
		consulta.setSintomas("Dolor de cabeza, sarcasmo.");
		consulta.setDiagnostico("Lupus (Es broma).");
		consulta.setAgregarAlHistorial(true);

		clinica.registrarConsulta(consulta);
		System.out.println("Consulta registrada. Estado de la cita asociada: " + citaParaConsulta.getEstado());
		System.out.println("Historial de " + pacienteJuan.getNombre() + " DESPUÉS: "
				+ clinica.historialClinico(pacienteJuan).size() + " entradas.");

		System.out.println("\n--- Prueba de Vacunación ---");
		System.out.println(
				"Vacunas de " + pacienteJuan.getNombre() + " ANTES: " + pacienteJuan.getVacunasAplicadas().size());

		Vacuna v = new Vacuna();
		v.setNombre("COVID-19");
		v.setNumDosis(1);
		v.setFechaAplicacion(hoy);

		clinica.registrarVacuna(pacienteJuan, v);
		System.out.println("Vacuna registrada.");
		System.out.println(
				"Vacunas de " + pacienteJuan.getNombre() + " DESPUÉS: " + pacienteJuan.getVacunasAplicadas().size());

		System.out.println("\n--- Prueba de Reporte ---");
		clinica.generarReporteGeneral();
	}
}