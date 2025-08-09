package at.gdn.backend.persistence.converter;


import at.gdn.backend.enums.Department;
import at.gdn.backend.persistence.converter.DepartmentConverter;
import at.gdn.backend.persistence.exception.DataQualityException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Nested;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CategoryConverterTest {
    private DepartmentConverter categoryConverter = new DepartmentConverter();

    @Nested
    class While_converting_valid_values {
        @Test
        void can_convert_enum_constants_to_db_value() {
            assertThat(categoryConverter.convertToDatabaseColumn(Department.ACCESSORIES)).isEqualTo("AC");
            assertThat(categoryConverter.convertToDatabaseColumn(Department.CLOTHING)).isEqualTo("CL");
            assertThat(categoryConverter.convertToDatabaseColumn(Department.EXTRAS)).isEqualTo("EX");

        }

        @ParameterizedTest
        @MethodSource
        void can_convert_valid_db_value_to_enum_constants(String dbValue, Department expectedEnumConstant) {
            assertThat(categoryConverter.convertToEntityAttribute(dbValue)).isEqualTo(expectedEnumConstant);
        }

        static Stream<Arguments> can_convert_valid_db_value_to_enum_constants() {
            return Stream.of(
                    Arguments.of("AC", Department.ACCESSORIES),
                    Arguments.of("CL", Department.CLOTHING),
                    Arguments.of("EX", Department.EXTRAS),
                    Arguments.of("ac", Department.ACCESSORIES),
                    Arguments.of("cl", Department.CLOTHING),
                    Arguments.of("ex", Department.EXTRAS)
            );
        }
    }

    @Nested
    class While_converting_null_values {
        @Test
        void can_convert_to_db_value() {
            assertThat(categoryConverter.convertToDatabaseColumn(null)).isNull();
        }

        @Test
        void can_convert_to_reference() {
            assertThat(categoryConverter.convertToEntityAttribute(null)).isNull();
        }
    }

    @Test
    void correctly_handle_invalid_db_value() {
        assertThatThrownBy(() -> categoryConverter.convertToEntityAttribute("XX")).isInstanceOf(DataQualityException.class);
    }

}

