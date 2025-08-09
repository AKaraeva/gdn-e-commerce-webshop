package at.gdn.backend.persistence.converter;


import at.gdn.backend.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import at.gdn.backend.enums.UserRole;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, Character> {

    private static final String VALID_VALUES = "'A', 'C', 'G','O'";
    public static final  String COLUMN_DEFINTION =  "enum ("+VALID_VALUES+")";


    @Override
    public Character convertToDatabaseColumn(UserRole attribute) {
        return switch (attribute){
            case null -> null;
            case ADMIN -> 'A';
            case CUSTOMER -> 'C';
            case GUEST -> 'G';
            case OPERATOR -> 'O';

        };
    }

    @Override
    public UserRole convertToEntityAttribute(Character dbData) {
        return switch (dbData){
            case null -> null;
            case 'A', 'a' -> UserRole.ADMIN ;
            case 'C', 'c' -> UserRole.CUSTOMER;
            case 'G', 'g' -> UserRole.GUEST;
            case 'O', 'o' -> UserRole.OPERATOR;
            default -> throw DataQualityException.forInvalidDbValue(dbData, UserRole.class, VALID_VALUES);


        };
    }
}
