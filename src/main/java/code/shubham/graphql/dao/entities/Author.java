package code.shubham.graphql.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Author {
    private int id;
    private String name;
}