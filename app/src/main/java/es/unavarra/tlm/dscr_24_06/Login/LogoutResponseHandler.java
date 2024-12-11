package es.unavarra.tlm.dscr_24_06.Login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LogoutResponseHandler extends AsyncHttpResponseHandler {

    private Context context;

    public LogoutResponseHandler(Context context) {
        this.context = context;
    }

    // Método para enviar la solicitud de logout
    public void logout() {

        // Usar SessionManager para obtener el token de sesión
        SessionManager sessionManager = new SessionManager(context);
        String sessionToken = sessionManager.getSession(); // Obtener el token (que es un JSON)

        if (sessionToken == null) {
            Toast.makeText(context, "No hay token de sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        // Extraer solo el 'token' desde el JSON
        String token = sessionManager.extractTokenFromSession(sessionToken);

        if (token == null) {
            Toast.makeText(context, "Token no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Configurar el cliente HTTP
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Authentication", token);  // Usar el token extraído en el encabezado

        // Crear un JSON vacío, si el API lo requiere
        StringEntity entity = new StringEntity("", "UTF-8");

        // Enviar la solicitud DELETE
        client.delete(context, "https://api.battleship.tatai.es/v2/session", entity, "application/json", this);

        // Limpiar la sesión usando SessionManager
        sessionManager.clearSession(); // Limpiar el token
    }

    @Override
    public void onSuccess(int httpCode, Header[] headers, byte[] responseBody) {
        // Mostrar un mensaje de éxito
        Toast.makeText(context, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

        // Redirigir al LoginActivity
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onFailure(int httpCode, Header[] headers, byte[] responseBody, Throwable error) {
        // Manejar el error
        Toast.makeText(context, "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
    }
}
