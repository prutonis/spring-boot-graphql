package io.morosanu.demographql.repo;

import io.morosanu.demographql.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Publisher save(Publisher publisher);
    List<Publisher> findAll();

    Optional<Publisher> findByName(String publisher);
}
