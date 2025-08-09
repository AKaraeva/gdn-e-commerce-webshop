package at.gdn.backend.persistence.exception;

public class DataQualityException extends RuntimeException {
    private DataQualityException(String message) {
        super(message);
    }

    public static DataQualityException forInvalidDbValue(Character invalidValue, Class<? extends Enum> enumClass, String validValues) {
        return forInvalidDbValue(invalidValue.toString(), enumClass, validValues);
    }

    public static DataQualityException forInvalidDbValue(String invalidValue, Class<? extends Enum> enumClass, String validValues) {
        var msg ="Found an invalid db value (%s) for enum class '%s'! Valid values are: %s".formatted(invalidValue, enumClass.getSimpleName(), validValues);
        return new DataQualityException(msg);
    }
}