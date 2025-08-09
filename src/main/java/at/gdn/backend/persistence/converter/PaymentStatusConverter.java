package at.gdn.backend.persistence.converter;

import at.gdn.backend.enums.PaymentStatus;
import at.gdn.backend.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, Character> {
    private static final String VALID_VALUES = "'O', 'P'";
    public static final String COLUMN_DEFINITION = "enum ("+VALID_VALUES+")";

    @Override
    public Character convertToDatabaseColumn(PaymentStatus attribute) {
        return switch (attribute) {
            case null -> null;
            case OPEN -> 'O';
            case PAID -> 'P';
        };
    }

    @Override
    public PaymentStatus convertToEntityAttribute(Character dbData) {
        return switch (dbData) {
            case null -> null;
            case 'O', 'o' -> PaymentStatus.OPEN;
            case 'P', 'p' -> PaymentStatus.PAID;
            default -> throw DataQualityException.forInvalidDbValue(dbData, PaymentStatus.class, VALID_VALUES);
        };
    }
}