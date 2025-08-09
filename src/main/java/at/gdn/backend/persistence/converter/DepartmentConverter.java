package at.gdn.backend.persistence.converter;

import at.gdn.backend.persistence.exception.DataQualityException;
import jakarta.persistence.AttributeConverter;
import at.gdn.backend.enums.Department;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DepartmentConverter implements AttributeConverter<Department, String> {

    private static final String VALID_VALUES = "'AC', 'CL', 'EX'";
    public static final  String COLUMN_DEFINTION =  "enum ("+VALID_VALUES+")";

    @Override
    public String convertToDatabaseColumn(Department attribute) {
        return switch (attribute){
            case null-> null;
            case ACCESSORIES -> "AC";
            case CLOTHING -> "CL";
            case EXTRAS -> "EX";
        };
    }

    @Override
    public Department convertToEntityAttribute(String dbData) {
        return switch (dbData){
            case null-> null;
            case "AC", "ac" -> Department.ACCESSORIES;
            case "CL", "cl" -> Department.CLOTHING;
            case "EX", "ex" -> Department.EXTRAS;
            default -> throw DataQualityException.forInvalidDbValue(dbData, Department.class, VALID_VALUES);
        };
    }
}
