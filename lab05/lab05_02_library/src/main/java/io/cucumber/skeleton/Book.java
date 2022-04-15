package io.cucumber.skeleton;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Book {
    private final String title;
    private final String author;
    private String category;
    private final LocalDateTime published;

    public Book(String title, String author, String category, LocalDateTime published) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getPublishingYear() {
        return published;
    }
}
