package at.gdn.backend.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
public record PhoneNumber(
        //AK: deleted @NotNull, to be able to save user without phone number
        @Min(1) @Max(999) Integer countryCode,
        @Min(1) @Max(99999) Integer areaCode,
        @Size(min=4, max=10) String serialNumber,
        @Positive @Max(99999) Integer extension

) {
    public PhoneNumber{
        if (countryCode == null || areaCode==null || serialNumber==null) {
            throw PhoneNumberException.forUnsupportedNullValues(countryCode, areaCode, serialNumber);
        }

        if (!(countryCode >= 1 && countryCode <= 999)
                || !(areaCode >= 1 && areaCode <= 99999)
                || !(serialNumber.length() >= 4 && serialNumber.length() <= 10)
                || (extension != null && !(extension > 0 && extension <= 99999))) {
            throw new IllegalArgumentException("The provided values are not in valid range");
        }
    }

    //AK: Fixes no-arg constructor issue
    public PhoneNumber() {
        this(43, 1, "123456789", null);
    }

    public  static class PhoneNumberException extends RuntimeException{

        private PhoneNumberException(String message){
            super(message);
        }

        public static PhoneNumberException forUnsupportedNullValues(Integer countryCode, Integer areaCode, String serialNumber) {
            var msg ="You provided an unsupported null value for either countryCode=%d, areaCode=%d or serialNumber=%s".formatted(countryCode, areaCode, serialNumber);
            return new PhoneNumberException(msg);
        }
    }
    public static PhoneNumber of(Integer countryCode, Integer areaCode, String serialNumber, Integer extension) {
        return new PhoneNumber(countryCode, areaCode, serialNumber, extension);
    }
}
