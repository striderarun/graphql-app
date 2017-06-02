package com.example;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterRepository {

    public static class Character {

        public Character(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String firstName;
        public String lastName;
    }

    private List<Character> characters = new ArrayList<>();

    @PostConstruct
    public void init() {
        Character aragorn = new Character("Aragorn", "Isildur");
        Character gandalf = new Character("Gandalf", "Greybeard");
        characters.add(aragorn);
        characters.add(gandalf);
    }

    public Character getCharacterByName(String firstName) {
        List<Character> found = characters.stream().filter(character -> character.firstName.equals(firstName)).collect(Collectors.toList());
        return found.size() > 0 ? found.get(0) : null;
    }

    public List<Character> getAll() {
        return characters;
    }
}
