package cl.inacap.wegofixtecnico.Clases;

public class Estadistica {
    private int Ganancia;
    private int Solicitudes;
    private int Calificacion;

    public Estadistica(){}
    public Estadistica(int ganancia, int solicitudes, int calificacion){
        this.Ganancia=ganancia;
        this.Solicitudes=solicitudes;
        this.Calificacion=calificacion;
    }

    public int getGanancia() {
        return Ganancia;
    }

    public void setGanancia(int ganancia) {
        Ganancia = ganancia;
    }

    public int getSolicitudes() {
        return Solicitudes;
    }

    public void setSolicitudes(int solicitudes) {
        Solicitudes = solicitudes;
    }

    public int getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(int calificacion) {
        Calificacion = calificacion;
    }
}
