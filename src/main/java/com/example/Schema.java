package com.example;


import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class Schema {

    @Autowired
    CharacterRepository characterRepository;

    GraphQLSchema graphQLSchema;

    @PostConstruct
    public void init() {
        try {
            SchemaParser schemaParser = new SchemaParser();
            ClassPathResource classPathResource = new ClassPathResource("schema.graphql");
            TypeDefinitionRegistry compiledSchema = schemaParser.parse(classPathResource.getFile());

            SchemaGenerator schemaGenerator = new SchemaGenerator();
            graphQLSchema = schemaGenerator.makeExecutableSchema(compiledSchema, buildRuntimeWiring());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public GraphQLSchema getSchema() {
        return graphQLSchema;
    }

    DataFetcher<CharacterRepository.Character> characterDataFetcher = environment ->
            characterRepository.getCharacterByName(environment.getArgument("firstName"));

    DataFetcher<List<CharacterRepository.Character>> allCharacters = environment -> characterRepository.getAll();

    DataFetcher<String> firstName = environment -> ((CharacterRepository.Character) environment.getSource()).firstName;

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("QueryType", typeWiring -> typeWiring
                        .dataFetcher("character", characterDataFetcher)
                        .dataFetcher("characters", allCharacters)
                ).type("Character", typeWiring -> typeWiring
                        .dataFetcher("firstName", firstName)
                ).build();
    }

}
