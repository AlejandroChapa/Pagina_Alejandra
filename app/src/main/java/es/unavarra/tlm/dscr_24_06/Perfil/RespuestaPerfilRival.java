package es.unavarra.tlm.dscr_24_06.Perfil;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import es.unavarra.tlm.dscr_24_06.Tablero;

public class RespuestaPerfilRival {

    public void RespuestaRival(Tablero context, String userID, String token) {
        AsyncHttpClient client = new AsyncHttpClient();

        // Configurar el encabezado con el token de sesión
        client.addHeader("X-Authentication", token);

        // Construir la URL con el game_id
        String url = "https://api.battleship.tatai.es/v2/user/" + userID;

        // Enviar una solicitud GET para obtener los datos del perfil
        client.get((Context) context, url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Convertir la respuesta a String
                String response = new String(responseBody);

                // Mostrar en Logcat para depuración
                Log.d("RespuestaPerfilRival", "Respuesta exitosa: " + response);

                // Usar tu clase existente para procesar y mostrar los datos
                procesarYMostrarPerfilRival((Context) context, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Convertir la respuesta a String (si no es nula)
                String response = responseBody != null ? new String(responseBody) : "No hay respuesta";

                // Mostrar en Logcat el error
                Log.e("RespuestaPerfilRival", "Error al obtener el perfil: " + response, error);

                // Mostrar un Toast indicando el error
                Toast.makeText((Context) context, "Error al obtener el perfil.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void procesarYMostrarPerfilRival(Context context, String json) {
        try {
            // Usar Gson para convertir el JSON a la clase que ya tienes creada
            Gson gson = new Gson();
            // Cambia `TuClase` por el nombre real de la clase que mapea el JSON
            Perfil userInfo = gson.fromJson(json, Perfil.class);

            // Crear un mensaje con los datos
            String mensaje = "ID: " + userInfo.getUser().getId() + "\n" +
                    "Usuario: " + userInfo.getUser().getUsername() + "\n" +
                    "Cuenta Creada: " + userInfo.getAccount().getCreatedAt() + "\n" +
                    "Juegos Jugados: " + userInfo.getStats().getGames() + "\n" +
                    "Victorias: " + userInfo.getStats().getWins() + "\n" +
                    "Derrotas: " + userInfo.getStats().getLosses() + "\n" +
                    "Victorias por Rendición: " + userInfo.getStats().getWinsBySurrender() + "\n" +
                    "Derrotas por Rendición: " + userInfo.getStats().getLossesBySurrender();

            // Mostrar el mensaje en un AlertDialog
            new AlertDialog.Builder(context)
                    .setTitle("Perfil de "+userInfo.getUser().getUsername())
                    .setMessage(mensaje)
                    .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                    .show();

        } catch (Exception e) {
            Log.e("ProcesarPerfil", "Error al procesar el JSON: " + e.getMessage(), e);
            Toast.makeText(context, "Error al procesar los datos del perfil.", Toast.LENGTH_SHORT).show();
        }
    }
}
