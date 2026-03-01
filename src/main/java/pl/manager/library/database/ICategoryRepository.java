package pl.manager.library.database;

import pl.manager.library.model.Category;

import java.util.List;

public interface ICategoryRepository {
    List<Category> getAllCategories();
    void addCategory(String name);
    Category getCategoryById(int id);
    boolean updateCategory(Category category);
    boolean deleteCategory(int id);
}