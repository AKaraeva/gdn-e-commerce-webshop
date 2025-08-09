package at.gdn.backend.persistence.converter;

import at.gdn.backend.enums.PaymentMethod;
import at.gdn.backend.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {
    private static final String VALID_VALUES = "'CC', 'PP', 'WT'";
    public static final String COLUMN_DEFINITION = "enum ("+VALID_VALUES+")";

    @Override
    public String convertToDatabaseColumn(PaymentMethod attribute) {
        return switch (attribute) {
            case null -> null;
            case CREDITCARD -> "CC";
            case PAYPAL -> "PP";
            case WIRETRANSFER -> "WT";
        };
    }

    @Override
    public PaymentMethod convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case null -> null;
            case "CC", "cc" -> PaymentMethod.CREDITCARD;
            case "PP", "pp" -> PaymentMethod.PAYPAL;
            case "WT", "wt" -> PaymentMethod.WIRETRANSFER;
            default -> throw DataQualityException.forInvalidDbValue(dbData, PaymentMethod.class, VALID_VALUES);
        };
    }
}