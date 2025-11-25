package logico;

public class Administrativo extends Personal {

    private static final long serialVersionUID = 4L;
    private int idAdmin;
    private String nombre;
    private String cargo;
    private String rutaFoto;

    public Administrativo() {
        super();
        this.idAdmin = 0;
        this.nombre = null;
        this.cargo = null;
        this.rutaFoto = null;
    }

    /*
    Función: Administrativo (Constructor)
    Argumentos: 
        (String) usuario: Login.
        (String) contrasenia: Password.
        (String) nombre: Nombre real.
        (String) cargo: Puesto (ej. Secretario, Gerente).
    Objetivo: Crear un administrativo.
    Retorno: (Ninguno): Constructor.
    */
    public Administrativo(String usuario, String contrasenia, String nombre, String cargo) {
        super(usuario, contrasenia);
        this.idAdmin = 0;
        this.nombre = nombre;
        this.cargo = cargo;
    }

    @Override
    public int getId() {
        return this.idAdmin;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }
}