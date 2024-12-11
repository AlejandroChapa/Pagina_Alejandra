package es.unavarra.tlm.dscr_24_06.MenuJuego;


import java.util.List;

import es.unavarra.tlm.dscr_24_06.Juego.Ships;

public class Game {


    private int id;
    private String state;
    private User1 you;
    private User1 enemy;
    private boolean your_turn;
    private String updated_at;
    private String created_at;
    private List<Ships> ships; // Si no necesitas los detalles de los barcos
    private Gunfire gunfire; // Clase para los detalles de los disparos (puedes simplificarla según sea necesario)
    private boolean notificacion;
    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public User1 getYou() { return you; }
    public void setYou(User1 you) { this.you = you; }

    public User1 getEnemy() { return enemy; }
    public void setEnemy(User1 enemy) { this.enemy = enemy; }

    public boolean isYourTurn() { return your_turn; }
    public void setYourTurn(boolean your_turn) { this.your_turn = your_turn; }



    public String getUpdatedAt() { return updated_at; }
    public void setUpdatedAt(String updated_at) { this.updated_at = updated_at; }

    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }

    public List<Ships> getShips() { return ships; }  // Cambiado a List<Ships>
    public void setShips(List<Ships> ships) { this.ships = ships; }

    public Gunfire getGunfire() { return gunfire; }
    public void setGunfire(Gunfire gunfire) { this.gunfire = gunfire; }

    public static class Gunfire {
        private List<Object> done;
        private List<Object> received;

        public List<Object> getDone() { return done; }
        public void setDone(List<Object> done) { this.done = done; }

        public List<Object> getReceived() { return received; }
        public void setReceived(List<Object> received) { this.received = received; }
    }
}



class User1 {
    private int id;
    private String username;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
