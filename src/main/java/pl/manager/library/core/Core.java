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
            case "4" -> rentBook(user);
            case "5" -> returnBook(user);
            case "6" -> viewMyRentals(user);
            case "7" -> searchByCategory();
            case "8" -> addNewBook();
            case "9" -> deleteBook();
            case "10" -> modifyBook();
            case "11" -> addNewUser();
            case "12" -> viewUsers();
            case "13" -> showStatistics();
            case "14" -> addNewCategory();
            case "15" -> modifyCategory();
            case "16" -> deleteCategory();
        }
    }

    private void exit() {
        gui.showMessage("Logged out.");
    }

    private void showAllBooks() {
        List<Category> categories = categoryRepository.getAllCategories();
        gui.showCategories(categories);
        System.out.println();
        List<Book> allBooks = bookRepository.getAllBooks();
        gui.showBooks(allBooks, categories);
    }

    private void searchByAuthor() {
        gui.showMessage("Specify author:");
        String author = gui.readUserChoice();
        List<Book> found = bookRepository.findByAuthor(author);
        gui.showBooks(found, categoryRepository.getAllCategories());
    }

    private void searchByTitle() {
        gui.showMessage("Specify title:");
        String title = gui.readUserChoice();
        List<Book> found = bookRepository.findByTitle(title);
        gui.showBooks(found, categoryRepository.getAllCategories());
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
        gui.showBooks(rentedBooks, categoryRepository.getAllCategories());
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
            gui.showBooks(found, categoryRepository.getAllCategories());
        }
    }

    private void addNewCategory() {
        String name = gui.readCategoryName();
        if (name != null && !name.trim().isEmpty()) {
            categoryRepository.addCategory(name.trim());
            gui.showMessage("Category added.");
        } else {
            gui.showMessage("Category name cannot be empty.");
        }
    }

    private void modifyCategory() {
        viewCategories();
        Integer id = gui.readCategoryId("Enter ID of the category to edit: ");
        if (id == null) return;

        Category category = categoryRepository.getCategoryById(id);
        if (category == null) {
            gui.showMessage("Category not found.");
            return;
        }

        String newName = gui.readCategoryName();
        if (newName != null && !newName.trim().isEmpty()) {
            category.setName(newName.trim());
            if (categoryRepository.updateCategory(category)) {
                gui.showMessage("Category updated successfully.");
            } else {
                gui.showMessage("Failed to update category.");
            }
        } else {
            gui.showMessage("Category name cannot be empty.");
        }
    }

    private void deleteCategory() {
        viewCategories();
        Integer id = gui.readCategoryId("Enter ID of the category to delete: ");
        if (id == null) return;

        boolean success = categoryRepository.deleteCategory(id);
        if (success) {
            gui.showMessage("Category deleted successfully.");
        } else {
            gui.showMessage("Failed to delete category. Make sure there are no books assigned to it.");
        }
    }
}