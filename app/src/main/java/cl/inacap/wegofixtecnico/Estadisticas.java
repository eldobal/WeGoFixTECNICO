package cl.inacap.wegofixtecnico;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cl.inacap.wegofixtecnico.Clases.AppSingleton;
import cl.inacap.wegofixtecnico.Clases.Estadistica;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Estadisticas extends AppCompatActivity {
    private SharedPreferences prefs;
    private TextView txtCalificacion;
    private TextView txtGanancia;
    private TextView txtSolicitudes;
    private Button btnVolver;
    private Estadistica estadistica = new Estadistica();
    String Tecnico = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_estadisticas);
        txtCalificacion =(TextView) findViewById(R.id.txtCalificacion);
        txtGanancia =(TextView) findViewById(R.id.txtGanancias);
        txtSolicitudes =(TextView) findViewById(R.id.txtSolicitudes);
        btnVolver = (Button) findViewById(R.id.btnVolver);

        setcredentiasexist();
        enviarRequest(Tecnico); //tecnico

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Estadisticas.this, Principal.class);
                startActivity(intent);
            }
        });
    }


    String url = "http://www.sebastianbaldovinos.com/app/test.php?funcion=estadisticas";

    private void enviarRequest(final String Tecnico) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String solicitudesStr) {
                        try {
                            JSONObject jsonObject = new JSONObject(solicitudesStr);
                           // String valor = jsonObject.getString("response");
                            JSONObject e = jsonObject.getJSONObject("response");
                            estadistica.setCalificacion(e.getInt("Calificacion"));
                            estadistica.setGanancia(e.getInt("Ganancia"));
                            estadistica.setSolicitudes(e.getInt("Solicitudes"));

                            txtSolicitudes.setText(String.valueOf(estadistica.getSolicitudes()));
                            txtGanancia.setText(String.valueOf(estadistica.getGanancia()));
                            txtCalificacion.setText(String.valueOf(estadistica.getCalificacion()));
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
        AppSingleton.getInstance(Estadisticas.this).addToRequestQue(stringRequest);
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
