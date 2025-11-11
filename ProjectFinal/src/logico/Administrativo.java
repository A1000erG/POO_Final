package logico;

public class Administrativo extends Personal {

	private String cargo;

	public Administrativo(int id, String nombre, String usuario, String contrasena, String cargo) {
		super(id, nombre, usuario, contrasena);
		this.cargo = cargo;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
}