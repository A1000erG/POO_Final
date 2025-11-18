package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Vacuna implements Serializable {

	/*
	 * Enum: ViaAdministracion Objetivo: Definir un tipo de dato estricto para la
	 * vía de administración.
	 */
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

	public Vacuna() {
		this.id = 0;
		this.nombre = null;
		this.fechaCaducidad = null;
		this.cantidadDisponible = 0;
		this.efectosAdversos = "";
		this.viaAdministracion = null;
	}

	/*
	 * Función: Vacuna (Constructor con parámetros) Argumentos: (String) nombre:
	 * Nombre de la vacuna (Ej. "Pfizer"). (LocalDate) fechaCaducidad: Fecha de
	 * vencimiento del lote. (int) cantidadDisponible: Cantidad en inventario.
	 * (String) efectosAdversos: Efectos comunes. (ViaAdministracion) via: Método de
	 * aplicación. Objetivo: Inicializar un tipo de vacuna con valores específicos.
	 * Retorno: Ninguno (es un constructor).
	 */
	public Vacuna(String nombre, LocalDate fechaCaducidad, int cantidadDisponible, String efectosAdversos,
			ViaAdministracion via) {
		this.id = 0;
		this.nombre = nombre;
		this.fechaCaducidad = fechaCaducidad;
		this.cantidadDisponible = cantidadDisponible;
		this.efectosAdversos = efectosAdversos;
		this.viaAdministracion = via;
	}

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

	/*
	 * Función: equals Argumentos: (Object) obj: El objeto a comparar. Objetivo:
	 * Compara si dos tipos de vacuna son iguales basándose en su ID. Retorno:
	 * (boolean): true si son iguales, false si no.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vacuna vacuna = (Vacuna) obj;
		return id == vacuna.id && id != 0;
	}

	/*
	 * Función: hashCode Argumentos: Ninguno. Objetivo: Generar un código hash
	 * basado en el ID de la Vacuna. Retorno: (int): El código hash.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}