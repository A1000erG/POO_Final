package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Vacuna implements Serializable {

	public enum ViaAdministracion {
		INTRAMUSCULAR, ORAL, SUBCUTANEA
	};

	private static final long serialVersionUID = 7L;
	private int id;
	private String nombre;
	private LocalDate fechaCaducidad;
	private int cantidadDisponible;
	private String efectosAdversos;
	private ViaAdministracion viaAdministracion;
    // NUEVO CAMPO PARA HABILITAR/DESHABILITAR
    private boolean activo;

	public Vacuna() {
		this.id = 0;
		this.nombre = null;
		this.fechaCaducidad = null;
		this.cantidadDisponible = 0;
		this.efectosAdversos = "";
		this.viaAdministracion = null;
        this.activo = true; // Por defecto activa
	}

	public Vacuna(String nombre, LocalDate fechaCaducidad, int cantidadDisponible, String efectosAdversos,
			ViaAdministracion via) {
		this.id = 0;
		this.nombre = nombre;
		this.fechaCaducidad = fechaCaducidad;
		this.cantidadDisponible = cantidadDisponible;
		this.efectosAdversos = efectosAdversos;
		this.viaAdministracion = via;
        this.activo = true;
	}
    
    // Getters y Setters originales...

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // ... Resto de la clase igual ...
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(LocalDate fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public int getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(int cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	public String getEfectosAdversos() {
		return efectosAdversos;
	}

	public void setEfectosAdversos(String efectosAdversos) {
		this.efectosAdversos = efectosAdversos;
	}

	public ViaAdministracion getViaAdministracion() {
		return viaAdministracion;
	}

	public void setViaAdministracion(ViaAdministracion viaAdministracion) {
		this.viaAdministracion = viaAdministracion;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vacuna vacuna = (Vacuna) obj;
		return id == vacuna.id && id != 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}