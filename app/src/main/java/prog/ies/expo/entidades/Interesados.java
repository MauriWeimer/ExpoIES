package prog.ies.expo.entidades;

public class Interesados {
    private String nombre;
    private String apellido;
    private String dni;
    private String provincia;
    private String ciudad;
    private String cel;
    private String correoElectronico;
    private String carrera;
    private String comoSeEntero;
    private String notificarme;
    private String observaciones;

    public Interesados(String nombre, String apellido, String dni, String provincia, String ciudad, String cel, String correoElectronico, String carrera, String comoSeEntero, String notificarme, String observaciones) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.cel = cel;
        this.correoElectronico = correoElectronico;
        this.carrera = carrera;
        this.comoSeEntero = comoSeEntero;
        this.notificarme = notificarme;
        this.observaciones = observaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDNI() { return dni; }

    public String getProvincia() { return provincia; }

    public String getCiudad() { return ciudad; }

    public String getCel() { return cel; }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getCarrera() {
        return carrera;
    }

    public String getComoSeEntero() {
        return comoSeEntero;
    }

    public String getNotificarme() {
        return notificarme;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
