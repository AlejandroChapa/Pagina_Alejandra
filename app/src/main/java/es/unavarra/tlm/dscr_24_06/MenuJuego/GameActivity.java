package es.unavarra.tlm.dscr_24_06.MenuJuego;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import es.unavarra.tlm.dscr_24_06.Login.LogoutResponseHandler;
import es.unavarra.tlm.dscr_24_06.Login.MainActivity;
import es.unavarra.tlm.dscr_24_06.Login.SessionManager;
import es.unavarra.tlm.dscr_24_06.Juego.MainActivityBarcos;
import es.unavarra.tlm.dscr_24_06.Perfil.RespuestaPerfil;
import es.unavarra.tlm.dscr_24_06.R;
import es.unavarra.tlm.dscr_24_06.Tablero;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {


    private EditText editText,editText1;
    private String userInput,userInput1;
    private GameAdapter gameAdapter, inactiveGameAdapter;
    private Rechazar rechazarinvitacion;
    private Aceptar aceptarinvitacion;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Usar SessionManager para obtener el token y el username
        SessionManager sessionManager = new SessionManager(this);
        String sesion = sessionManager.getSession();
        //String Nombre = sessionManager.extractUsernameFromSession(sesion);
        String userid = sessionManager.extractUserIdFromSession(sesion);

        String token = sessionManager.extractTokenFromSession(sesion); // Extraer el token

        if(!sessionManager.isSessionActive()){
            Intent intent = new Intent(GameActivity.this, MainActivity.class );
            startActivity(intent);
        }

        // Inicializar las vistas
        editText = findViewById(R.id.editText);
        editText1 = findViewById(R.id.editText1);
        Button btnInvite = findViewById(R.id.btn_invite);
        Button btnPerfil = findViewById(R.id.btn_perfil);
        Button btnActualizar = findViewById(R.id.btnActualizar);
        Button btnBuscar = findViewById(R.id.buscar);
        ListView listViewActiveGames = findViewById(R.id.listViewActiveGames);
        ListView listViewInactiveGames = findViewById(R.id.listViewInactiveGames);
        rechazarinvitacion = new Rechazar();
        aceptarinvitacion = new Aceptar();




        // Configurar el adaptador para las partidas activas
        List<Game> initialGameList = new ArrayList<>();
        gameAdapter = new GameAdapter(this, initialGameList);
        listViewActiveGames.setAdapter(gameAdapter);
        List<Game> initialGameList1 = new ArrayList<>();
        inactiveGameAdapter = new GameAdapter(this, initialGameList1);
        listViewInactiveGames.setAdapter(inactiveGameAdapter);


        // Configurar el botón de invitar
        btnInvite.setOnClickListener(v -> {
            // Guarda lo que el usuario escribió en la variable
            userInput = editText.getText().toString();

            // Verificar que el 'username' no esté vacío
            if (userInput.isEmpty()) {
                Toast.makeText(GameActivity.this, "Por favor, ingresa un nombre de usuario", Toast.LENGTH_SHORT).show();
                return;
            }

            // Paso 2: Llamar a la función sendChallenge() para enviar el desafío
            ChallengePlayer challengePlayer = new ChallengePlayer();
            challengePlayer.sendChallenge(GameActivity.this, userInput); // Pasa el contexto y el username
            new Handler().postDelayed(() -> {
                getActiveGames();
                getInactiveGames();
            }, 500); // Espera 500ms antes de actualizar
            // Paso 3: Mostrar un Toast indicando que el desafío ha sido enviado
            Toast.makeText(GameActivity.this, "Desafío enviado a: " + userInput, Toast.LENGTH_SHORT).show();
        });
        // Configuracion el botonn de MOstart perfil.
        btnPerfil.setOnClickListener(v -> {
            //Vamos a mandar un
            RespuestaPerfil respuestaPerfil = new RespuestaPerfil();

            // Simular valores de prueba para gameId y token

            // Llamar al método para obtener y mostrar el perfil
            respuestaPerfil.RespuestaMiPerfil(GameActivity.this, userid, token);
            //Toast.makeText(GameActivity.this, "Funciona?", Toast.LENGTH_SHORT).show();
        });
        //Para buscar por nombre de usuario
        btnBuscar.setOnClickListener(v ->{
            userInput1 = editText1.getText().toString();
            // Verificar que el 'username' no esté vacío
            if (userInput1.isEmpty()) {
                Toast.makeText(GameActivity.this, "Por favor, ingresa un nombre de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "ew:"+userInput1, Toast.LENGTH_SHORT).show();
            getActiveGames1();
        });

        // Obtener las partidas activas
        getActiveGames();
        getInactiveGames();
        btnActualizar.setOnClickListener(view ->{
                new AlertDialog.Builder(GameActivity.this)
                .setTitle(" Quieres Actulizar tus listas:.")
                .setPositiveButton("Aceptarala", (dialog, which) -> {
                    //Aceptar la invitacion
                    new Handler().postDelayed(() -> {
                        getActiveGames();
                        getInactiveGames();
                    }, 500); // Espera 500ms antes de actualizar
                })
                .setNegativeButton("Rechazar", (dialog, which) -> {})
                .show();
        });


        //Cuando tocas algo de la lista activa:
        listViewActiveGames.setOnItemClickListener((parent, view, position, id) -> {
            Game selectedGame = gameAdapter.getItem(position);
            //Para rechazar o aceptar la invitacion o jugar
            if (selectedGame != null) {
                switch (selectedGame.getState()) {
                    case "waiting":
                        if (selectedGame.isYourTurn()) {
                            //Aqui entras cuando debes aceptar o rechazar una partida
                            String gameIdAsString = String.valueOf(selectedGame.getId());
                            new AlertDialog.Builder(this)
                                    .setTitle(selectedGame.getEnemy().getUsername() + " te a invitado a jugar.")
                                    .setMessage("¿Quieres Aceptarala o Rechazar?")
                                    .setPositiveButton("Aceptarala", (dialog, which) -> {
                                        //Aceptar la invitacion
                                        aceptarinvitacion.AceptarInvitacion(this, gameIdAsString, token);
                                        new Handler().postDelayed(() -> {
                                            getActiveGames();
                                            getInactiveGames();
                                        }, 1000); // Espera 1s antes de actualizar
                                    })
                                    .setNegativeButton("Rechazar", (dialog, which) -> {
                                        // Rechazar el juego si el usuario confirma
                                        rechazarinvitacion.rechazarJuego(this, gameIdAsString, token);
                                        new Handler().postDelayed(() -> {
                                            getActiveGames();
                                            getInactiveGames();
                                        }, 1000); // Espera 1s antes de actualizar
                                    })
                                    .show();
                        } 
                        else{
                            Toast.makeText(this, "Tienes que esperar que acepte tu rival", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "placing":
                        if(selectedGame.isYourTurn()){
                            String gameIdAsString = String.valueOf(selectedGame.getId());
                            Intent intent = new Intent(GameActivity.this, MainActivityBarcos.class);
                            // Pasa la variable gameIdString como un extra en el Intent
                            intent.putExtra("GAME_ID", gameIdAsString);
                            startActivity(intent);
                            new Handler().postDelayed(() -> {
                                getActiveGames();
                                getInactiveGames();
                            }, 500); // Espera 500ms antes de actualizar
                        }
                        else{
                            Toast.makeText(this, "Ya has colocado tus barcos espera a que tu rival los coloque para emepzar la partida.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "started":
                        if(!selectedGame.isYourTurn()){
                            new AlertDialog.Builder(this)
                                    .setTitle("Estas jugando contra "+selectedGame.getEnemy().getUsername() + ":")
                                    .setMessage("Aunque es el turno de tu rival ¿Quieres ver como va la partida?")
                                    .setPositiveButton("Aceptar", (dialog, which) -> {
                                        //Aceptar la invitacion
                                        String Enemy = String.valueOf(selectedGame.getEnemy().getId());
                                        String gameID = String.valueOf(selectedGame.getId());
                                        Intent intent1 = new Intent(GameActivity.this, Tablero.class);
                                        String disparo = "N";
                                        intent1.putExtra("EnemyID", Enemy);
                                        intent1.putExtra("token", token);
                                        intent1.putExtra("gameid", gameID);
                                        intent1.putExtra("disparo", disparo);
                                        startActivity(intent1);

                                        new Handler().postDelayed(() -> {
                                            getActiveGames();
                                            getInactiveGames();
                                        }, 500); // Espera 500ms antes de actualizar
                                    })
                                    .setNegativeButton("Rechazar", (dialog, which) -> {
                                        // Rechazar el juego si el usuario confirma


                                        new Handler().postDelayed(() -> {
                                            getActiveGames();
                                            getInactiveGames();
                                        }, 500); // Espera 500ms antes de actualizar
                                        //dialog.dismiss();
                                    })
                                    .show();
                        }
                        else{
                            new AlertDialog.Builder(this)
                                    .setTitle(selectedGame.getEnemy().getUsername() + " te a invitado a jugar.")
                                    .setMessage("¿Es tu turno quieres hacer un movimineto?")
                                    .setPositiveButton("Aceptar", (dialog, which) -> {
                                        //Aceptar la invitacion
                                        String Enemy = String.valueOf(selectedGame.getEnemy().getId());
                                        String gameID = String.valueOf(selectedGame.getId());
                                        Intent intent1 = new Intent(GameActivity.this, Tablero.class);
                                        String disparo = "Y";
                                        intent1.putExtra("disparo", disparo);
                                        intent1.putExtra("EnemyID", Enemy);
                                        intent1.putExtra("token", token);
                                        intent1.putExtra("gameid", gameID);
                                        startActivity(intent1);



                                    })
                                    .setNegativeButton("Rechazar", (dialog, which) -> {
                                        // Rechazar el juego si el usuario confirma


                                        new Handler().postDelayed(() -> {
                                            getActiveGames();
                                            getInactiveGames();
                                        }, 500); // Espera 500ms antes de actualizar
                                        //dialog.dismiss();
                                    })
                                    .show();

                        }

                        //AQUI
                        break;

                    default:
                        Toast.makeText(this, "NO has selecionado una casilla correcta.", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

    }

    private void getActiveGames() {
        // Obtener el token de sesión

        SessionManager sessionManager = new SessionManager(this);
        String sessionToken = sessionManager.getSession(); // Obtener el token (que es un JSON)
        // Extraer el token desde el JSON
        String token = sessionManager.extractTokenFromSession(sessionToken);

        if (token == null) {
            Toast.makeText(this, "Token no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Configurar el cliente HTTP
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Authentication", token); // Usar el token extraído en el encabezado

        // Hacer la solicitud GET
        client.get(this, "https://api.battleship.tatai.es/v2/game/active", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    // Obtener la lista de juegos activos desde la respuesta JSON
                    JSONArray gamesArray = response.getJSONArray("games");

                    // Mostrar la respuesta en Logcat
                    Log.d("ActiveGames", "Juegos Activos: " + gamesArray);

                    // Usar Gson para convertir el JSON en una lista de objetos Game
                    Gson gson = new Gson();
                    Type gameListType = new TypeToken<List<Game>>() {}.getType();
                    List<Game> gamesList_Active = gson.fromJson(gamesArray.toString(), gameListType);

                    // Limpiar los datos previos de los juegos activos (esto es clave)


                    // Guardar los juegos activos en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String gamesJsonStringActive = gson.toJson(gamesList_Active);
                    editor.putString("activeGamesArray", gamesJsonStringActive); // Clave para juegos activos
                    editor.apply();

                    // Actualizar el adaptador con los datos de las partidas activas
                    gameAdapter.updateGames(gamesList_Active);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(GameActivity.this, "Error al procesar los juegos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(GameActivity.this, "Error al obtener las partidas activas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getActiveGames1() {
        // Obtener el token de sesión

        SessionManager sessionManager = new SessionManager(this);
        String sessionToken = sessionManager.getSession(); // Obtener el token (que es un JSON)
        // Extraer el token desde el JSON
        String token = sessionManager.extractTokenFromSession(sessionToken);

        if (token == null) {
            Toast.makeText(this, "Token no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Configurar el cliente HTTP
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Authentication", token); // Usar el token extraído en el encabezado

        // Hacer la solicitud GET
        client.get(this, "https://api.battleship.tatai.es/v2/game/active", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    // Obtener la lista de juegos activos desde la respuesta JSON
                    JSONArray gamesArray = response.getJSONArray("games");

                    // Mostrar la respuesta en Logcat
                    Log.d("ActiveGames", "Juegos Activos: " + gamesArray);

                    // Usar Gson para convertir el JSON en una lista de objetos Game
                    Gson gson = new Gson();
                    Type gameListType = new TypeToken<List<Game>>() {}.getType();
                    List<Game> gamesList_Active = gson.fromJson(gamesArray.toString(), gameListType);
                    List<Game> List = new ArrayList<>();
                    for(Game games : gamesList_Active){
                        if (Objects.equals(games.getEnemy().getUsername(), userInput1)){
                            List.add(games);
                        }
                    }
                    Toast.makeText(GameActivity.this, ""+List.size(), Toast.LENGTH_SHORT).show();
                    gameAdapter.updateGames(List);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(GameActivity.this, "Error al procesar los juegos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(GameActivity.this, "Error al obtener las partidas activas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getInactiveGames() {
        // Obtener el token de sesión
        SessionManager sessionManager = new SessionManager(this);
        String sessionToken = sessionManager.getSession(); // Obtener el token (que es un JSON)
        // Extraer el token desde el JSON
        String token = sessionManager.extractTokenFromSession(sessionToken);

        if (token == null) {
            Toast.makeText(this, "Token no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Configurar el cliente HTTP
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Authentication", token); // Usar el token extraído en el encabezado

        // Hacer la solicitud GET
        client.get(this, "https://api.battleship.tatai.es/v2/game/inactive", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    // Obtener la lista de juegos inactivos desde la respuesta JSON
                    JSONArray gamesArray = response.getJSONArray("games");

                    // Mostrar la respuesta en Logcat
                    Log.d("InActiveGames", "Juegos Inactivos: " + gamesArray);

                    // Usar Gson para convertir el JSON en una lista de objetos Game
                    Gson gson = new Gson();
                    Type gameListType = new TypeToken<List<Game>>() {}.getType();
                    List<Game> gamesList_Inactive = gson.fromJson(gamesArray.toString(), gameListType);
                    for(Game list :gamesList_Inactive){
                        if(Objects.equals(list.getState(), "finished")){

                            Toast.makeText(GameActivity.this, "wqwqeqwe"+ list.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                        } else if (Objects.equals(list.getState(), "finished") && !list.isYourTurn()) {
                            Toast.makeText(GameActivity.this, "111", Toast.LENGTH_SHORT).show();

                        }

                    }
                    // Limpiar los datos previos de los juegos inactivos (esto es clave)

                    // Guardar los juegos inactivos en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String gamesJsonStringInactive = gson.toJson(gamesList_Inactive);
                    editor.putString("inactiveGamesArray", gamesJsonStringInactive); // Clave para juegos inactivos
                    editor.apply();

                    // Actualizar el adaptador con los datos de las partidas inactivas
                    inactiveGameAdapter.updateGames(gamesList_Inactive);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(GameActivity.this, "Error al procesar los juegos", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(GameActivity.this, "Error al obtener las partidas inactivas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú del toolbar
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar el clic del botón de salir
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Crear una instancia de LogoutResponseHandler
        LogoutResponseHandler logoutHandler = new LogoutResponseHandler(this);
        // Llamar al método logout
        logoutHandler.logout();
        // Finalizar la actividad actual
        // Obtener las preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Limpiar las preferencias relacionadas con la sesión
        editor.remove("activeGamesArray");
        editor.remove("inactiveGamesArray");
        editor.remove("sessionToken");  // Si guardas el token de sesión
        editor.apply();


        // También puedes eliminar cualquier otro dato guardado (por ejemplo, el nombre de usuario

        finish();
    }
}
