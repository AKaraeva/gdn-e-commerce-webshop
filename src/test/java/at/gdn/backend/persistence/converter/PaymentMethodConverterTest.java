package at.gdn.backend.persistence.converter;

import at.gdn.backend.enums.PaymentMethod;
import at.gdn.backend.persistence.converter.PaymentMethodConverter;
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
class PaymentMethodConverterTest {

    private PaymentMethodConverter converter = new PaymentMethodConverter();


    @Nested
    class While_converting_valid_values{
        @Test
        void can_convert_enum_constants_to_db_value(){
            assertThat(converter.convertToDatabaseColumn(PaymentMethod.PAYPAL)).isEqualTo("PP");
            assertThat(converter.convertToDatabaseColumn(PaymentMethod.CREDITCARD)).isEqualTo("CC");
            assertThat(converter.convertToDatabaseColumn(PaymentMethod.WIRETRANSFER)).isEqualTo("WT");
        }

        @ParameterizedTest
        @MethodSource
        void can_convert_valid_db_value_to_enum_constants(String dbValue, PaymentMethod expectedEnumConstant){
            assertThat(converter.convertToEntityAttribute(dbValue)).isEqualTo(expectedEnumConstant);
        }

        static Stream<Arguments> can_convert_valid_db_value_to_enum_constants(){
            return Stream.of(
                    Arguments.of("PP", PaymentMethod.PAYPAL),
                    Arguments.of("CC", PaymentMethod.CREDITCARD),
                    Arguments.of("WT", PaymentMethod.WIRETRANSFER),
                    Arguments.of("pp", PaymentMethod.PAYPAL),
                    Arguments.of("cc", PaymentMethod.CREDITCARD),
                    Arguments.of("wt", PaymentMethod.WIRETRANSFER)
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
        assertThatThrownBy(() -> converter.convertToEntityAttribute("XX")).isInstanceOf(DataQualityException.class);
    }
}