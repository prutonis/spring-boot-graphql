package io.morosanu.demographql.gql;

import io.morosanu.demographql.domain.Author;
import io.morosanu.demographql.domain.Publisher;
import io.morosanu.demographql.service.AuthorService;
import io.morosanu.demographql.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @QueryMapping("authors")
    public Iterable<Author> getAuthors() {
        return authorService.getAuthors();
    }

    @MutationMapping("createAuthor")
    public Author createAuthor(@Argument String firstName, @Argument String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        return authorService.createAuthor(author);
    }
}
