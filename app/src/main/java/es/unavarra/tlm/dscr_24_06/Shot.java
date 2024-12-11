package es.unavarra.tlm.dscr_24_06;

import es.unavarra.tlm.dscr_24_06.Juego.Ships;

public class Shot {
    private String result;
    private Ships.Position position;

    public String getResult() {
        return result;
    }

    public Ships.Position getPosition() {
        return position;
    }

    public void setPosition(Ships.Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "shot {" +
                "result: " + result +
                ", position: " + position +
            '}';
        }
    }
