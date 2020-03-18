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
import android.widget.*;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SolicitudAtendida extends AppCompatActivity {

    SweetAlertDialog dp;
    private SharedPreferences prefs;
    private static LayoutInflater inflater = null;
    private ArrayList<Solicitud> solicitudes = new ArrayList<Solicitud>();
    private Solicitud solicitud = new Solicitud();
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final String Fecha = sdf.format(calendar.getTime());

    //ITEMS VIEW
    private Button btnBoton;
    private EditText txtTexto;
    private EditText txtPrecio;

    private RadioButton rbPagada;
    private RadioButton rbNoPagada;
    private RadioGroup rbg;

    private Button btnFinalizar;
    private TextView tvIdSolicitud;
    private TextView tvUsuario;
    private TextView tvFecha;
    private TextView tvEstado;
    private TextView tvDescripcion;
    private TextView tvAviso;
    // FIN ITEMS VIEW


    String Tecnico = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_atendida);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);

        // DECLARACIONES ITEMS

        btnBoton = (Button) findViewById(R.id.btnBoton);
        txtTexto = (EditText) findViewById(R.id.txtTexto);
        txtPrecio = (EditText) findViewById(R.id.txtPrecio);

        rbPagada = (RadioButton) findViewById(R.id.rbPagada);
        rbNoPagada = (RadioButton) findViewById(R.id.rbNoPagada);
        rbg = (RadioGroup) findViewById(R.id.rbg);

        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        tvIdSolicitud = (TextView) findViewById(R.id.tvIdSolicitud);
        tvUsuario = (TextView) findViewById(R.id.tvUsuario);
        tvFecha = (TextView) findViewById(R.id.tvFecha);
        tvEstado = (TextView) findViewById(R.id.tvEstado);
        tvDescripcion = (TextView) findViewById(R.id.tvDescripcion);
        tvAviso = (TextView) findViewById(R.id.tvAviso);
        // FIN DECLARACIONES ITEMS

        setcredentiasexist();
        enviarRequest(Tecnico);
    }

    String url = "http://www.sebastianbaldovinos.com/app/test.php?funcion=solicitudatendida";

    private void enviarRequest(final String Tecnico) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String solicitudesStr) {
                        try {
                            JSONObject jsonObject = new JSONObject(solicitudesStr);
                            JSONArray listaJSON = jsonObject.getJSONArray("solicitudes");
                            if (listaJSON.length() > 0) {

                                for (int i = 0; i < listaJSON.length(); i++) {
                                    JSONObject s = listaJSON.getJSONObject(i);
                                    solicitud.setIdSolicitud(s.getInt("idSolicitud"));
                                    solicitud.setFecha(s.getString("Fecha"));
                                    solicitud.setDescripcion(s.getString("Descripcion"));
                                    solicitud.setEstado_idEstado(s.getInt("Estado_idEstado"));
                                    solicitud.setSolicitadopor_Usuario(s.getString("Solicitadopor_Usuario"));
                                    solicitud.setAtendidopor_Usuario(s.getString("Atendidopor_Usuario"));
                                }
                                tvIdSolicitud.setText(String.valueOf(solicitud.getIdSolicitud()));
                                tvUsuario.setText(solicitud.getSolicitadopor_Usuario());
                                tvFecha.setText(solicitud.getFecha());
                                tvEstado.setText(String.valueOf(solicitud.getEstado_idEstado()));
                                tvDescripcion.setText(solicitud.getDescripcion());

                                if (solicitud.getEstado_idEstado() == 8/*CONFIRMADA*/) {
                                    txtPrecio.setVisibility(View.INVISIBLE);
                                    rbPagada.setVisibility(View.INVISIBLE);
                                    rbNoPagada.setVisibility(View.INVISIBLE);
                                    txtPrecio.setVisibility(View.INVISIBLE);
                                    btnFinalizar.setBackgroundColor(Color.GRAY);
                                    btnFinalizar.setTextColor(Color.BLACK);
                                    tvAviso.setHint("Necesita diagnosticar para Finalizar");
                                    btnBoton.setText("DIAGNOSTICAR");
                                    txtTexto.setHint("Diagnóstico");
                                    btnBoton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String id = String.valueOf(solicitud.getIdSolicitud());
                                            String diagnostico = txtTexto.getText().toString();
                                            if (!txtTexto.getText().equals("")) {
                                                diagnosticar(id, Fecha, diagnostico);
                                                Intent intent = new Intent(SolicitudAtendida.this, Principal.class);
                                                startActivity(intent);
                                            } else {
                                                dp = new SweetAlertDialog(SolicitudAtendida.this, SweetAlertDialog.WARNING_TYPE);
                                                dp.setTitleText("UwU");
                                                dp.setContentText("El diagnóstico es obligatorio!");
                                                dp.show();
                                            }

                                        }
                                    });
                                } else if (solicitud.getEstado_idEstado() == 4 /*REPARANDO*/) {
                                    txtTexto.setHint("Solución");
                                    tvAviso.setVisibility(View.INVISIBLE);
                                    btnBoton.setVisibility(View.INVISIBLE);
                                    btnFinalizar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!txtTexto.getText().equals("") && !txtPrecio.getText().equals("")) {
                                                String precio = txtPrecio.getText().toString();
                                                String solucion = txtTexto.getText().toString();
                                                String idSolicitud = tvIdSolicitud.getText().toString();
                                                String usuario = tvUsuario.getText().toString();
                                                if (rbPagada.isChecked()) {
                                                    solucionarPagada(precio, solucion, idSolicitud, usuario, Tecnico);
                                                } else if (rbNoPagada.isChecked()) {
                                                    solucionarNoPagada(precio, solucion, idSolicitud, usuario, Tecnico);
                                                }
                                            } else {
                                                dp = new SweetAlertDialog(SolicitudAtendida.this, SweetAlertDialog.WARNING_TYPE);
                                                dp.setTitleText("UwU");
                                                dp.setContentText("Solucion y/o Precio son obligatorios!");
                                                dp.show();
                                            }
                                        }
                                    });

                                } else if (solicitud.getEstado_idEstado() == 2 /*ATENDIENDO*/) {

                                    txtPrecio.setVisibility(View.INVISIBLE);
                                    rbNoPagada.setVisibility(View.INVISIBLE);
                                    rbPagada.setVisibility(View.INVISIBLE);
                                    tvAviso.setHint("El cliente debe confirmar su ida.");

                                    btnBoton.setText("DIAGNOSTICAR");
                                    btnBoton.setBackgroundColor(Color.GRAY);
                                    btnBoton.setTextColor(Color.BLACK);

                                    btnFinalizar.setBackgroundColor(Color.GRAY);
                                    btnFinalizar.setTextColor(Color.BLACK);

                                } else if (solicitud.getEstado_idEstado() == 3 /*DIAGNOSTICADO*/) {
                                    btnBoton.setVisibility(View.INVISIBLE);
                                    txtTexto.setVisibility(View.INVISIBLE);
                                    txtPrecio.setVisibility(View.INVISIBLE);
                                    tvAviso.setHint("Pregunte si el cliente necesita Reparacion.");

                                    btnFinalizar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String precio = "0";
                                            String solucion = txtTexto.getText().toString();
                                            String idSolicitud = tvIdSolicitud.getText().toString();
                                            String usuario = tvUsuario.getText().toString();
                                            if (rbPagada.isChecked()) {
                                                solucionarPagada(precio, solucion, idSolicitud, usuario, Tecnico);
                                            } else if (rbNoPagada.isChecked()) {
                                                solucionarNoPagada(precio, solucion, idSolicitud, usuario, Tecnico);
                                            } else if (!rbPagada.isChecked() && !rbNoPagada.isChecked()) {
                                                dp = new SweetAlertDialog(SolicitudAtendida.this, SweetAlertDialog.WARNING_TYPE);
                                                dp.setTitleText("UwU");
                                                dp.setContentText("Debe escoger 'Pagada' o 'No Pagada'!");
                                                dp.show();
                                            }
                                        }
                                    });

                                }
                            } else if (solicitud == null) {
                                Intent intent = new Intent(SolicitudAtendida.this, SinSolicitud.class);
                                startActivity(intent);
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
        AppSingleton.getInstance(SolicitudAtendida.this).addToRequestQue(stringRequest);
    }

    /*
     * ==========================================================================================
     *                               REQUEST PARA DIAGNOSTICAR
     * ==========================================================================================
     * */

    String url2 = "http://www.sebastianbaldovinos.com/app/test.php?funcion=diagnosticosolicitud";

    private void diagnosticar(final String idSolicitud, final String Fecha, final String Diagnostico) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String solicitudesStr) {
                        try {
                            JSONObject jsonObject = new JSONObject(solicitudesStr);
                            String valor = jsonObject.getString("response");

                            if (valor.equals("OK")) {
                                dp = new SweetAlertDialog(SolicitudAtendida.this, SweetAlertDialog.SUCCESS_TYPE);
                                dp.setTitleText("OwO");
                                dp.setContentText("Diagnostico creado exitosamente!");
                                dp.show();
                            } else {
                                dp = new SweetAlertDialog(SolicitudAtendida.this, SweetAlertDialog.ERROR_TYPE);
                                dp.setTitleText("Oops...");
                                dp.setContentText("No se ha podido diagnosticar!");
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
                params.put("Fecha", Fecha);
                params.put("Diagnostico", Diagnostico);
                return params;
            }
        };
        AppSingleton.getInstance(SolicitudAtendida.this).addToRequestQue(stringRequest);
    }

    /*
     * ==========================================================================================
     *                               REQUEST SOLICITUD PAGADA
     * ==========================================================================================
     * */

    String url3 = "http://www.sebastianbaldovinos.com/app/test.php?funcion=solicitudpagada";

    private void solucionarPagada(final String precio, final String solucion, final String idSolicitud, final String usuario, final String tecnico) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String solicitudesStr) {
                        try {
                            JSONObject jsonObject = new JSONObject(solicitudesStr);
                            String valor = jsonObject.getString("response");

                            if (valor.equals("OK")) {
                                Intent intent = new Intent(SolicitudAtendida.this, Principal.class);
                                startActivity(intent);
                            } else {
                                dp = new SweetAlertDialog(SolicitudAtendida.this, SweetAlertDialog.ERROR_TYPE);
                                dp.setTitleText("Oops...");
                                dp.setContentText("Error al completar Solicitud!");
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
                params.put("Precio", precio);
                params.put("Solucion", solucion);
                params.put("idSolicitud", idSolicitud);
                params.put("Solucitadopor_Usuario", tecnico);
                params.put("Atendidopor_Usuario", tecnico);

                return params;
            }
        };
        AppSingleton.getInstance(SolicitudAtendida.this).addToRequestQue(stringRequest);
    }

    /*
     * ==========================================================================================
     *                               REQUEST SOLICITUD NO PAGADA
     * ==========================================================================================
     * */

    String url4 = "http://www.sebastianbaldovinos.com/app/test.php?funcion=solicitudnopagada";

    private void solucionarNoPagada(final String precio, final String solucion, final String idSolicitud, final String usuario, final String tecnico) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String solicitudesStr) {
                        try {
                            JSONObject jsonObject = new JSONObject(solicitudesStr);
                            String valor = jsonObject.getString("response");

                            if (valor.equals("OK")) {
                                Intent intent = new Intent(SolicitudAtendida.this, Principal.class);
                                startActivity(intent);
                            } else {
                                dp = new SweetAlertDialog(SolicitudAtendida.this, SweetAlertDialog.ERROR_TYPE);
                                dp.setTitleText("Oops...");
                                dp.setContentText("Error al completar Solicitud!");
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
                params.put("Precio", precio);
                params.put("Solucion", solucion);
                params.put("idSolicitud", idSolicitud);
                params.put("Solucitadopor_Usuario", tecnico);
                params.put("Atendidopor_Usuario", tecnico);

                return params;
            }
        };
        AppSingleton.getInstance(SolicitudAtendida.this).addToRequestQue(stringRequest);
    }


    /*
     * ==========================================================================================
     *                               METODOS PARA GUARDAR USUARIO DE SESION
     * ==========================================================================================
     * */

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
