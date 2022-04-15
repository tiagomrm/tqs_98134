package io.cucumber.skeleton;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class StepDefinitions {

    LibraryManager libraryManager = new LibraryManager();

    List<Book> result;

    @ParameterType("([0-9]{2})-([0-9]{2})-([0-9]{4})")
    public LocalDateTime iso8601Date(String day, String month, String year){
        return LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),0, 0);
    }

    @Given("an {string} book with the title {string}, written by {string}, published in {iso8601Date}")
    public void addNewBook(String category, String title, String author, LocalDateTime year) {
        Book book = new Book(title, author, category, year);
        libraryManager.addBook(book);
    }

    @And("another {string} book with the title {string}, written by {string}, published in {iso8601Date}")
    public void addAnotherBook(String category, String title, String author, LocalDateTime year) {
        Book book = new Book(title, author, category, year);
        libraryManager.addBook(book);
    }

    @When("the customer searches for books published between {int} and {int}")
    public void theCustomerSearchesForBooksPublishedBetweenAnd(int year1, int year2) {
        result = libraryManager.searchBooksInRangeYears(year1, year2);
    }

    @Then("{int} books should have been found")
    public void booksShouldHaveBeenFound(int arg0) {
        assertThat(result.size(), equalTo(arg0));
    }

    @And("Book {int} should have the title {string}")
    public void bookShouldHaveTheTitleSomeOtherBook(int index, String title) throws IndexOutOfBoundsException {
        assertThat(result.get(index - 1).getTitle(), equalTo(title));
    }

    @When("the customer searches for books written by {string}")
    public void theCustomerSearchesForBooksWrittenByTimTomson(String author) {
        result = libraryManager.searchBooksByAuthor(author);
    }

    @When("the customer searches for books of category {string}")
    public void theCustomerSearchesForBooksOfCategoryAction(String category) {
        result = libraryManager.searchBooksByCategory(category);
    }
}
