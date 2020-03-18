package cl.inacap.wegofixtecnico;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cl.inacap.wegofixtecnico.Clases.*;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Notificacion extends AppCompatActivity {

    SweetAlertDialog dp;
    String tecnico = "";
    private SharedPreferences prefs;
    TextView texto;
    private ListView lista;
    private static LayoutInflater inflater = null;

    ArrayList<NotificacionClass> notificaciones = new ArrayList<NotificacionClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion);

        setcredentiasexist();
        inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        enviarRequest(tecnico);

        texto = (TextView)findViewById(R.id.tvNoNoti);

    }

    String url = "http://www.sebastianbaldovinos.com/app/test.php?funcion=encontrarnotificacion";

    //metodo para enviar los datos y realizar la accion o acciones necesarias
    private void enviarRequest(final String Tecnico) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray listaJSON = jsonObject.getJSONArray("response");
                            if (listaJSON != null) {
                                texto.setText("Notificaciones");
                                for (int i = 0; i < listaJSON.length(); i++) {
                                    JSONObject s = listaJSON.getJSONObject(i);
                                    NotificacionClass notificacion = new NotificacionClass();
                                    notificacion.setIdNotificacion(s.getInt("IdNotificacion"));
                                    notificacion.setMensaje(s.getString("Mensaje"));
                                    notificacion.setUsuario_Usuario(s.getString("Usuario_Usuario"));
                                    notificaciones.add(notificacion);
                                }

                                if (notificaciones.size() > 0) {
                                    final View vista = inflater.inflate(R.layout.elemento_solicitud, null);
                                    lista = (ListView) findViewById(R.id.lvNotifiaciones);
                                    NotificacionAdaptador ad = new NotificacionAdaptador(getApplicationContext(), notificaciones);
                                    lista.setAdapter(ad);
                                } else if(notificaciones.size() == 0){
                                    dp = new SweetAlertDialog(Notificacion.this, SweetAlertDialog.WARNING_TYPE);
                                    dp.setTitleText("UwU");
                                    dp.setContentText("No se han encontrado solicitudes pendientes!");
                                    dp.show();
                                    texto.setText("No hay nuevas notificaciones.");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Usuario", Tecnico);
                return params;
            }
        };
        AppSingleton.getInstance(Notificacion.this).addToRequestQue(stringRequest);
    }

    private void setcredentiasexist() {
        String usuario = getuserusuairoprefs();
        if (!TextUtils.isEmpty(usuario)) {
            tecnico = usuario;
        }
    }

    private String getuserusuairoprefs() {

        return prefs.getString("Usuario", "");
    }

    private String getusercontraseñaprefs() {

        return prefs.getString("Contraseña", "");
    }
}
