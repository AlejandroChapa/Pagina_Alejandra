package es.unavarra.tlm.dscr_24_06.Juego;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MandarBarco {

    private static final org.apache.commons.logging.Log log = LogFactory.getLog(MandarBarco.class);

    public void Mandar(Context context, String gameId, String token, Ships barco) {
        AsyncHttpClient client = new AsyncHttpClient();

        // Configurar el encabezado con el token de sesión
        client.addHeader("X-Authentication", token);
        client.addHeader("Content-Type", "application/json");  // Especifica que estamos enviando JSON

        // Construir la URL con el game_id (parámetro de ruta)
        String url = "https://api.battleship.tatai.es/v2/game/" + gameId + "/ship";

        // Crear el cuerpo de la solicitud (Request Body) para el barco
        try {
            JSONObject requestBody = new JSONObject();

            // Asignar el tipo de barco
            requestBody.put("ship_type", barco.getShip_type());

            // Crear el array de posiciones
            JSONArray positionsArray = new JSONArray();
            for (Ships.Position position : barco.getPositions()) {
                JSONObject positionObject = new JSONObject();
                positionObject.put("row", position.getRow());
                positionObject.put("column", position.getColumn());
                positionsArray.put(positionObject);
            }

            // Asignar las posiciones al cuerpo de la solicitud
            requestBody.put("positions", positionsArray);

            Log.d("MandarBarco", "Cuerpo de la solicitud: " + requestBody);

            // Enviar la solicitud POST con el cuerpo de la solicitud en formato JSON
            client.post(context, url, new cz.msebera.android.httpclient.entity.StringEntity(requestBody.toString(), "UTF-8"), "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    // Convertir la respuesta a String
                    String response = new String(responseBody);

                    // Mostrar en Logcat para depuración
                    Log.d("Barco Enviado", "Respuesta exitosa: " + response);

                    // Mostrar un Toast indicando éxito
                    Toast.makeText(context, "Barco colocado exitosamente en el juego", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = responseBody != null ? new String(responseBody) : "No hay respuesta";
                    Log.e("MandarBarco", "Error al enviar el barco: " + response, error);

                    // Aquí logueamos el código de estado HTTP
                    Log.e("MandarBarco", "Código de estado: " + statusCode);
                    // También puedes revisar el contenido del error
                    Log.e("MandarBarco", "Error: " + error.getMessage());

                    Toast.makeText(context, "Error al colocar el barco en el juego", Toast.LENGTH_SHORT).show();
                }

            });
        } catch (Exception e) {
            // Manejo de errores al construir el cuerpo de la solicitud
            Log.e("MandarBarco", "Error al crear el cuerpo de la solicitud: " + e.getMessage(), e);
            Toast.makeText(context, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }


}
