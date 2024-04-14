package code.shubham.graphql.dao.repositories;

import code.shubham.graphql.dao.entities.Author;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AuthorRepository {
    private ConcurrentHashMap<Integer, Author> authors;
    private AtomicInteger nextId = new AtomicInteger();

    public AuthorRepository() {
        this.authors = new ConcurrentHashMap<>();
        this.save(Author.builder().name("John").build());
        this.save(Author.builder().name("Bill").build());
    }

    public Collection<Author> findAll() {
        return authors.values();
    }

    public Author findById(final int authorId) {
        return Optional.ofNullable(this.authors.get(authorId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find author by id " + authorId));
    }

    public Author save(final Author author) {
        final int id = this.nextId.incrementAndGet();
        author.setId(id);
        this.authors.put(id, author);
        return author;
    }

    public Author deleteById(final Integer id) {
        return this.authors.remove(id);
    }
}