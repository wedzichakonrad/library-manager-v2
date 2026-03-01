package pl.manager.library.gui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.manager.library.model.Book;
import pl.manager.library.model.Category;
import pl.manager.library.model.Role;
import pl.manager.library.model.User;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class GUI implements IGUI {
    private final Scanner scanner;
    private final List<String> USER_OPTIONS = List.of("0", "1", "2", "3", "4", "5", "6", "7");
    private final List<String> ADMIN_OPTIONS = Stream.concat(
            USER_OPTIONS.stream(),
            Stream.of("8", "9", "10", "11", "12", "13", "14", "15", "16")
    ).toList();

    private void showCommonMenu() {
        System.out.println("0. Exit");
        System.out.println("1. View Books");
        System.out.println("2. Search books by author");
        System.out.println("3. Search books by title");
        System.out.println("4. Rent a book");
        System.out.println("5. Return a book");
        System.out.println("6. My rentals");
        System.out.println("7. Search by Category");
    }

    private void showUserMenu() {
        showCommonMenu();
    }

    private void showAdminMenu() {
        showCommonMenu();
        System.out.println("8. Add Book");
        System.out.println("9. Remove Book");
        System.out.println("10. Edit Book");
        System.out.println("11. Add User");
        System.out.println("12. View Users");
        System.out.println("13. View Statistics");
        System.out.println("14. Add Category");
        System.out.println("15. Edit Category");
        System.out.println("16. Delete Category");
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
    public void showBooks(List<Book> books, List<Category> categories) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.printf("%-4s | %-20s | %-25s | %-6s | %-15s | %-10s%n", "ID", "Author", "Title", "Year", "Category", "Status");
            System.out.println("------------------------------------------------------------------------------------------------");
            for (Book book : books) {
                String status = book.isAvailable() ? "Available" : "Rented";
                String catName = "-";

                if (book.getCategoryId() != null) {
                    for (Category c : categories) {
                        if (c.getId() == book.getCategoryId()) {
                            catName = c.getName();
                            break;
                        }
                    }
                }

                System.out.printf("%-4d | %-20s | %-25s | %-6d | %-15s | %-10s%n",
                        book.getId(), book.getAuthor(), book.getTitle(), book.getYear(), catName, status);
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
            System.out.println("Invalid book ID.");
            return null;
        }
    }

    @Override
    public Book readBookData() {
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter year of publication: ");

        int year;
        try {
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid year. Setting to 0.");
            year = 0;
        }

        if (author.isBlank() || title.isBlank()) {
            System.out.println("Author and title cannot be empty!");
            return null;
        }

        return new Book(title, author, year);
    }

    @Override
    public void showUsers(List<User> users) {
        for (User user : users) {
            System.out.println(user.getId() + " " + user.getLogin() + " [" + user.getRole() + "]");
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showStatistics(Map<String, Integer> stats) {
        System.out.println("--- Library Statistics ---");
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            System.out.printf("%-20s : %d%n", entry.getKey(), entry.getValue());
        }
        System.out.println("--------------------------");
    }

    @Override
    public void showPopularBooks(Map<String, Integer> popularBooks) {
        System.out.println("--- Top 5 Most Popular Books ---");
        if (popularBooks.isEmpty()) {
            System.out.println("No rentals recorded yet.");
        } else {
            for (Map.Entry<String, Integer> entry : popularBooks.entrySet()) {
                System.out.printf("Times rented: %-3d | %s%n", entry.getValue(), entry.getKey());
            }
        }
        System.out.println("--------------------------------");
    }

    @Override
    public void showCategories(List<Category> categories) {
        System.out.println("--- Categories ---");
        for (Category cat : categories) {
            System.out.println(cat.getId() + ". " + cat.getName());
        }
    }

    @Override
    public Integer readCategoryId(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        try {
            int id = Integer.parseInt(input);
            if (id <= 0) return null;
            return id;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String readCategoryName() {
        System.out.print("Enter category name: ");
        return scanner.nextLine();
    }
}