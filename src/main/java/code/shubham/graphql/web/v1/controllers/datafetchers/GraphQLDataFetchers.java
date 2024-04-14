package code.shubham.graphql.web.v1.controllers.datafetchers;

import code.shubham.graphql.dao.entities.Author;
import code.shubham.graphql.dao.repositories.AuthorRepository;
import code.shubham.graphql.dao.entities.Book;
import code.shubham.graphql.dao.repositories.BookRepository;
import graphql.schema.DataFetcher;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GraphQLDataFetchers {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public DataFetcher getBooksDataFetcher() {
        return dataFetchingEnvironment -> this.bookRepository.findAll();
    }

    public DataFetcher getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            int bookId = Integer.parseInt(dataFetchingEnvironment.getArgument("id"));
            return this.bookRepository.findById(bookId);
        };
    }

    public DataFetcher getAuthorsDataFetcher() {
        return dataFetchingEnvironment -> this.authorRepository.findAll();
    }

    public DataFetcher getAuthorByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            int bookId = Integer.parseInt(dataFetchingEnvironment.getArgument("id"));
            return this.authorRepository.findById(bookId);
        };
    }

    public DataFetcher updateAuthorById() {
        return dataFetchingEnvironment -> {
            final int authorId = dataFetchingEnvironment.<Integer>getArgument("authorId");
            final String name = dataFetchingEnvironment.getArgument("name");
            final Author author = this.authorRepository.findById(authorId);
            author.setName(name);
            return this.authorRepository.save(author);
        };
    }

    public DataFetcher deleteAuthorById() {
        return dataFetchingEnvironment -> {
            final int authorId = dataFetchingEnvironment.<Integer>getArgument("authorId");
            if (this.bookRepository.findAll()
                    .stream()
                    .anyMatch(book -> book.getAuthorId() == authorId))
                throw new IllegalArgumentException("author with id" + authorId + " has a book");
            return this.authorRepository.deleteById(authorId);
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            final Book book = dataFetchingEnvironment.getSource();
            return this.authorRepository.findById(book.getAuthorId());
        };
    }

    public DataFetcher getPageCountDataFetcher() {
        return dataFetchingEnvironment -> {
            final Book book = dataFetchingEnvironment.getSource();
            return book.getTotalPages();
        };
    }

    public DataFetcher createAuthor() {
        return dataFetchingEnvironment -> {
            final String name = dataFetchingEnvironment.getArgument("name");
            return this.authorRepository.save(Author.builder().name(name).build());
        };
    }

    public DataFetcher createBook() {
        return dataFetchingEnvironment -> {
            final String name = dataFetchingEnvironment.getArgument("name");
            final Integer totalPages = dataFetchingEnvironment.<Integer>getArgument("totalPages");
            final Integer authorId = dataFetchingEnvironment.<Integer>getArgument("authorId");
            Optional.ofNullable(this.authorRepository.findById(authorId))
                    .orElseThrow(() -> new IllegalArgumentException("No author with id: " + authorId + " found"));
            return this.bookRepository.save(Book.builder().name(name).totalPages(totalPages).authorId(authorId).build());
        };
    }

}