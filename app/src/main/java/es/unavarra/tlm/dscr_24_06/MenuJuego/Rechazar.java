package es.unavarra.tlm.dscr_24_06.MenuJuego;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Rechazar {

    public void rechazarJuego(Context context, String gameId, String token) {
        // Crear el cliente HTTP
        AsyncHttpClient client = new AsyncHttpClient();

        // Configurar el encabezado con el token de sesión
        client.addHeader("X-Authentication", token);

        // Construir la URL
        String url = "https://api.battleship.tatai.es/v2/game/" + gameId;

        // Enviar la solicitud DELETE
        client.delete(context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Convertir la respuesta a String
                String response = new String(responseBody);

                // Mostrar en Logcat
                Log.d("RechazarJuego", "Respuesta exitosa: " + response);

                // Mostrar un Toast indicando éxito
                Toast.makeText(context, "Juego rechazado exitosamente: " + gameId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Convertir la respuesta a String (si no es nula)
                String response = responseBody != null ? new String(responseBody) : "No hay respuesta";

                // Mostrar en Logcat
                Log.e("RechazarJuego", "Error al rechazar el juego: " + response, error);

                // Mostrar un Toast indicando el error
                Toast.makeText(context, "Error al rechazar el juego: " + gameId, Toast.LENGTH_SHORT).show();
            }
        });
    }


}

