package cl.inacap.wegofixtecnico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cl.inacap.wegofixtecnico.Clases.Adaptador;
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
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerSolicitudes extends AppCompatActivity {

    SweetAlertDialog dp;
    private static LayoutInflater inflater = null;
    private TextView tvNoRegistros;
    private ListView lista;
    private ImageButton btnVolver;


    ArrayList<Solicitud> solicitudes = new ArrayList<Solicitud>();

    int[] imagenes = {
            R.drawable.ic_pendiente,
            R.drawable.ic_atendida,
            R.drawable.ic_enproceso,
            R.drawable.ic_completada,
            R.drawable.ic_cancelada
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_solicitudes);
        inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        enviarRequest();

        tvNoRegistros = (TextView)findViewById(R.id.tvNoRegistros);

        btnVolver = (ImageButton)findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerSolicitudes.this, Principal.class);
                startActivity(intent);
            }
        });
    }

    String url = "http://www.sebastianbaldovinos.com/app/test.php?funcion=listasolicitudes";

    private void enviarRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String solicitudesStr) {
                        try {
                            JSONObject jsonObject = new JSONObject(solicitudesStr);
                            JSONArray listaJSON = jsonObject.getJSONArray("solicitudes");

                            for (int i = 0; i < listaJSON.length(); i++) {
                                JSONObject s = listaJSON.getJSONObject(i);
                                Solicitud solicitud = new Solicitud();
                                solicitud.setIdSolicitud(s.getInt("idSolicitud"));
                                solicitud.setFecha(s.getString("Fecha"));
                                solicitud.setDescripcion(s.getString("Descripcion"));
                                solicitud.setEstado_idEstado(s.getInt("Estado_idEstado"));
                                solicitud.setSolicitadopor_Usuario(s.getString("Solicitadopor_Usuario"));
                                solicitud.setAtendidopor_Usuario(s.getString("Atendidopor_Usuario"));

                                solicitudes.add(solicitud);
                            }
                            if (solicitudes.size() > 0) {
                                final View vista = inflater.inflate(R.layout.elemento_solicitud, null);
                                lista = (ListView) findViewById(R.id.lvSolicitudes);
                                Adaptador ad = new Adaptador(getApplicationContext(), solicitudes, imagenes);
                                lista.setAdapter(ad);
                            } else if(solicitudes.size() == 0){
                                dp = new SweetAlertDialog(VerSolicitudes.this, SweetAlertDialog.WARNING_TYPE);
                                dp.setTitleText("UwU");
                                dp.setContentText("No se han encontrado solicitudes pendientes!");
                                dp.show();
                                tvNoRegistros.setText("No hay solicitudes pendientes.");
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
                return params;
            }
        };
        AppSingleton.getInstance(VerSolicitudes.this).addToRequestQue(stringRequest);
    }
}
