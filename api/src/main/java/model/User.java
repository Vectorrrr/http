package model;

/**
 * The class contains information
 * about an authorized user
 * @author Gladush Ivan
 * @since 30.03.16.
 */
public class User {
    private String name;
    private String surname;

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return String.format("User name %s User surname: %s", name, surname);
    }
}