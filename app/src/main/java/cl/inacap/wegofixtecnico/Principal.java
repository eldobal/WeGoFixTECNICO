package cl.inacap.wegofixtecnico;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import cl.inacap.wegofixtecnico.Clases.AppSingleton;
import cl.inacap.wegofixtecnico.Clases.Solicitud;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity {

    SweetAlertDialog dp;
    private SharedPreferences prefs;
    private static LayoutInflater inflater = null;
    private ArrayList<Solicitud> solicitudes = new ArrayList<Solicitud>();
    private Solicitud solicitud = new Solicitud();
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final String Fecha = sdf.format(calendar.getTime());

    private ImageButton btnNotificacion;
    private Button btnVerSolicitudes;
    private Button btnAtendidos;
    private Button btnEstadisticas;

    String Tecnico = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        btnVerSolicitudes = (Button) findViewById(R.id.btnVerSolicitudes);
        btnAtendidos = (Button) findViewById(R.id.btnAtendidos);
        btnEstadisticas = (Button) findViewById(R.id.btnEstadisticas);
        btnNotificacion = (ImageButton) findViewById(R.id.btnNotificacion);
        setcredentiasexist();

        btnVerSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Principal.this, VerSolicitudes.class);
                startActivity(intent);
            }
        });

        btnAtendidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarRequest(Tecnico);
            }
        });
        btnEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Principal.this, Estadisticas.class);
                startActivity(intent);
            }
        });

        btnNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    String url = "http://www.sebastianbaldovinos.com/app/test.php?funcion=solicitudatendida";

    private void enviarRequest(final String Tecnico) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String solicitudesStr) {
                        try {
                            JSONObject jsonObject = new JSONObject(solicitudesStr);
                            String valor = jsonObject.getString("solicitudes");
                            if(valor.equals("ERROR")){
                                Intent intent = new Intent(Principal.this, SinSolicitud.class);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(Principal.this, SolicitudAtendida.class);
                                startActivity(intent);
                            }
                            /*for (int i = 0; i < listaJSON.length(); i++) {
                                JSONObject s = listaJSON.getJSONObject(i);
                                solicitud.setIdSolicitud(s.getInt("idSolicitud"));
                                solicitud.setFecha(s.getString("Fecha"));
                                solicitud.setDescripcion(s.getString("Descripcion"));
                                solicitud.setEstado_idEstado(s.getInt("Estado_idEstado"));
                                solicitud.setSolicitadopor_Usuario(s.getString("Solicitadopor_Usuario"));
                                solicitud.setAtendidopor_Usuario(s.getString("Atendidopor_Usuario"));
                            }*/

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
        AppSingleton.getInstance(Principal.this).addToRequestQue(stringRequest);
    }

    private void setcredentiasexist() {
        String usuario = getuserusuairoprefs();
        if (!TextUtils.isEmpty(usuario)) {
            Tecnico = usuario;
        }
    }

    private String getuserusuairoprefs() {

        return prefs.getString("Usuario", "");
    }
}
