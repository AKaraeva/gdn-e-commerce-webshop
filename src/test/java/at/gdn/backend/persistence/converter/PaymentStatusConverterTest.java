package at.gdn.backend.persistence.converter;

import at.gdn.backend.enums.PaymentStatus;
import at.gdn.backend.persistence.converter.PaymentStatusConverter;
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
class PaymentStatusConverterTest {

    private PaymentStatusConverter converter = new PaymentStatusConverter();


    @Nested
    class While_converting_valid_values{
        @Test
        void can_convert_enum_constants_to_db_value(){
            assertThat(converter.convertToDatabaseColumn(PaymentStatus.OPEN)).isEqualTo('O');
            assertThat(converter.convertToDatabaseColumn(PaymentStatus.PAID)).isEqualTo('P');
        }

        @ParameterizedTest
        @MethodSource
        void can_convert_valid_db_value_to_enum_constants(Character dbValue, PaymentStatus expectedEnumConstant){
            assertThat(converter.convertToEntityAttribute(dbValue)).isEqualTo(expectedEnumConstant);
        }

        static Stream<Arguments> can_convert_valid_db_value_to_enum_constants(){
            return Stream.of(
                    Arguments.of('O', PaymentStatus.OPEN),
                    Arguments.of('P', PaymentStatus.PAID),
                    Arguments.of('o', PaymentStatus.OPEN),
                    Arguments.of('p', PaymentStatus.PAID)
            );
        }
    }

    @Nested
    class While_converting_null_values{
        @Test
        void can_convert_to_db_value(){
            assertThat(converter.convertToDatabaseColumn(null)).isNull();
        }

        @Test
        void can_convert_to_reference(){
            assertThat(converter.convertToEntityAttribute(null)).isNull();
        }
    }


    @Test
    void correctly_handle_invalid_db_value(){
        assertThatThrownBy(() -> converter.convertToEntityAttribute('X')).isInstanceOf(DataQualityException.class);
    }
}