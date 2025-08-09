package at.gdn.backend.richtypes;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public record Lastname(String lastName) {


    public static final int MAX_LENGTH  = 64;
    public static final Pattern PATTERN = Pattern.compile("^\\p{IsAlphabetic}[\\p{IsAlphabetic}-\\. ]{0,%d}+$".formatted(MAX_LENGTH-1));
    public static final Predicate<String> isValidLastName = PATTERN.asMatchPredicate();

    public Lastname {
        Objects.requireNonNull(lastName);
        if (!isValidLastName.test(lastName)) throw new IllegalArgumentException(lastName + " is not a valid last name");
    }

    public static Lastname of(String lastName) {return new Lastname(lastName);}
}
