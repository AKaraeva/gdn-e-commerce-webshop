package at.gdn.backend.persistence.converter;


import at.gdn.backend.richtypes.Firstname;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FirstNameConverter implements AttributeConverter<Firstname, String> {

    @Override
    public String convertToDatabaseColumn(Firstname attribute) {
        return(attribute == null)? null :attribute.firstName();
        }

    @Override
    public Firstname convertToEntityAttribute(String dbData) {
        return (dbData == null)? null :new Firstname(dbData);
    }
}
