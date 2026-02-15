package pl.manager.library.gui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.manager.library.model.Book;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class GUI implements IGUI {
    private final Scanner scanner;
    private final List<String> USER_OPTIONS = List.of("0", "1", "2", "3");
    private final List<String> ADMIN_OPTIONS = Stream.concat(
            USER_OPTIONS.stream(),
            Stream.of("4", "5", "6", "7", "8")
    ).toList();

    private void showCommonMenu() {
        System.out.println("0. Exit");
        System.out.println("1. View Books");
        System.out.println("2. Search books by author");
        System.out.println("3. Search books by title");
    }

    private void showUserMenu() {
        showCommonMenu();
    }

    private void showAdminMenu() {
        showCommonMenu();
        System.out.println("4. Add Book");
        System.out.println("5. Remove Book");
        System.out.println("6. Edit Book");
        System.out.println("7. Add User");
        System.out.println("8. View Users");
    }

    @Override
    public User readUserData() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user password: ");
        String password = scanner.nextLine();
        return new User(name, password);
    }

    @Override
    public void showMenuForRole(Role role) {
        System.out.println("------------------------");
        if (role == Role.ADMIN) {
            this.showAdminMenu();
        } else {
            this.showUserMenu();
        }
    }

    @Override
    public String readUserChoice() {
        return scanner.nextLine();
    }

    @Override
    public boolean isUserChoiceValid(String choice, Role role) {
        try {
            if (role == Role.ADMIN) {
                return ADMIN_OPTIONS.contains(choice);
            }
            return USER_OPTIONS.contains(choice);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void showBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (Book book : books) {
                System.out.println(book.getId() + " " + book.getAuthor() + " - " + book.getTitle());
            }
        }
    }

    @Override
    public Integer readBookId(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        try {
            int id = Integer.parseInt(input);
            if (id < 0) {
                System.out.println("Book ID cannot be negative.");
                return null;
            }
            return id;
        } catch (NumberFormatException e) {
            System.out.println("Invalid book ID. Please enter a valid number.");
            return null;
        }
    }

    @Override
    public Book readBookData() {
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        if (author.isBlank() || title.isBlank()) {
            System.out.println("Author and title cannot be empty!");
            return null;
        }
        return new Book(title, author);
    }

    @Override
    public void showUsers(List<User> users) {
        for (User user : users) {
            System.out.println(user.getId() + " " + user.getLogin());
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

}