package code.shubham.graphql.models;

import lombok.Data;

@Data
public class QueryDTO {
    private String query;
    private String variables;

}