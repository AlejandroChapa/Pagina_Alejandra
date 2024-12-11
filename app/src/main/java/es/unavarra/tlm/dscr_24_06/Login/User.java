package es.unavarra.tlm.dscr_24_06.Login;

public class User {
    private String username;
    private String password;

    // Constructor
    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    // Getters y Setters
    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}