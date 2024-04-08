package io.morosanu.demographql.gql;

import io.morosanu.demographql.domain.Book;
import io.morosanu.demographql.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping("books")
    public Iterable<Book> getBooks() {
        return bookService.getBooks();
    }

    @QueryMapping("book")
    public Book getBook(@Argument Integer id) {
        return bookService.getBookById(id).orElse(null);
    }

    @MutationMapping("createBook")
    public Book createBook(@Argument String title, @Argument String isbn, @Argument String author, @Argument String publisher) {
        return bookService.createBook(title, isbn, author, publisher);
    }

    @QueryMapping("booksByName")
    public Iterable<Book> getBooksByName(@Argument String namePart) {
        return bookService.getBooksByName(namePart);
    }

    @QueryMapping("booksByAuthorName")
    public Iterable<Book> getBooksByAuthorName(@Argument String namePart) {
        return bookService.getBooksByAuthorName(namePart);
    }
}
