package logico;

import java.time.LocalDate;

public class Solicitante extends Persona {

    private static final long serialVersionUID = 5L;

    public Solicitante() {
        super();
    }

    /*
    Función: Solicitante (Constructor)
    Argumentos: 
        (String) cedula: ID legal.
        (String) nombre: Nombre.
        (char) sexo: 'M' o 'F'.
        (LocalDate) fechaNacimiento: Fecha nac.
        (String) telefono: Teléfono.
    Objetivo: Crear una persona que solicita ingreso a la clínica.
    Retorno: (Ninguno): Constructor.
    */
    public Solicitante(String cedula, String nombre, char sexo, LocalDate fechaNacimiento, String telefono) {
        super(cedula, nombre, sexo, fechaNacimiento, telefono);
    }
}