package at.gdn.backend.persistence.converter;

import at.gdn.backend.enums.OrderStatus;
import at.gdn.backend.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Character> {
    private static final String VALID_VALUES = "'P', 'S','D', 'C'";
    public static final String COLUMN_DEFINITION = "enum ("+VALID_VALUES+")";

    @Override
    public Character convertToDatabaseColumn(OrderStatus attribute) {
        return switch (attribute) {
            case null -> null;
            case PENDING -> 'P';
            case SHIPPED -> 'S';
            case DELIVERED -> 'D';
            case CANCELLED -> 'C';
        };
    }

    @Override
    public OrderStatus convertToEntityAttribute(Character dbData) {
        return switch (dbData) {
            case null -> null;
            case 'P', 'p' -> OrderStatus.PENDING;
            case 'S', 's' -> OrderStatus.SHIPPED;
            case 'D', 'd' -> OrderStatus.DELIVERED;
            case 'C', 'c' -> OrderStatus.CANCELLED;
            default -> throw DataQualityException.forInvalidDbValue(dbData, OrderStatus.class, VALID_VALUES);
        };
    }
}