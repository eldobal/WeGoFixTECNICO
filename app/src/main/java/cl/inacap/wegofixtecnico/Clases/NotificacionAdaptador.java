package cl.inacap.wegofixtecnico.Clases;

import android.widget.BaseAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cl.inacap.wegofixtecnico.R;

import java.util.ArrayList;

public class NotificacionAdaptador extends BaseAdapter {


    private static LayoutInflater inflater = null;

    Context contexto;
    ArrayList<NotificacionClass> notificaciones;
    ArrayList<NotificacionClass> listanotificaciones;
    NotificacionClass noti = new NotificacionClass();

    public NotificacionAdaptador(Context contexto, ArrayList<NotificacionClass> notificaciones) {
        this.contexto = contexto;
        this.notificaciones = notificaciones;

        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.elemento_notificacion, null);

        TextView mensaje = (TextView)vista.findViewById(R.id.tvMensaje);
        final Button btnOk = (Button)vista.findViewById(R.id.btnOk);

        mensaje.setText(notificaciones.get(i).getMensaje());

        noti.setIdNotificacion(notificaciones.get(i).getIdNotificacion());
        noti.setMensaje(mensaje.getText().toString());
        noti.setUsuario_Usuario(notificaciones.get(i).getUsuario_Usuario());



        final int posicion = i;
        btnOk.setTag(i);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificacionClass n;
                n = notificaciones.get(posicion);
                Intent vistaNotificaciones = new Intent(contexto, NotificacionClass.class);
                vistaNotificaciones.putExtra("noti", n);
                contexto.startActivity(vistaNotificaciones);
            }
        });


        return vista;
    }
}
