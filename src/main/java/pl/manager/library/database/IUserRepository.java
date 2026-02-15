package pl.manager.library.database;

import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.List;

public interface IUserRepository {
    User getUserByLogin(String login);
    void addUser(String login, String password, Role role);
    List<User> getUsers();
}