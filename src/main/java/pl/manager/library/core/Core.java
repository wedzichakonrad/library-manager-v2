package pl.manager.library.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.manager.library.authentication.IAuthenticator;
import pl.manager.library.database.*;
import pl.manager.library.gui.IGUI;
import pl.manager.library.model.Book;
import pl.manager.library.model.Category;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Core implements ICore {
    private final IGUI gui;
    private final IAuthenticator authenticator;
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;
    private final IRentalRepository rentalRepository;
    private final IStatisticsRepository statisticsRepository;
    private final ICategoryRepository categoryRepository;

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
                continue;
            }

            if (choice.equals("0")) {
                exit();
                break;
            }

            executeAction(choice, user);
        }
    }

    private void executeAction(String choice, User user) {
        switch (choice) {
            case "1" -> showAllBooks();
            case "2" -> searchByAuthor();
            case "3" -> searchByTitle();
            case "4" -> addNewBook();
            case "5" -> deleteBook();
            case "6" -> modifyBook();
            case "7" -> addNewUser();
            case "8" -> viewUsers();
            case "9" -> rentBook(user);
            case "10" -> returnBook(user);
            case "11" -> viewMyRentals(user);
            case "12" -> showStatistics();
            case "13" -> viewCategories();
            case "14" -> searchByCategory();
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
            viewCategories();
            Integer catId = gui.readCategoryId("Enter category ID (or 0 to skip): ");
            book.setCategoryId(catId);

            bookRepository.addBook(book);
            gui.showMessage("Book added.");
        }
    }

    private void deleteBook() {
        Integer id = gui.readBookId("Enter ID of the book to delete: ");
        if (id == null) return;

        boolean success = bookRepository.deleteBook(id);
        if (success) {
            gui.showMessage("Deleted.");
        } else {
            gui.showMessage("Book with given ID not found.");
        }
    }

    private void modifyBook() {
        Integer id = gui.readBookId("Enter ID of the book to edit: ");
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
            bookToEdit.setYear(newData.getYear());

            viewCategories();
            Integer catId = gui.readCategoryId("Enter new category ID (or 0 to set to null): ");
            bookToEdit.setCategoryId(catId);

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
        gui.showMessage("User added.");
    }

    private void viewUsers() {
        List<User> users = userRepository.getUsers();
        gui.showUsers(users);
    }

    private void rentBook(User user) {
        Integer id = gui.readBookId("Enter ID of the book to rent: ");
        if (id == null) return;

        boolean success = rentalRepository.rentBook(id, user.getId());
        if (success) {
            gui.showMessage("Book rented successfully.");
        } else {
            gui.showMessage("Failed to rent book. It might not exist or is already rented.");
        }
    }

    private void returnBook(User user) {
        Integer id = gui.readBookId("Enter ID of the book to return: ");
        if (id == null) return;

        boolean success = rentalRepository.returnBook(id, user.getId());
        if (success) {
            gui.showMessage("Book returned successfully.");
        } else {
            gui.showMessage("Failed to return book. It might not be rented by you.");
        }
    }

    private void viewMyRentals(User user) {
        List<Book> rentedBooks = rentalRepository.getRentedBooksByUser(user.getId());
        gui.showMessage("--- My Rentals ---");
        gui.showBooks(rentedBooks);
    }

    private void showStatistics() {
        Map<String, Integer> stats = statisticsRepository.getStatistics();
        gui.showStatistics(stats);

        Map<String, Integer> popularBooks = statisticsRepository.getMostPopularBooks(5);
        gui.showPopularBooks(popularBooks);
    }

    private void viewCategories() {
        List<Category> categories = categoryRepository.getAllCategories();
        gui.showCategories(categories);
    }

    private void searchByCategory() {
        viewCategories();
        Integer catId = gui.readCategoryId("Enter Category ID to search: ");
        if (catId != null) {
            List<Book> found = bookRepository.findByCategory(catId);
            gui.showBooks(found);
        }
    }
}