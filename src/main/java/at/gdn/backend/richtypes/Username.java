package at.gdn.backend.richtypes;


import lombok.Builder;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Builder
public record Username(String username) {

    public static final int MAX_LENGTH = 64;

    public static final Pattern PATTERN = Pattern.compile("^\\p{IsAlphabetic}[\\p{IsAlphabetic}-\\. ]{0,%d}+$".formatted(MAX_LENGTH - 1));
    public static final Predicate<String> isValidUsername = PATTERN.asMatchPredicate();

    public Username {
        Objects.requireNonNull(username);
        if (!isValidUsername.test(username))
            throw new IllegalArgumentException("Invalid Firstname: " + username);

    }

    public static Username of(String username) {
        return new Username(username);
    }
}