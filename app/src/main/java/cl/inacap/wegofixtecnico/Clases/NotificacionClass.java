package cl.inacap.wegofixtecnico.Clases;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NotificacionClass implements Parcelable {
    private int idNotificacion;
    private String Mensaje;
    private String Usuario_Usuario;

    private ArrayList<NotificacionClass> lista = new ArrayList<NotificacionClass>();


    public NotificacionClass(){

    }

    public NotificacionClass(Parcel in){
        lista=new ArrayList<NotificacionClass>();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in){
        idNotificacion=in.readInt();
        Mensaje=in.readString();
        Usuario_Usuario=in.readString();
        in.readTypedList(lista,CREATOR);
    }
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(idNotificacion);
        dest.writeString(Mensaje);
        dest.writeString(Usuario_Usuario);
        dest.writeTypedList(lista);
    }

    public void Add(NotificacionClass notificaciones)
    {
        lista.add(notificaciones);
    }

    public static final Parcelable.Creator<NotificacionClass>CREATOR =new Parcelable.Creator<NotificacionClass>(){
        public NotificacionClass createFromParcel(Parcel in){
            return new NotificacionClass(in);
        }
        public NotificacionClass[] newArray(int size){
            return new NotificacionClass[size];
        }
    };

    public NotificacionClass(int idNotificacion, String mensaje, String usuario){
        this.idNotificacion=idNotificacion;
        this.Mensaje=mensaje;
        this.Usuario_Usuario=usuario;
    }

    public NotificacionClass(NotificacionClass notificacionClass) {
        this.idNotificacion = notificacionClass.idNotificacion;
        this.Mensaje = notificacionClass.Mensaje;
        this.Usuario_Usuario = notificacionClass.Usuario_Usuario;
    }
    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public String getUsuario_Usuario() {
        return Usuario_Usuario;
    }

    public void setUsuario_Usuario(String usuario_Usuario) {
        Usuario_Usuario = usuario_Usuario;
    }
}
