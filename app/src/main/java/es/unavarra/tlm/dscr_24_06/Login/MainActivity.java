package es.unavarra.tlm.dscr_24_06.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import es.unavarra.tlm.dscr_24_06.MenuJuego.GameActivity;
import es.unavarra.tlm.dscr_24_06.R;

public class MainActivity extends AppCompatActivity {

    private Button btnRegistro, btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SessionManager session = new SessionManager(this);


        if(session.isSessionActive()){
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        }


        // Referencias a los botones
        btnRegistro = findViewById(R.id.btnRegistro);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);


        // Listener para el botón de Registro
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de Registro
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        // Listener para el botón de Iniciar Sesión
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de Iniciar Sesión
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });




    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        logoutUser(this);

    }

    private void logoutUser(Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        // Crear una instancia de LogoutResponseHandler
        LogoutResponseHandler logoutHandler = new LogoutResponseHandler(this);
        // Llamar al método logout
        logoutHandler.logout();
        // Finalizar la actividad actual
        finish();
    }
}
