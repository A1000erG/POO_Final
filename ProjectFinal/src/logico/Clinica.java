package logico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    
    private int proximoIdDoctor;

    private static final String ARCHIVO_DATOS = "clinica.dat";
    
    // Variable para almacenar errores sin usar Exceptions (Transient para no serializar)
    private transient String ultimoMensajeError;

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
        this.proximoIdDoctor = 1;
        this.ultimoMensajeError = "";
    }

    /*
    Función: getInstance
    Argumentos: Ninguno.
    Objetivo: Implementar Singleton y cargar datos automáticamente.
    Retorno: (Clinica): La instancia única.
    */
    public static Clinica getInstance() {
        if (instance == null) {
            instance = cargarDatos();
            if (instance == null) {
                instance = new Clinica();
            }
        }
        return instance;
    }

    /*
    Función: login
    Argumentos: 
        (String) usuario: Login.
        (String) contrasenia: Password.
    Objetivo: Validar credenciales sin lanzar excepciones.
    Retorno: (Personal): Objeto encontrado o null si falla.
    */
    public Personal login(String usuario, String contrasenia) {
        if (usuario.equals("admin") && contrasenia.equals("admin")) {
            Administrativo adminTemp = new Administrativo();
            adminTemp.setNombre("Super Admin");
            adminTemp.setUsuario("admin");
            adminTemp.setCargo("Administrador General");
            return adminTemp;
        }

        for (Doctor doc : doctores) {
            if (doc.getUsuario().equals(usuario) && doc.getContrasenia().equals(contrasenia)) {
                return doc;
            }
        }
        for (Administrativo admin : administrativos) {
            if (admin.getUsuario().equals(usuario) && admin.getContrasenia().equals(contrasenia)) {
                return admin;
            }
        }
        
        this.ultimoMensajeError = "Credenciales incorrectas.";
        return null; 
    }

    /*
     * Función: loginTipo
     * Argumentos: (String) usuario, (String) password
     * Objetivo: Autenticar y devolver el TIPO de usuario como String.
     * Nota: Agregado para compatibilidad con tu Login.java.
     * Retorno: (String): "Doctor", "Administrativo" o null si falla.
     */
    public String loginTipo(String usuario, String password) {
        Personal logueado = login(usuario, password);

        if (logueado == null) {
            return null; // Error en login
        }
        if (logueado instanceof Doctor) {
            return "Doctor";
        }
        if (logueado instanceof Administrativo) {
            return "Administrativo";
        }
        return "Desconocido";
    }

    /*
    Función: registrarDoctor
    Argumentos: (Doctor) doctor: Objeto a guardar.
    Objetivo: Agregar doctor a la lista.
    Retorno: (void).
    */
    public void registrarDoctor(Doctor doctor) {
    	doctor.setIdDoctor(this.proximoIdDoctor);
        this.proximoIdDoctor++;
        this.doctores.add(doctor);
    }
    
    public void registrarAdministrativo(Administrativo admin) {
        this.administrativos.add(admin);
    }

    /*
    Función: registrarPaciente
    Argumentos: (Paciente) paciente: Objeto a guardar.
    Objetivo: Agregar paciente con ID autogenerado.
    Retorno: (void).
    */
    public void registrarPaciente(Paciente paciente) {
        paciente.setIdPaciente(this.proximoIdPaciente);
        this.proximoIdPaciente++;
        this.pacientes.add(paciente);
    }

    /*
    Función: registrarSolicitante
    Argumentos: (Solicitante) solicitante: Objeto a guardar.
    Objetivo: Agregar persona a la cola de espera.
    Retorno: (void).
    */
    public void registrarSolicitante(Solicitante solicitante) {
        this.solicitantes.add(solicitante);
    }
    
    public void registrarVacuna(Paciente paciente, Vacuna vacuna) {
        vacuna.setId(this.proximoIdVacuna);
        this.proximoIdVacuna++;
        this.vacunas.add(vacuna);
        paciente.getVacunasAplicadas().add(vacuna);
    }

    /*
    Función: convertirSolicitanteAPaciente
    Argumentos: (Solicitante) solicitante: Objeto origen.
    Objetivo: Promover solicitante a paciente validando duplicados.
    Retorno: (Paciente): El nuevo paciente o null si falla.
    */
    public Paciente convertirSolicitanteAPaciente(Solicitante solicitante) {
        Paciente pacienteExistente = getPacientePorCedula(solicitante.getCedula());
        
        if (pacienteExistente != null) {
            this.ultimoMensajeError = "Ya existe paciente con esa cédula.";
            return null;
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
    Función: programarCita
    Argumentos: 
        (Paciente) paciente: Quien asiste.
        (Doctor) doctor: Quien atiende.
        (LocalDate) fecha: Cuando.
        (String) hora: A qué hora.
    Objetivo: Crear cita si hay cupo y la fecha es válida.
    Retorno: (boolean): true si éxito, false si error (ver getUltimoMensajeError).
    */
    public boolean programarCita(Paciente paciente, Doctor doctor, LocalDate fecha, String hora) {
        if (paciente == null || doctor == null || fecha == null || hora == null) {
            this.ultimoMensajeError = "Datos incompletos.";
            return false;
        }

        if (esFechaPasada(fecha)) {
            this.ultimoMensajeError = "No se puede programar en el pasado.";
            return false;
        }

        int conteoCitasDia = 0;
        for (Cita cita : citas) {
            if (cita.getDoctor().equals(doctor) && 
                cita.getFecha().isEqual(fecha) &&
                !cita.getEstado().equals("Cancelada")) {
                conteoCitasDia++;
            }
        }

        if (conteoCitasDia >= doctor.getCupoDia()) {
            this.ultimoMensajeError = "El doctor no tiene cupo para este día.";
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
    
    public boolean cancelarCita(Cita cita) {
        if (esFechaPasada(cita.getFecha())) {
            this.ultimoMensajeError = "No se puede cancelar una cita pasada.";
            return false;
        }
        cita.setEstado("Cancelada");
        return true;
    }

    public boolean modificarEstadoCita(Cita cita, String nuevoEstado) {
        if (cita.getEstado().equals("Realizada")) {
            this.ultimoMensajeError = "No se puede modificar una cita ya realizada.";
            return false;
        }
        if (esFechaPasada(cita.getFecha()) && !cita.getEstado().equals("Pendiente")) {
            this.ultimoMensajeError = "No se puede modificar una cita pasada no pendiente.";
            return false;
        }
        cita.setEstado(nuevoEstado);
        return true;
    }

    /*
    Función: registrarConsulta
    Argumentos: (Consulta) consulta: La consulta realizada.
    Objetivo: Guardar consulta, actualizar historial y cerrar cita.
    Retorno: (void).
    */
    public void registrarConsulta(Consulta consulta) {
        consulta.setIdConsulta(this.proximoIdConsulta++);
        this.consultas.add(consulta);

        if (consulta.isAgregarAlHistorial() && consulta.getCitaAsociada() != null) {
            Paciente paciente = consulta.getCitaAsociada().getPaciente();
            if (paciente != null) {
                paciente.getHistorialClinico().add(consulta);
            }
        }

        if (consulta.getCitaAsociada() != null) {
            consulta.getCitaAsociada().setEstado("Realizada");
        }
    }
    
    /*
    Función: guardarDatos
    Argumentos: Ninguno.
    Objetivo: Guardar datos localmente y enviarlos al servidor de respaldo.
    Retorno: (boolean): true si el respaldo remoto funcionó.
    */
    public boolean guardarDatos() {
        // 1. Guardado local
        guardarDatosLocal();
        
        // 2. Preparar reporte texto (Puntos extra)
        StringBuilder reporte = new StringBuilder();
        reporte.append("REPORTE DE ENFERMEDADES - CLINICA\n");
        reporte.append("Fecha: ").append(LocalDate.now()).append("\n");
        // Lógica simple de conteo de diagnósticos para el reporte
        reporte.append("Total Consultas: ").append(consultas.size()).append("\n");
        
        // 3. Enviar al servidor
        File archivoLocal = new File(ARCHIVO_DATOS);
        return ClienteRespaldo.enviarRespaldo(archivoLocal, reporte.toString());
    }

    /*
    Función: guardarDatosLocal
    Argumentos: Ninguno.
    Objetivo: Serializar la clase completa.
    Retorno: (void).
    */
    public void guardarDatosLocal() {
        try {
            FileOutputStream archivoSalida = new FileOutputStream(ARCHIVO_DATOS);
            ObjectOutputStream flujoSalida = new ObjectOutputStream(archivoSalida);
            flujoSalida.writeObject(this);
            flujoSalida.close();
            archivoSalida.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Función: cargarDatos
    Argumentos: Ninguno.
    Objetivo: Deserializar la clase completa.
    Retorno: (Clinica): Instancia o null.
    */
    private static Clinica cargarDatos() {
        try {
            File archivo = new File(ARCHIVO_DATOS);
            if (!archivo.exists()) return null;
            
            FileInputStream archivoEntrada = new FileInputStream(archivo);
            ObjectInputStream flujoEntrada = new ObjectInputStream(archivoEntrada);
            Clinica instancia = (Clinica) flujoEntrada.readObject();
            flujoEntrada.close();
            archivoEntrada.close();
            return instancia;
        } catch (Exception e) {
            return null;
        }
    }
    
    
    // ------------------- METODOS PARA REGISTRAR DOCTORES Y ADMINISTRADORES -------------------
    
    public boolean existeUsuario(String usuario) {
        if (usuario == null) return false;

        for (Doctor d : doctores) {
            if (d.getUsuario() != null && d.getUsuario().equalsIgnoreCase(usuario)) {
                return true;
            }
        }
        for (Administrativo a : administrativos) {
            if (a.getUsuario() != null && a.getUsuario().equalsIgnoreCase(usuario)) {
                return true;
            }
        }
        return false;
    }
    
    
    public boolean registrarDoctorDesdeFormulario(
            String usuario,
            String contrasenia,
            String nombre,
            String especialidad,
            String cupoTexto,
            String rutaFotoOriginal
    ) {
        // 1. Validar campos vacíos
        if (usuario == null || usuario.trim().isEmpty() ||
            contrasenia == null || contrasenia.trim().isEmpty() ||
            nombre == null || nombre.trim().isEmpty() ||
            especialidad == null || especialidad.trim().isEmpty() ||
            cupoTexto == null || cupoTexto.trim().isEmpty()) {

            ultimoMensajeError = "Todos los campos son obligatorios.";
            return false;
        }

        // 2. Validar cupo
        int cupoDia;
        try {
            cupoDia = Integer.parseInt(cupoTexto.trim());
            if (cupoDia <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            ultimoMensajeError = "El cupo por día debe ser un número entero positivo.";
            return false;
        }

        // 3. Validar usuario repetido
        if (existeUsuario(usuario)) {
            ultimoMensajeError = "Ya existe un usuario con ese nombre.";
            return false;
        }

        // 4. Crear y registrar
        Doctor nuevo = new Doctor(usuario, contrasenia, nombre, especialidad, cupoDia);
        
        // --- Foto: copiar a carpeta FotosDoctores ---
        if (rutaFotoOriginal != null && !rutaFotoOriginal.trim().isEmpty()) {
            try {
                // Carpeta destino
                File carpeta = new File("FotosDoctores");
                if (!carpeta.exists()) {
                    carpeta.mkdirs();
                }

                // próximo ID para el nombre del archivo
                int idParaFoto = this.proximoIdDoctor;

                // extensión del archivo original
                String extension = obtenerExtension(rutaFotoOriginal);

                String nombreArchivo = String.format("doctor_%03d%s", idParaFoto, extension);

                File destino = new File(carpeta, nombreArchivo);

                Path origenPath = Paths.get(rutaFotoOriginal);
                Path destinoPath = destino.toPath();

                Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);

                // Guardar la ruta (relativa) en el doctor
                nuevo.setRutaFoto(destino.getPath());

            } catch (Exception e) {
                this.ultimoMensajeError = "Doctor registrado, pero no se pudo guardar la foto correctamente.";
            }
        }
        
        registrarDoctor(nuevo);
        guardarDatos();

        return true;
    }

    private String obtenerExtension(String ruta) {
        int punto = ruta.lastIndexOf('.');
        if (punto == -1) {
            return ""; // sin extensión
        }
        return ruta.substring(punto); // incluye el punto, ej: ".jpg"
    }
    
    
    
    // --- Métodos Auxiliares y Getters ---

    public static boolean esFechaPasada(LocalDate fecha) {
        return fecha.isBefore(LocalDate.now());
    }

    public Paciente getPacientePorCedula(String cedula) {
        for (Paciente p : pacientes) {
            if (p.getCedula().equals(cedula)) return p;
        }
        return null;
    }

    public Doctor getDoctorPorUsuario(String usuario) {
        for (Doctor d : doctores) {
            if (d.getUsuario().equals(usuario)) return d;
        }
        return null;
    }
    
    public int getProximoIdDoctor() {
        return proximoIdDoctor;
    }

    public String getUltimoMensajeError() {
        return ultimoMensajeError;
    }
    
    // Getters de listas (Nombres originales)
    public ArrayList<Doctor> getDoctores() { return doctores; }
    public ArrayList<Paciente> getPacientes() { return pacientes; }
    public ArrayList<Cita> getCitas() { return citas; }
    public ArrayList<Consulta> getConsultas() { return consultas; }
    public ArrayList<Vacuna> getVacunas() { return vacunas; }
    public ArrayList<Solicitante> getSolicitantes() { return solicitantes; }
    public ArrayList<Administrativo> getAdministrativos() { return administrativos; }
}