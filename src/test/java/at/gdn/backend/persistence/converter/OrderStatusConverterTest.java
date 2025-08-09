package at.gdn.backend.persistence.converter;

import at.gdn.backend.enums.OrderStatus;
import at.gdn.backend.persistence.converter.OrderStatusConverter;
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
class OrderStatusConverterTest {

    private OrderStatusConverter converter = new OrderStatusConverter();


    @Nested
    class While_converting_valid_values{
        @Test
        void can_convert_enum_constants_to_db_value(){
            assertThat(converter.convertToDatabaseColumn(OrderStatus.PENDING)).isEqualTo('P');
            assertThat(converter.convertToDatabaseColumn(OrderStatus.SHIPPED)).isEqualTo('S');
            assertThat(converter.convertToDatabaseColumn(OrderStatus.DELIVERED)).isEqualTo('D');
            assertThat(converter.convertToDatabaseColumn(OrderStatus.CANCELLED)).isEqualTo('C');
        }

        @ParameterizedTest
        @MethodSource
        void can_convert_valid_db_value_to_enum_constants(Character dbValue, OrderStatus expectedEnumConstant){
            assertThat(converter.convertToEntityAttribute(dbValue)).isEqualTo(expectedEnumConstant);
        }

        static Stream<Arguments> can_convert_valid_db_value_to_enum_constants(){
            return Stream.of(
                    Arguments.of('P', OrderStatus.PENDING),
                    Arguments.of('S', OrderStatus.SHIPPED),
                    Arguments.of('D', OrderStatus.DELIVERED),
                    Arguments.of('C', OrderStatus.CANCELLED),
                    Arguments.of('p', OrderStatus.PENDING),
                    Arguments.of('s', OrderStatus.SHIPPED),
                    Arguments.of('d', OrderStatus.DELIVERED),
                    Arguments.of('c', OrderStatus.CANCELLED)
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