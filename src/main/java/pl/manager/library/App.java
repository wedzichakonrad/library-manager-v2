package pl.manager.library;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.manager.library.configuration.AppConfiguration;
import pl.manager.library.core.Core;
import pl.manager.library.config.DatabaseConfig;
import pl.manager.library.database.IBookRepository;
import pl.manager.library.database.ICategoryRepository;
import pl.manager.library.database.IUserRepository;
import pl.manager.library.model.Book;
import pl.manager.library.model.Role;

public class App {
    public static void main(String[] args) {
        DatabaseConfig.initialize();

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfiguration.class);

        IUserRepository userRepository = context.getBean(IUserRepository.class);
        addDefaultUsers(userRepository);

        ICategoryRepository categoryRepository = context.getBean(ICategoryRepository.class);
        addDefaultCategories(categoryRepository);

        IBookRepository bookRepository = context.getBean(IBookRepository.class);
        addDefaultBooks(bookRepository);

        Core core = context.getBean(Core.class);
        core.run();
    }

    private static void addDefaultUsers(IUserRepository userRepository) {
        if (userRepository.getUserByLogin("admin") == null) {
            userRepository.addUser("admin", "admin", Role.ADMIN);
        }
        if (userRepository.getUserByLogin("jan") == null) {
            userRepository.addUser("jan", "jan", Role.USER);
        }
    }

    private static void addDefaultCategories(ICategoryRepository categoryRepository) {
        if (categoryRepository.getAllCategories().isEmpty()) {
            categoryRepository.addCategory("Fantastyka");
            categoryRepository.addCategory("Klasyka");
            categoryRepository.addCategory("Edukacja");
        }
    }

    private static void addDefaultBooks(IBookRepository bookRepository) {
        if (bookRepository.getAllBooks().isEmpty()) {
            Book b1 = new Book("Wladca Pierscieni", "J.R.R. Tolkien", 1954);
            b1.setCategoryId(1);
            bookRepository.addBook(b1);

            Book b2 = new Book("Lalka", "Boleslaw Prus", 1890);
            b2.setCategoryId(2);
            bookRepository.addBook(b2);

            Book b3 = new Book("Wiedzmin", "Andrzej Sapkowski", 1990);
            b3.setCategoryId(1);
            bookRepository.addBook(b3);

            Book b4 = new Book("Clean Code", "Robert C. Martin", 2008);
            b4.setCategoryId(3);
            bookRepository.addBook(b4);
        }
    }
}