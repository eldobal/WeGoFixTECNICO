package cl.inacap.wegofixtecnico;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import cl.inacap.wegofixtecnico.Clases.AppSingleton;
import cn.pedant.SweetAlert.SweetAlertDialog;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    SweetAlertDialog dp;
    private SharedPreferences prefs;
    private EditText txtUsuario;
    private EditText txtContraseña;
    private Button btnIniciarSesion;
    private Button btnRegistrarse;


    public Login() {
    }


    //codigo para el correcto funcionamiento del login



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = (EditText) findViewById(R.id.txtNombre);
        txtContraseña = (EditText) findViewById(R.id.txtContraseña);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);


        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String usuario = txtUsuario.getText().toString();
                String contrasena = txtContraseña.getText().toString();
                String  hola ="holaffffgfdgsfgsfgsfgsfdgsfdgf";
                if (!usuario.equals("") && !contrasena.equals("") && !usuario.isEmpty() && !contrasena.isEmpty()) {
                    enviarRequest(usuario, contrasena);
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                }
            }

        });


    }

    String url = "http://www.sebastianbaldovinos.com/app/test.php?funcion=logintecnico";

    //metodo para enviar los datos y realizar la accion o acciones necesarias
    private void enviarRequest(final String Usuario, final String Contrasena) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //rescatas el json desde la web api
                            JSONObject jsonObject = new JSONObject(response);
                            // rescatas el valor del objeto
                            String valor = jsonObject.getString("response");
                            //comparas el string rescatado del objetojson y lo comparas con un ok
                            if (valor.equals("OK")) {
                                //conversion de datos a String
                                String usu = txtUsuario.getText().toString();
                                String contra = txtContraseña.getText().toString();
                                Intent intent = new Intent(Login.this, Principal.class);
                                //guardar los datos de la session en el sp
                                saveOnPreferences(usu, contra);
                                startActivity(intent);
                            } else {
                                dp = new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE);
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
                params.put("Usuario", Usuario);
                params.put("Contrasena", Contrasena);
                return params;
            }
        };
        AppSingleton.getInstance(Login.this).addToRequestQue(stringRequest);
    }

    private void saveOnPreferences(String usuario, String contrasena) {


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Usuario", usuario);
        editor.putString("ContraseNa", contrasena);
        //linea la cual guarda todos los valores en la pref antes de continuar
        editor.commit();
        editor.apply();


    }

    private void setcredentiasexist() {
        String usuario = getuserusuairoprefs();
        String contraseña = getusercontraseñaprefs();
        if (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(contraseña)) {
            txtUsuario.setText(usuario);
            txtContraseña.setText(contraseña);
        }
    }

    private String getuserusuairoprefs() {

        return prefs.getString("Usuario", "");
    }

    private String getusercontraseñaprefs() {

        return prefs.getString("Contraseña", "");
    }
}
