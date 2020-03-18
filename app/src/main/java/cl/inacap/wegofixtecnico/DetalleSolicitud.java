package cl.inacap.wegofixtecnico;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cl.inacap.wegofixtecnico.Clases.AppSingleton;
import cl.inacap.wegofixtecnico.Clases.Solicitud;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetalleSolicitud extends AppCompatActivity {

    SweetAlertDialog dp;
    private SharedPreferences prefs;
    private TextView tvIdSolicitud;
    private TextView tvUsuario;
    private TextView tvFecha;
    private TextView tvEstado;
    private TextView tvDescripcion;
    private ImageView ivPerfil;
    private Button btnAtender;

    String tecnico = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_solicitud);

        int img[] = {R.drawable.baldo};

        tvIdSolicitud = (TextView)findViewById(R.id.tvIdSolicitud);
        tvUsuario = (TextView)findViewById(R.id.tvUsuario);
        tvFecha = (TextView)findViewById(R.id.tvFecha);
        tvEstado = (TextView)findViewById(R.id.tvEstado);
        tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);
        ivPerfil =(ImageView)findViewById(R.id.ivPerfil);
        btnAtender = (Button)findViewById(R.id.btnAtender);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        final Solicitud soli = (Solicitud) b.getParcelable("soli");


        tvIdSolicitud.setText(String.valueOf(soli.getIdSolicitud()));
        tvUsuario.setText(soli.getSolicitadopor_Usuario());
        tvFecha.setText(soli.getFecha());
        tvEstado.setText(String.valueOf(soli.getEstado_idEstado()));
        tvDescripcion.setText(soli.getDescripcion());
        ivPerfil.setImageResource(img[0]);


        setcredentiasexist();

        btnAtender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idsoli = String.valueOf(soli.getIdSolicitud());
                String cliente = soli.getSolicitadopor_Usuario();

                enviarRequest(idsoli,tecnico,cliente);
            }
        });



    }

    String url = "http://www.sebastianbaldovinos.com/app/test.php?funcion=atendersolicitud";

    //metodo para enviar los datos y realizar la accion o acciones necesarias
    private void enviarRequest(final String idSolicitud, final String Tecnico, final String Usuario) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String valor = jsonObject.getString("response");
                            if (valor.equals("OK")) {
                                Intent intent = new Intent(DetalleSolicitud.this, Principal.class);
                                dp = new SweetAlertDialog(DetalleSolicitud.this, SweetAlertDialog.ERROR_TYPE);
                                dp.setTitleText("Buena!");
                                dp.setContentText("Solicitud Atendida!");
                                dp.show();
                                startActivity(intent);
                            } else {
                                dp = new SweetAlertDialog(DetalleSolicitud.this, SweetAlertDialog.ERROR_TYPE);
                                dp.setTitleText("Oops...");
                                dp.setContentText("No se ha podido crear la solicitud!");
                                dp.show();
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
                params.put("idSolicitud", idSolicitud);
                params.put("Atendidopor_Usuario", Tecnico);
                params.put("Solicitadopor_Usuario", Usuario);
                return params;
            }
        };
        AppSingleton.getInstance(DetalleSolicitud.this).addToRequestQue(stringRequest);
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
