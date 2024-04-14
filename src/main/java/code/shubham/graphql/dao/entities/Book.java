package code.shubham.graphql.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Book {
    private int id;
    private String name;
    private int totalPages;
    private int authorId;
}