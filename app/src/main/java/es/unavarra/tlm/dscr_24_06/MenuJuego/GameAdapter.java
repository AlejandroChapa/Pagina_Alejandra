package es.unavarra.tlm.dscr_24_06.MenuJuego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.unavarra.tlm.dscr_24_06.R;

public class GameAdapter extends ArrayAdapter<Game> {

    public GameAdapter(Context context, List<Game> games) {
        super(context, 0, games);
    }

    public void updateGames(List<Game> games) {
        clear();
        addAll(games);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_game, parent, false);
        }

        // Obtener el objeto Game en la posición actual
        Game game = getItem(position);


        // Referenciar los elementos del layout
        TextView textViewChallenge = convertView.findViewById(R.id.textViewChallenge);
        TextView textViewStatus = convertView.findViewById(R.id.textViewStatus);
        TextView textViewTurn = convertView.findViewById(R.id.textViewTurn);

        // Asignar valores a los elementos del layout
        String challenge = game.getYou().getUsername() + " vs " + game.getEnemy().getUsername();
        String status = game.getState();
        String turn = game.isYourTurn() ? "Tu turno" : "Turno enemigo";

        textViewChallenge.setText(challenge);
        textViewStatus.setText(status);
        textViewTurn.setText(turn);

        return convertView;
    }
}
