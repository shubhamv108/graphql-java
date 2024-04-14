package code.shubham.graphql.web.v1.controllers;

import code.shubham.graphql.models.QueryDTO;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GraphQLController {

    private final GraphQL graphQL;

    @PostMapping("/graphql")
    public ResponseEntity<?> query(@RequestBody final QueryDTO query) {
        final ExecutionResult executionResult = this.graphQL.execute(query.getQuery());
        return ResponseEntity.ok(executionResult.getData());
    }
}