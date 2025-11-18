package logico;

/*
 * Función: ClinicaException (Excepción Personalizada)
 * Argumentos: (String) mensaje: El mensaje de error legible para el usuario.
 * Objetivo: Crear una excepción personalizada para todos los errores de reglas de
 * negocio de la clínica.
 * Esto permite a la interfaz (consola o visual) capturar estos errores y
 * mostrarlos.
 * Retorno: Ninguno.
 */
public class ClinicaException extends Exception {

    private static final long serialVersionUID = 11L;

    public ClinicaException(String mensaje) {
        super(mensaje);
    }
}