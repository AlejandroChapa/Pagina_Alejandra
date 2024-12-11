package es.unavarra.tlm.dscr_24_06.Login;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionManager {
    private static final String PREF_NAME = "SessionPrefs";     // Nombre del archivo SharedPreferences
    private static final String KEY_LOGIN_DATE = "login_date";  // Clave para guardar la hora de inicio de sesión
    private static final String KEY_TOKEN = "session_token";    // Clave para guardar el token
    private final SharedPreferences sharedPreferences;          // Objeto para acceder a SharedPreferences
    private final SharedPreferences.Editor editor;              // Editor para escribir en SharedPreferences

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Guardar el token
    public void saveSessionToken(String token) {
        // Guardar el token
        editor.putString(KEY_TOKEN, token);

        // Obtener la hora actual
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        editor.putString(KEY_LOGIN_DATE, currentDateTime);

        // Aplicar los cambios
        editor.commit();
    }

    // Obtener la fecha/hora de inicio de sesión
    public String getLoginDate() {
        return sharedPreferences.getString(KEY_LOGIN_DATE, null);
    }

    // Obtener el token
    public String getSession() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Borrar el token
    public void clearSession() {
        editor.remove(KEY_TOKEN);
        editor.commit();
    }

    // Verificar si hay una sesión activa
    public boolean isSessionActive() {
        return getSession() != null;
    }

    // Extraer el 'user.id' del JSON completo
    public String extractUserIdFromSession(String sessionToken) {
        try {
            // Parsear el JSON
            JSONObject jsonResponse = new JSONObject(sessionToken);
            // Extraer el user desde el objeto 'user'
            JSONObject user = jsonResponse.getJSONObject("user");
            return user.getString("id"); // Extraer el valor de 'id'
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Si ocurre un error al parsear el JSON
        }
    }

    // Extraer el 'user.username' del JSON completo
    public String extractUsernameFromSession(String sessionToken) {
        try {
            // Parsear el JSON
            JSONObject jsonResponse = new JSONObject(sessionToken);
            // Extraer el user desde el objeto 'user'
            JSONObject user = jsonResponse.getJSONObject("user");
            return user.getString("username"); // Extraer el valor de 'username'
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Si ocurre un error al parsear el JSON
        }
    }

    // Extraer el 'session.token' del JSON completo
    public String extractTokenFromSession(String sessionToken) {
        try {
            // Parsear el JSON
            JSONObject jsonResponse = new JSONObject(sessionToken);
            // Extraer el token desde el objeto 'session'
            JSONObject session = jsonResponse.getJSONObject("session");
            return session.getString("token"); // Extraer el valor de 'token'
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Si ocurre un error al parsear el JSON
        }
    }

    // Extraer el 'session.valid_until' del JSON completo
    public String extractValidUntilFromSession(String sessionToken) {
        try {
            // Parsear el JSON
            JSONObject jsonResponse = new JSONObject(sessionToken);
            // Extraer el session desde el objeto 'session'
            JSONObject session = jsonResponse.getJSONObject("session");
            return session.getString("valid_until"); // Extraer el valor de 'valid_until'
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Si ocurre un error al parsear el JSON
        }
    }

}