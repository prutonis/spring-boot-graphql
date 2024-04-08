package io.morosanu.demographql.service;

import io.morosanu.demographql.domain.Book;
import io.morosanu.demographql.repo.AuthorRepository;
import io.morosanu.demographql.repo.BookRepository;
import io.morosanu.demographql.repo.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }


    public Collection<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Collection<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    public Collection<Book> getBooksByName(String bookName) {
        return bookRepository.findByTitle(bookName);
    }

    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    public Book createBook(String title, String isbn, String author, String publisher) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        authorRepository.findByName(author).ifPresent(book::setAuthor);
        publisherRepository.findByName(publisher).ifPresent(book::setPublisher);
        return bookRepository.save(book);
    }

    public Iterable<Book> getBooksByAuthorName(String namePart) {
        return bookRepository.findByAuthorName(namePart);
    }
}
