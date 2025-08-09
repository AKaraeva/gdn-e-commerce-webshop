package at.gdn.backend.persistence.converter;

import at.gdn.backend.enums.UserRole;
import at.gdn.backend.persistence.exception.DataQualityException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserRoleConverterTest {
    private final UserRoleConverter userRoleConverter = new UserRoleConverter();

    @Nested
    class While_converting_valid_values {
        @Test
        void can_convert_enum_constants_to_db_value() {
            assertThat(userRoleConverter.convertToDatabaseColumn(UserRole.ADMIN)).isEqualTo('A');
            assertThat(userRoleConverter.convertToDatabaseColumn(UserRole.CUSTOMER)).isEqualTo('C');
            assertThat(userRoleConverter.convertToDatabaseColumn(UserRole.GUEST)).isEqualTo('G');
            assertThat(userRoleConverter.convertToDatabaseColumn(UserRole.OPERATOR)).isEqualTo('O');

        }

        @ParameterizedTest
        @MethodSource
        void can_convert_valid_db_value_to_enum_constants(Character dbValue, UserRole expectedEnumConstant) {
            assertThat(userRoleConverter.convertToEntityAttribute(dbValue)).isEqualTo(expectedEnumConstant);
        }

        static Stream<Arguments> can_convert_valid_db_value_to_enum_constants() {
            return Stream.of(
                    Arguments.of("A", UserRole.ADMIN),
                    Arguments.of("C", UserRole.CUSTOMER),
                    Arguments.of("G", UserRole.GUEST),
                    Arguments.of("O", UserRole.OPERATOR),

                    Arguments.of("a", UserRole.ADMIN),
                    Arguments.of("c", UserRole.CUSTOMER),
                    Arguments.of("g", UserRole.GUEST),
                    Arguments.of("o", UserRole.OPERATOR)


            );
        }
    }

    @Nested
    class While_converting_null_values {
        @Test
        void can_convert_to_db_value() {
            assertThat(userRoleConverter.convertToDatabaseColumn(null)).isNull();
        }

        @Test
        void can_convert_to_reference() {
            assertThat(userRoleConverter.convertToEntityAttribute(null)).isNull();
        }
    }

    @Test
    void correctly_handle_invalid_db_value() {
        assertThatThrownBy(() -> userRoleConverter.convertToEntityAttribute('X')).isInstanceOf(DataQualityException.class);
    }

}