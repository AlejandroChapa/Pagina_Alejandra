package es.unavarra.tlm.dscr_24_06.MenuJuego;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Aceptar {

    public void AceptarInvitacion(Context context, String gameId, String token){
        AsyncHttpClient client = new AsyncHttpClient();

        // Configurar el encabezado con el token de sesión
        client.addHeader("X-Authentication", token);

        // Construir la URL con el game_id (parámetro de ruta)
        String url = "https://api.battleship.tatai.es/v2/game/" + gameId;

        // Enviar una solicitud POST para aceptar la invitación (sin cuerpo, si la API no lo requiere)
        client.post(context, url, null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Convertir la respuesta a String
                String response = new String(responseBody);

                // Mostrar en Logcat para depuración
                Log.d("AceptarInvitacion", "Respuesta exitosa: " + response);

                // Mostrar un Toast indicando éxito
                Toast.makeText(context, "Juego aceptado exitosamente: " + gameId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Convertir la respuesta a String (si no es nula)
                String response = responseBody != null ? new String(responseBody) : "No hay respuesta";

                // Mostrar en Logcat el error
                Log.e("AceptarInvitacion", "Error al aceptar la invitación: " + response, error);

                // Mostrar un Toast indicando el error
                Toast.makeText(context, "Error al aceptar la invitación: " + gameId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
