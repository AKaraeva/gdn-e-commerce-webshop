package at.gdn.backend.persistence.converter;

import at.gdn.backend.enums.AddressType;
import at.gdn.backend.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AddressTypeConverter implements AttributeConverter<AddressType, String> {

    private static final String VALID_VALUES = "'BI', 'DE', 'PR'";
    public static final String COLUMN_DEFINITION = "enum (" + VALID_VALUES + ")";

    @Override
    public String convertToDatabaseColumn(AddressType attribute) {
        return switch (attribute) {
            case null -> null;
            case BILLING -> "BI";
            case DELIVERY -> "DE";
            case PRIVATE -> "PR";
        };
    }

    @Override
    public AddressType convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case null -> null;
            case "BI", "bi" -> AddressType.BILLING;
            case "DE", "de" -> AddressType.DELIVERY;
            case "PR", "pr" -> AddressType.PRIVATE;
            default -> throw DataQualityException.forInvalidDbValue(dbData, AddressType.class, VALID_VALUES);
        };
    }
}