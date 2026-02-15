package pl.manager.library.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Book {
    private final int id;
    private String title;
    private String author;

    public Book(String title, String author) {
        this.id = 0;
        this.title = title;
        this.author = author;
    }
}