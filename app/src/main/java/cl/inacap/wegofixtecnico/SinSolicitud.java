package cl.inacap.wegofixtecnico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SinSolicitud extends AppCompatActivity {

    private Button btnVerSolicitudes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_solicitud);

        btnVerSolicitudes =(Button)findViewById(R.id.btnVerSoli);

        btnVerSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SinSolicitud.this, VerSolicitudes.class);
                startActivity(intent);
            }
        });
    }
}
