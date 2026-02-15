package pl.manager.library.database;

import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository implements IUserRepository {
    @Getter
    private final List<User> users = new ArrayList<>();
    private int idCounter = 0;

    public UserRepository() {
        this.addUser("admin", "admin", Role.ADMIN);
        this.addUser("user", "user", Role.USER);
    }

    public void addUser(String login, String password, Role role) throws IllegalArgumentException {
        if (login.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Password and login cannot be empty.");
        }

        if (getUserByLogin(login) != null) {
            throw new IllegalArgumentException("User with login '" + login + "' already exists.");
        }

        users.add(new User(login, DigestUtils.md5Hex(password), role, idCounter++));
    }

    @Override
    public User getUserByLogin(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login) ) {
                return user;
            }
        }
        return null;
    }
}