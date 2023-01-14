package project.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Role {

    ADMINISTRATOR,
    USER;

    public static Optional<Role> find(String role) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(role))
                .findFirst();
    }
}
