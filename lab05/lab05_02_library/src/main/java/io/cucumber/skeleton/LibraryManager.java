package io.cucumber.skeleton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryManager {

    List<Book> bookList;

    public LibraryManager(List<Book> bookList) {
        this.bookList = bookList;
    }

    public LibraryManager() {
        bookList = new ArrayList<>();
    }

    public void addBook(Book book) {
        bookList.add(book);
    }

    public List<Book> searchBooksInRangeYears(int start, int end) {
        return bookList.stream()
                .filter(
                        book -> book.getPublishingYear().getYear() <= end &&
                                book.getPublishingYear().getYear() >= start)
                .sorted(Comparator.comparing(Book::getPublishingYear).reversed())
                .collect(Collectors.toList());
    }
    public List<Book> searchBooksByAuthor(String author) {
        return bookList.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .sorted(Comparator.comparing(Book::getPublishingYear).reversed())
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByCategory(String category) {
        return bookList.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparing(Book::getPublishingYear).reversed())
                .collect(Collectors.toList());
    }
}
