package pl.manager.library.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.manager.library.authentication.IAuthenticator;
import pl.manager.library.database.IBookRepository;
import pl.manager.library.database.IUserRepository;
import pl.manager.library.gui.IGUI;
import pl.manager.library.model.Book;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Core implements ICore {
    private final IGUI gui;
    private final IAuthenticator authenticator;
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;

    @Override
    public void run() {
        while (true) {
            User userData = gui.readUserData();
            User authenticatedUser = authenticator.authenticate(userData);

            if (authenticatedUser == null) {
                gui.showMessage("Error logging in - try again");
                continue;
            }

            handleMenu(authenticatedUser);
            break;
        }
    }

    private void handleMenu(User user) {

        while (true) {
            gui.showMenuForRole(user.getRole());
            String choice = gui.readUserChoice();

            if (!gui.isUserChoiceValid(choice, user.getRole())) {
                gui.showMessage("Wrong choice, try again.");
            }

            if (choice.equals("0")) {
                exit();
                break;
            }

            executeAction(choice);
        }
    }

    private void executeAction(String choice) {
        switch (choice) {
            case "1" -> showAllBooks();
            case "2" -> searchByAuthor();
            case "3" -> searchByTitle();
            case "4" -> addNewBook();
            case "5" -> deleteBook();
            case "6" -> modifyBook();
            case "7" -> addNewUser();
            case "8" -> viewUsers();
        }
    }

    private void exit() {
        gui.showMessage("Logged out.");
    }

    private void showAllBooks() {
        List<Book> allBooks = bookRepository.getAllBooks();
        gui.showBooks(allBooks);
    }

    private void searchByAuthor() {
        gui.showMessage("Specify author:");
        String author = gui.readUserChoice();
        List<Book> found = bookRepository.findByAuthor(author);
        gui.showBooks(found);
    }

    private void searchByTitle() {
        gui.showMessage("Specify title:");
        String title = gui.readUserChoice();
        List<Book> found = bookRepository.findByTitle(title);
        gui.showBooks(found);
    }

    private void addNewBook() {
        Book book = gui.readBookData();
        if (book != null) {
            bookRepository.addBook(book.getAuthor(), book.getTitle());
            gui.showMessage("Book added.");
        }
    }

    private void deleteBook() {
        Integer id = gui.readBookId("Enter ID of the book to delete:");
        if (id == null) return;

        boolean success = bookRepository.deleteBook(id);
        if (success) {
            gui.showMessage("Deleted.");
        } else {
            gui.showMessage("Book with given ID not found.");
        }
    }

    private void modifyBook() {
        Integer id = gui.readBookId("Enter ID of the book to edit:");
        if (id == null) return;

        Book bookToEdit = bookRepository.getBookById(id);
        if (bookToEdit == null) {
            gui.showMessage("Book with given ID not found.");
            return;
        }

        Book newData = gui.readBookData();
        if (newData != null) {
            bookToEdit.setAuthor(newData.getAuthor());
            bookToEdit.setTitle(newData.getTitle());
            bookRepository.updateBook(bookToEdit);
            gui.showMessage("Updated successfully.");
        }
    }

    private void addNewUser() {
        User userData = gui.readUserData();
        userRepository.addUser(
            userData.getLogin(),
            userData.getPassword(),
            Role.USER
        );
    }

    private void viewUsers() {
        List<User> users = userRepository.getUsers();
        gui.showUsers(users);
    }
}