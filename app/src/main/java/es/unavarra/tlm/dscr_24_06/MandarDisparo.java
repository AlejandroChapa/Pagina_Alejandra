package es.unavarra.tlm.dscr_24_06;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.Header;

public class MandarDisparo {
    FireResponse fireResponse;
    public static FireResponse lastFireResponse;
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(MandarDisparo.class);

    public void MandarDis(Context context, String gameId, String token, String row, int col) {
        AsyncHttpClient client = new AsyncHttpClient();

        // Configurar el encabezado con el token de sesión
        client.addHeader("X-Authentication", token);
        client.addHeader("Content-Type", "application/json");  // Especifica que estamos enviando JSON

        // Construir la URL con el game_id (parámetro de ruta)
        String url = "https://api.battleship.tatai.es/v2/game/" + gameId + "/shoot";

        // Crear el cuerpo de la solicitud (Request Body) para el disparo
        try {
            JSONObject position = new JSONObject();
            position.put("row", row);  // "row": "A"
            position.put("column", col);  // "column": 1
            JSONObject requestBody = new JSONObject();
            requestBody.put("position", position);
            Log.d("MandarDisparo", "Cuerpo de la solicitud: " + requestBody.toString());

            // Enviar la solicitud POST con el cuerpo de la solicitud en formato JSON
            StringEntity entity = new StringEntity(requestBody.toString(), "UTF-8");
            client.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    // Convertir la respuesta a String
                    String response = new String(responseBody);
                    Gson gson = new Gson();
                    lastFireResponse = gson.fromJson(response, FireResponse.class);
                    fireResponse = gson.fromJson(response, FireResponse.class);

                    if (fireResponse.getGame().getState().equals("finished")){
                        new AlertDialog.Builder(context)
                                .setTitle(" Ha acabado.")
                                .setMessage("¿Quieres Aceptarala o Rechazar?")
                                .setPositiveButton("Aceptarala", (dialog, which) -> {

                                })
                                .setNegativeButton("Rechazar", (dialog, which) -> {
                                    // Rechazar el juego si el usuario confirma



                                })
                                .show();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = responseBody != null ? new String(responseBody) : "No hay respuesta";
                    Log.e("MandarDisparo", "Error al enviar el disparo: " + response, error);

                    // Aquí logueamos el código de estado HTTP
                    Log.e("MandarDisparo", "Código de estado: " + statusCode);
                    // También puedes revisar el contenido del error
                    Log.e("MandarDisparo", "Error: " + error.getMessage());

                    Toast.makeText(context, "Espera tu turno", Toast.LENGTH_SHORT).show();
                }

            });
        }  catch (Exception e) {
            // Manejo de errores al construir el cuerpo de la solicitud
            Log.e("MandarDisparo", "Error al crear el cuerpo de la solicitud: " + e.getMessage(), e);
            Toast.makeText(context, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}