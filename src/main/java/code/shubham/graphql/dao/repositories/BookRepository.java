package code.shubham.graphql.dao.repositories;

import code.shubham.graphql.dao.entities.Book;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class BookRepository {
    private ConcurrentHashMap<Integer, Book> books;
    private AtomicInteger nextId = new AtomicInteger();

    public BookRepository() {
        this.books = new ConcurrentHashMap<>();
        this.save(Book.builder().name("Spring Book").totalPages(300).authorId(1).build());
        this.save(Book.builder().name("GraphQL Book").totalPages(250).authorId(1).build());
    }

    public Collection<Book> findAll() {
        return books.values();
    }

    public Book findById(final int bookId) {
        return Optional.ofNullable(this.books.get(bookId))
                .orElseThrow(() -> new IllegalArgumentException("Can't find book by id " + bookId));
    }

    public Book save(final Book book) {
        final int id = this.nextId.incrementAndGet();
        book.setId(id);
        this.books.put(id, book);
        return book;
    }
}