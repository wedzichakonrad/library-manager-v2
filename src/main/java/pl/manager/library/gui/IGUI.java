package pl.manager.library.gui;

import pl.manager.library.model.Book;
import pl.manager.library.model.Category;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.List;
import java.util.Map;

public interface IGUI {
    User readUserData();
    void showMenuForRole(Role role);
    String readUserChoice();
    boolean isUserChoiceValid(String choice, Role role);
    void showBooks(List<Book> books, List<Category> categories);
    Integer readBookId(String prompt);
    Book readBookData();
    void showUsers(List<User> users);
    void showMessage(String message);
    void showStatistics(Map<String, Integer> stats);
    void showPopularBooks(Map<String, Integer> popularBooks);
    void showCategories(List<Category> categories);
    Integer readCategoryId(String prompt);
    String readCategoryName();
}