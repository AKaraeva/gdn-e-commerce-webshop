//package at.gdn.backend.valueobjects;
//
//import jakarta.persistence.*;
//import lombok.Builder;
//
//import java.util.function.Predicate;
//import java.util.regex.Pattern;
//
//@Builder
//@Embeddable
//public record Email(
//        String email
//) {
//        public static final int length = 64;
//        public static final Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)*[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
//        public static final Predicate<String> isValidEmail = pattern.asMatchPredicate();
//
//
//    public Email {
//            if (email == null) throw new IllegalArgumentException("email cannot be null");
//            if (!isValidEmail.test(email)) throw new IllegalArgumentException("Invalid email: "+ email);
//
//    }
//}
