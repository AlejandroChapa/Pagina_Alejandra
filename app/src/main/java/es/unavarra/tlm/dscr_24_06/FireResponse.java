package es.unavarra.tlm.dscr_24_06;


import es.unavarra.tlm.dscr_24_06.Juego.Ships;
import es.unavarra.tlm.dscr_24_06.MenuJuego.Game;

public class FireResponse {
    private Shot shot;
    private Game game;
    private Ships ship;
    private Gunfire gunfire;

    public Shot getShot() {
        return shot;
    }

    public Game getGame() {
        return game;
    }

    public Ships getShip() {
        return ship;
    }

    public Gunfire getGunfire() {
        return gunfire;
    }


}
