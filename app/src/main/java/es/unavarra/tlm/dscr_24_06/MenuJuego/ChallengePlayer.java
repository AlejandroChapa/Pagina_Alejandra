package es.unavarra.tlm.dscr_24_06.MenuJuego;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import es.unavarra.tlm.dscr_24_06.Login.SessionManager;

public class ChallengePlayer {

    // Método modificado para aceptar un contexto genérico
    public void sendChallenge(GameActivity context, String username) {
        // Paso 1: Obtener el token de sesión
        SessionManager sessionManager = new SessionManager(context);
        String sessionToken = sessionManager.getSession(); // Obtener el token (que es un JSON)

        if (sessionToken == null) {
            Toast.makeText(context, "No hay token de sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        // Paso 2: Extraer el token desde el JSON
        String token = sessionManager.extractTokenFromSession(sessionToken);

        // Paso 3: Configurar el cliente HTTP
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Authentication", token);  // Usar el token extraído en el encabezado

        // Paso 4: Crear el JSON con el 'username' que se quiere desafiar
        try {
            JSONObject challengeData = new JSONObject();
            challengeData.put("username", username);  // Añadir el 'username' al cuerpo de la solicitud

            // Convertir el JSON a StringEntity para enviarlo en la solicitud
            StringEntity entity = new StringEntity(challengeData.toString(), "UTF-8");

            // Paso 5: Enviar la solicitud PUT
            client.put(context, "https://api.battleship.tatai.es/v2/challenge", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    // Aquí puedes manejar la respuesta de la API, por ejemplo:
                    Toast.makeText(context, "Desafío enviado exitosamente", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    // Mostrar el código de estado de la respuesta
                    String errorMessage = "Error al enviar el desafío";
                    if (errorResponse != null) {
                        errorMessage = "Error: " + errorResponse.toString();
                    }

                    // Agregar el código de estado y el error al mensaje de error
                    Toast.makeText(context, "Error al enviar el desafío. Código: " + statusCode + " - " + errorMessage, Toast.LENGTH_LONG).show();

                    // Imprimir más detalles para depuración
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al crear el JSON", Toast.LENGTH_SHORT).show();
        }
    }
}
