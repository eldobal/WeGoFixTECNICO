package cl.inacap.wegofixtecnico.Clases;

import org.json.JSONException;
import org.json.JSONObject;


public class Usuario {

    private String Usuario;
    private String Contrasena;
    private String Nombre;
    private String Apellidos;
    private int Telefono;
    private String Correo;
    private String Direccion;
    private int Tipousuario;
    private int Estadousuario;

    public Usuario(){}

    public Usuario(String Usuario, String Contrasena, String Nombre, String Apellidos, int Telefono, String Correo, String Direccion, int Tipousuario, int Estadousuario) {
    this.Usuario = Usuario;
    this.Contrasena =Contrasena;
    this.Nombre =Nombre;
    this.Apellidos=Apellidos;
    this.Telefono=Telefono;
    this.Correo=Correo;
    this.Direccion=Direccion;
    this.Tipousuario=Tipousuario;
    this.Estadousuario=Estadousuario;
    }


    public Usuario(JSONObject objetoJSON)throws JSONException {
        Usuario = objetoJSON.getString("Usuario");
        Contrasena = objetoJSON.getString("Contrasena");
        Nombre= objetoJSON.getString("Nombre");
        Apellidos=objetoJSON.getString("Apellidos");
        Telefono=objetoJSON.getInt("Telefono");
        Correo=objetoJSON.getString("Correo");
        Direccion=objetoJSON.getString("Direccion");
        Tipousuario=objetoJSON.getInt("Tipousuario");
        Estadousuario=objetoJSON.getInt("Estadousuario");
    }


    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getContraseña() {
        return Contrasena;
    }

    public void setContraseña(String contraseña) {
        Contrasena = contraseña;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellidos;
    }

    public void setApellido(String apellido) {
        Apellidos = apellido;
    }

    public int getTelefono() {
        return Telefono;
    }

    public void setTelefono(int telefono) {
        Telefono = telefono;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public int getTipousuario() {
        return Tipousuario;
    }

    public void setTipousuario(int tipousuario) {
        Tipousuario = tipousuario;
    }

    public int getEstadousuario() {
        return Estadousuario;
    }

    public void setEstadousuario(int estadousuario) {
        Estadousuario = estadousuario;
    }
}
