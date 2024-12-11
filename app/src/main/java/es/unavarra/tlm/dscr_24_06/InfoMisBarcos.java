package es.unavarra.tlm.dscr_24_06;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import es.unavarra.tlm.dscr_24_06.Juego.Ships;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.unavarra.tlm.dscr_24_06.MenuJuego.Game;


public class InfoMisBarcos {

    public void MisBarcos(Context context, String gameId, String token, final GameInfoCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        // Configurar el encabezado con el token de sesión
        client.addHeader("X-Authentication", token);

        // Construir la URL con el game_id (parámetro de ruta)
        String url = "https://api.battleship.tatai.es/v2/game/" + gameId;

        // Enviar una solicitud GET
        client.get(context, url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    // Obtener el array 'ships'
                    JSONArray ships = jsonResponse.getJSONArray("ships");

                    // Lista para almacenar los objetos Ships
                    ArrayList<Ships> shipsList = new ArrayList<>();

                    // Recorrer el array 'ships'
                    for (int i = 0; i < ships.length(); i++) {
                        JSONObject shipObject = ships.getJSONObject(i);
                        String shipType = shipObject.getString("type");

                        // Crear una nueva instancia de Ships
                        Ships ship = new Ships(shipType);

                        // Obtener las posiciones y agregar a la lista del barco
                        JSONArray positionsArray = shipObject.getJSONArray("position");
                        for (int j = 0; j < positionsArray.length(); j++) {
                            JSONObject positionObject = positionsArray.getJSONObject(j);
                            String row = positionObject.getString("row");
                            int column = positionObject.getInt("column");

                            // Añadir la posición al barco
                            ship.addPosition(row, column);
                        }
                        // Añadir el barco a la lista
                        shipsList.add(ship);
                    }

                    try {
                        // Obtener gunfire y validar su contenido
                        JSONObject gunfire = jsonResponse.optJSONObject("gunfire");
                        ArrayList<Gunfire> doneList = gunfire != null ? processGunfire(gunfire.optJSONArray("done")) : new ArrayList<>();
                        ArrayList<Gunfire> receivedList = gunfire != null ? processGunfire(gunfire.optJSONArray("received")) : new ArrayList<>();

                        // Llamar al callback con los datos
                        callback.onGameInfoReceived(shipsList, doneList, receivedList);

                    } catch (JSONException e) {
                        Log.e("InfoGame", "Error al procesar JSON", e);
                        Toast.makeText(context, "Error al procesar la partida: " + gameId, Toast.LENGTH_SHORT).show();
                    }




                } catch (JSONException e) {
                    Log.e("InfoGame", "Error al procesar JSON", e);
                    Toast.makeText(context, "Error al procesar la partida: " + gameId, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = responseBody != null ? new String(responseBody) : "Sin respuesta";
                Log.e("InfoGame", "Error al obtener la información del juego: " + response, error);
                Toast.makeText(context, "Error al cargar la partida: " + gameId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método auxiliar para procesar gunfire
    private ArrayList<Gunfire> processGunfire(JSONArray gunfireArray) throws JSONException {
        ArrayList<Gunfire> gunfireList = new ArrayList<>();

        if (gunfireArray == null || gunfireArray.length() == 0) {
            // Retornar una lista vacía si no hay disparos
            return gunfireList;
        }

        for (int i = 0; i < gunfireArray.length(); i++) {
            JSONObject gunfireObject = gunfireArray.getJSONObject(i);
            String result = gunfireObject.getString("result");
            JSONObject position = gunfireObject.getJSONObject("position");
            String row = position.getString("row");
            int column = position.getInt("column");

            // Crear un objeto Gunfire y agregarlo a la lista
            Gunfire gunfire = new Gunfire(result, row, column);
            gunfireList.add(gunfire);
        }

        return gunfireList;
    }


    // Definir el callback
    public interface GameInfoCallback {
        void onGameInfoReceived(ArrayList<Ships> ships, ArrayList<Gunfire> done, ArrayList<Gunfire> received);
    }

}