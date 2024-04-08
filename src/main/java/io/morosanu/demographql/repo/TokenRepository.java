package io.morosanu.demographql.repo;

import io.morosanu.demographql.domain.Token;
import io.morosanu.demographql.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    void deleteAllByUser(User dbUser);
    void deleteByToken(String token);

    Token findByToken(String token);
}
