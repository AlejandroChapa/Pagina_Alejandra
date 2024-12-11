package es.unavarra.tlm.dscr_24_06.Login;

import static com.loopj.android.http.AsyncHttpClient.log;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import cz.msebera.android.httpclient.entity.StringEntity; // Asegúrate de importar esta
import cz.msebera.android.httpclient.entity.ContentType; // Si la necesitas
import es.unavarra.tlm.dscr_24_06.R;

import androidx.appcompat.app.AppCompatActivity;


public class RegistroActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasena;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString();
                String contrasena = etContrasena.getText().toString();

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(RegistroActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Crear el objeto User
                    User newUser = new User(usuario, contrasena);

                    // Convertir el objeto a JSON usando Gson


                    // Enviar la solicitud al servidor
                    sendRegisterRequest(newUser);
                }
            }
        });
    }

    private void sendRegisterRequest(User newUser) {
        AsyncHttpClient client = new AsyncHttpClient();
        Gson gson = new Gson();

        try {
            // Crear la entidad de la solicitud
            StringEntity entity = new StringEntity(gson.toJson(newUser), ContentType.APPLICATION_JSON);
            log.d("RegistroActivity","JSON request:" + gson.toJson(newUser));

            // Enviar la solicitud PUT
            client.put((Context) this, "https://api.battleship.tatai.es/v2/user", entity, "application/json", new RegistroResponseHandler(this));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }


}

