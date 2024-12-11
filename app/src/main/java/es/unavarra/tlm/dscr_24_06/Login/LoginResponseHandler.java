package es.unavarra.tlm.dscr_24_06.Login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import es.unavarra.tlm.dscr_24_06.MenuJuego.GameActivity;

public class LoginResponseHandler extends AsyncHttpResponseHandler {
    private final Context context;

    public LoginResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int httpCode, Header[] headers, byte[] responseBody) {
        // Registrar en Logcat la respuesta
        String response = new String(responseBody);
        Log.d("LoginResponse", "HTTP " + httpCode + ": " + response);

        // Usar SessionManager para guardar el token
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.saveSessionToken(response);

        // Obtener la hora de inicio de sesión
        String loginDate = sessionManager.getLoginDate();
        //System.out.println("Hora de inicio de sesión: " + loginDate); // Guarda el token en SharedPreferences

        // Redirigir a GameActivity
        Intent intent = new Intent(context, GameActivity.class);
        context.startActivity(intent);
        ((LoginActivity) context).finish(); // Cerrar LoginActivity
    }

    @Override
    public void onFailure(int httpCode, Header[] headers, byte[] responseBody, Throwable error) {
        // Registrar en Logcat el error
        String response = responseBody != null ? new String(responseBody) : "No response";
        Log.e("LoginResponse", "HTTP " + httpCode + ": " + response, error);

        // Mostrar el mensaje de error
        Toast.makeText(context, "Error al iniciar sesión: " + httpCode, Toast.LENGTH_SHORT).show();
    }
}
