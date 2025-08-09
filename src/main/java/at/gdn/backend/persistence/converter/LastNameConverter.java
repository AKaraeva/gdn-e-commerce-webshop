package at.gdn.backend.persistence.converter;


import at.gdn.backend.richtypes.Lastname;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LastNameConverter implements AttributeConverter<Lastname, String> {

    @Override
    public String convertToDatabaseColumn(Lastname attribute) {
        return(attribute == null)? null :attribute.lastName();
        }

    @Override
    public Lastname convertToEntityAttribute(String dbData) {
        return (dbData == null)? null :new Lastname(dbData);
    }


}
