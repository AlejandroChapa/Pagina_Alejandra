package es.unavarra.tlm.dscr_24_06.Login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import es.unavarra.tlm.dscr_24_06.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.battleship.tatai.es/v2/session";

        // Crear el objeto de solicitud
        User userLogin = new User(username, password);
        Gson gson = new Gson();
        String jsonUserLogin = gson.toJson(userLogin);

        try {
            // Crear la entidad de la solicitud
            StringEntity entity = new StringEntity(jsonUserLogin, ContentType.APPLICATION_JSON);

            // Enviar la solicitud POST
            client.put(this, url, entity, "application/json", new LoginResponseHandler(this));


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}
