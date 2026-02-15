package pl.manager.library.gui;

import pl.manager.library.model.Book;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.List;

public interface IGUI {
    User readUserData();
    void showMenuForRole(Role role);
    String readUserChoice();
    boolean isUserChoiceValid(String choice, Role role);
    void showBooks(List<Book> books);
    Integer readBookId(String prompt);
    Book readBookData();
    void showUsers(List<User> users);
    void showMessage(String message);
}