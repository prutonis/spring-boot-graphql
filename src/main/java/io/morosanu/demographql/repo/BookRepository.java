package io.morosanu.demographql.repo;

import io.morosanu.demographql.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE b.author.firstName LIKE %:namePart% OR b.author.lastName LIKE %:namePart%")
    Collection<Book> findByAuthorName(String namePart);
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:bookTitle%")
    Collection<Book> findByTitle(String bookTitle);


    Optional<Book> findById(Integer id);

    List<Book> findAll();
}
