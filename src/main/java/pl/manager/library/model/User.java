package pl.manager.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private int id;
    private final String login;
    private final String password;
    private Role role;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User (String login, String password, Role role, int id) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }
}