package at.gdn.backend.richtypes;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;


public record Firstname(String firstName) {

    public static final int MAX_LENGTH  = 64;
    public static final Pattern PATTERN = Pattern.compile("^\\p{IsAlphabetic}[\\p{IsAlphabetic}-\\. ]{0,%d}+$".formatted(MAX_LENGTH-1));
    public static final Predicate<String> isValidFirstName = PATTERN.asMatchPredicate();

    public Firstname {
        Objects.requireNonNull(firstName);
        if (!isValidFirstName.test(firstName)) throw new IllegalArgumentException(firstName + " is not a valid first name");
    }

    public static Firstname of(String firstName) {return new Firstname(firstName);}
}
