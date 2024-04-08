package io.morosanu.demographql.repo;

import io.morosanu.demographql.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Query("SELECT a FROM Author a WHERE a.firstName || ' ' || a.lastName like :author")
    Optional<Author> findByName(String author);
}
