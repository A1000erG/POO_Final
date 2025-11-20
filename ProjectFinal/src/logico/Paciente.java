package logico;

import java.util.ArrayList;
import java.util.Objects;

public class Paciente extends Persona {

    private static final long serialVersionUID = 6L;
    private int idPaciente;
    private ArrayList<Consulta> historialClinico;
    private ArrayList<Vacuna> vacunasAplicadas;

    public Paciente() {
        super();
        this.idPaciente = 0;
        this.historialClinico = new ArrayList<Consulta>();
        this.vacunasAplicadas = new ArrayList<Vacuna>();
    }

    /*
    Función: getHistorialClinico
    Argumentos: Ninguno.
    Objetivo: Obtener la lista de consultas pasadas.
    Retorno: (ArrayList<Consulta>): La lista de consultas.
    */
    public ArrayList<Consulta> getHistorialClinico() {
        return historialClinico;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public void setHistorialClinico(ArrayList<Consulta> historialClinico) {
        this.historialClinico = historialClinico;
    }

    public ArrayList<Vacuna> getVacunasAplicadas() {
        return vacunasAplicadas;
    }

    public void setVacunasAplicadas(ArrayList<Vacuna> vacunasAplicadas) {
        this.vacunasAplicadas = vacunasAplicadas;
    }
    
    /*
    Función: equals
    Argumentos: (Object) obj: Objeto a verificar.
    Objetivo: Comparar pacientes por Cédula (Regla de Negocio).
    Retorno: (boolean): Verdadero si tienen la misma cédula.
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Paciente paciente = (Paciente) obj;
        return Objects.equals(getCedula(), paciente.getCedula());
    }
}