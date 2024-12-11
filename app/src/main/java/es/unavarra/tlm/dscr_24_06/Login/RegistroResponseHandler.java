package es.unavarra.tlm.dscr_24_06.Login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import es.unavarra.tlm.dscr_24_06.MenuJuego.GameActivity;

public class RegistroResponseHandler extends AsyncHttpResponseHandler {

    private Context context;

    // Constructor que recibe el contexto
    public RegistroResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int httpCode, Header[] headers, byte[] responseBody) {
        // Procesar la respuesta exitosa
        String response = new String(responseBody);

        // Usar SessionManager para guardar el token
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.saveSessionToken(response);

        // Obtener la hora de inicio de sesión


        // Mostrar mensaje de éxito
        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show();

        // Redirigir a GameActivity
        Intent intent = new Intent(context, GameActivity.class);
        context.startActivity(intent);

        // Cerrar la actividad actual (opcional)
        if (context instanceof RegistroActivity) {
            ((RegistroActivity) context).finish();
        }
    }

    @Override
    public void onFailure(int httpCode, Header[] headers, byte[] responseBody, Throwable error) {
        // Procesar la respuesta fallida
        String response = responseBody != null ? new String(responseBody) : "Error desconocido";
        Toast.makeText(context, "Fallo en el registro: " + response, Toast.LENGTH_SHORT).show();
    }
}
