package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA DE GESTIÓN CLÍNICA (DEMO) ===");
        
        // ---------------------------------------------------------
        // PASO 1: LEVANTAR SERVIDOR DE RESPALDO (BACKEND)
        // ---------------------------------------------------------
        ServidorRespaldo servidor = new ServidorRespaldo(7001);
        servidor.start();
        try { Thread.sleep(1000); } catch (InterruptedException e) {} // Esperar a que suba
        System.out.println("[OK] Servidor de respaldo escuchando en puerto 7001.\n");

        // ---------------------------------------------------------
        // PASO 2: CARGAR INSTANCIA DE CLÍNICA
        // ---------------------------------------------------------
        Clinica clinica = Clinica.getInstance();
        System.out.println("[OK] Clínica cargada.");
        
        // Verificar Catálogo de Enfermedades
        ArrayList<Enfermedad> enfermedades = clinica.getCatalogoEnfermedades();
        System.out.println("     Catálogo cargado con " + enfermedades.size() + " enfermedades.");

        // ---------------------------------------------------------
        // PASO 3: CREACIÓN DE DATOS MAESTROS (DOCTOR Y PACIENTE)
        // ---------------------------------------------------------
        // Limpiamos listas para la prueba si es necesario, o verificamos si existen
        if (clinica.getDoctores().isEmpty()) {
            System.out.println("     Creando datos de prueba...");
            
            // Registrar Doctor
            Doctor doctor = new Doctor("drhouse", "1234", "Dr. Gregory House", "Diagnóstico", 10);
            clinica.registrarDoctor(doctor);
            
            // Registrar Solicitante y convertir a Paciente
            Solicitante solicitante = new Solicitante("001-0000-1", "Pepe Paciente", 'M', LocalDate.of(1995, 5, 20), "555-0101");
            clinica.registrarSolicitante(solicitante);
            clinica.convertirSolicitanteAPaciente(solicitante);
        }
        
        Doctor miDoctor = clinica.getDoctores().get(0);
        Paciente miPaciente = clinica.getPacientes().get(0);

        // ---------------------------------------------------------
        // PASO 4: FLUJO DE CITA
        // ---------------------------------------------------------
        System.out.println("\n[PASO 4] Programando Cita...");
        // Programamos cita para mañana
        boolean resultadoCita = clinica.programarCita(miPaciente, miDoctor, LocalDate.now().plusDays(1), "10:00 AM");
        
        if (resultadoCita) {
            System.out.println("[OK] Cita programada exitosamente.");
        } else {
            System.err.println("[ERROR] " + clinica.getUltimoMensajeError());
            return; // Detener prueba si falla
        }
        
        Cita miCita = clinica.getCitas().get(clinica.getCitas().size() - 1); // Obtener la última cita

        // ---------------------------------------------------------
        // PASO 5: SIMULACIÓN DE CONSULTA Y DIAGNÓSTICO (¡NUEVO!)
        // ---------------------------------------------------------
        System.out.println("\n[PASO 5] Realizando Consulta y Diagnóstico...");
        
        Consulta nuevaConsulta = new Consulta();
        nuevaConsulta.setCitaAsociada(miCita);
        nuevaConsulta.setDoctor(miDoctor);
        nuevaConsulta.setSintomas("Fiebre alta, dolor retroocular, dolor muscular.");
        
        // --- AQUÍ LA MAGIA DEL DIAGNÓSTICO ---
        // 1. Buscamos una enfermedad "PELIGROSA" en el catálogo (ej. Dengue)
        Enfermedad enfermedadDetectada = null;
        for (Enfermedad enf : clinica.getCatalogoEnfermedades()) {
            if (enf.getNombre().equalsIgnoreCase("Dengue")) {
                enfermedadDetectada = enf;
                break;
            }
        }
        
        if (enfermedadDetectada != null) {
            System.out.println("     Médico selecciona enfermedad: " + enfermedadDetectada.getNombre());
            System.out.println("     ¿Es vigilada?: " + enfermedadDetectada.isEsBajoVigilancia());

            // 2. Creamos el objeto Diagnostico
            Diagnostico nuevoDiagnostico = new Diagnostico(
                "Paciente requiere hidratación inmediata. Monitorear plaquetas.", 
                enfermedadDetectada, 
                Diagnostico.TipoSeveridad.GRAVE
            );
            
            // 3. Asignamos a la consulta (ESTO DEBE DISPARAR EL ALERT EN CONSOLA)
            System.out.println("     >>> Asignando diagnóstico...");
            nuevaConsulta.setDiagnostico(nuevoDiagnostico); 
            // ^^^^ Fíjate en la consola justo después de esta línea ^^^^
            
            // 4. Registrar la consulta en el sistema
            clinica.registrarConsulta(nuevaConsulta);
            System.out.println("[OK] Consulta registrada y cita cerrada.");

        } else {
            System.err.println("[ERROR] No se encontró la enfermedad 'Dengue' en el catálogo para la prueba.");
        }

        // ---------------------------------------------------------
        // PASO 6: RESPALDO DEL SISTEMA
        // ---------------------------------------------------------
        System.out.println("\n[PASO 6] Ejecutando Respaldo del Sistema...");
        boolean respaldoOk = clinica.guardarDatos();
        
        if (respaldoOk) {
            System.out.println("[OK] Respaldo enviado correctamente al Servidor (Archivos .dat y .txt generados).");
            System.out.println("     Revise la carpeta del proyecto para ver 'reporte_enfermedades.txt'.");
        } else {
            System.out.println("[ERROR] Falló la conexión con el servidor de respaldo.");
        }

        System.out.println("\n=== PRUEBA FINALIZADA CON ÉXITO ===");
        System.exit(0);
    }
}