package at.gdn.backend.persistence.converter;

import at.gdn.backend.enums.AddressType;
import at.gdn.backend.persistence.converter.AddressTypeConverter;
import at.gdn.backend.persistence.exception.DataQualityException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AddressTypeConverterTest {
    private AddressTypeConverter converter = new AddressTypeConverter();


    @Nested
    class While_converting_valid_values {
        @Test
        void can_convert_enum_constants_to_db_value() {
            assertThat(converter.convertToDatabaseColumn(AddressType.DELIVERY)).isEqualTo("DE");
            assertThat(converter.convertToDatabaseColumn(AddressType.BILLING)).isEqualTo("BI");

        }

        @ParameterizedTest
        @MethodSource
        void can_convert_valid_db_value_to_enum_constants(String dbValue, AddressType expectedEnumConstant) {
            assertThat(converter.convertToEntityAttribute(dbValue)).isEqualTo(expectedEnumConstant);
        }

        static Stream<Arguments> can_convert_valid_db_value_to_enum_constants() {
            return Stream.of(
                    Arguments.of("DE", AddressType.DELIVERY),
                    Arguments.of("BI", AddressType.BILLING),
                    Arguments.of("de", AddressType.DELIVERY),
                    Arguments.of("bi", AddressType.BILLING)
            );
        }
    }

    @Nested
    class While_converting_null_values {
        @Test
        void can_convert_to_db_value() {
            assertThat(converter.convertToDatabaseColumn(null)).isNull();
        }

        @Test
        void can_convert_to_reference() {
            assertThat(converter.convertToEntityAttribute(null)).isNull();
        }
    }


    @Test
    void correctly_handle_invalid_db_value() {
        assertThatThrownBy(() -> converter.convertToEntityAttribute("X")).isInstanceOf(DataQualityException.class);
    }
}