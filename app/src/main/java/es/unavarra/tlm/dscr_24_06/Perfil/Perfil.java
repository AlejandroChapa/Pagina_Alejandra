package es.unavarra.tlm.dscr_24_06.Perfil;


import com.google.gson.Gson;

public class Perfil {
    private User user;
    private Account account;
    private Stats stats;

    // Getters y setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    // Clase interna para User
    public static class User {
        private int id;
        private String username;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    // Clase interna para Account
    public static class Account {
        private String created_at;

        public String getCreatedAt() {
            return created_at;
        }

        public void setCreatedAt(String created_at) {
            this.created_at = created_at;
        }
    }

    // Clase interna para Stats
    public static class Stats {
        private int games;
        private int wins;
        private int losses;
        private int wins_by_surrender;
        private int losses_by_surrender;

        public int getGames() {
            return games;
        }

        public void setGames(int games) {
            this.games = games;
        }

        public int getWins() {
            return wins;
        }

        public void setWins(int wins) {
            this.wins = wins;
        }

        public int getLosses() {
            return losses;
        }

        public void setLosses(int losses) {
            this.losses = losses;
        }

        public int getWinsBySurrender() {
            return wins_by_surrender;
        }

        public void setWinsBySurrender(int wins_by_surrender) {
            this.wins_by_surrender = wins_by_surrender;
        }

        public int getLossesBySurrender() {
            return losses_by_surrender;
        }

        public void setLossesBySurrender(int losses_by_surrender) {
            this.losses_by_surrender = losses_by_surrender;
        }
    }

    // Método para convertir JSON a objeto UserInfo
    public static Perfil fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Perfil.class);
    }

    // Método para transmitir información
    public void transmitInfo() {
        System.out.println("User Info:");
        System.out.println("ID: " + user.getId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Account Created At: " + account.getCreatedAt());
        System.out.println("Games Played: " + stats.getGames());
        System.out.println("Wins: " + stats.getWins());
        System.out.println("Losses: " + stats.getLosses());
        System.out.println("Wins by Surrender: " + stats.getWinsBySurrender());
        System.out.println("Losses by Surrender: " + stats.getLossesBySurrender());
    }
}