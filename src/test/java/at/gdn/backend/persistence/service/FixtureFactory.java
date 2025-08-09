package at.gdn.backend.persistence.service;


import at.gdn.backend.entities.Country;
import at.gdn.backend.entities.User;
import at.gdn.backend.enums.AddressType;
import at.gdn.backend.richtypes.Firstname;
import at.gdn.backend.richtypes.Lastname;
import at.gdn.backend.valueobjects.Address;
import at.gdn.backend.valueobjects.PhoneNumber;

import java.util.List;

public class FixtureFactory {

    public static PhoneNumber phoneNumber() {
        return new PhoneNumber(43, 678, "1234", 1);
    }

    public static Country austria() {
        return Country.builder().cityName("Wien").iso2Code("AT").areaCode(45).build();
    }

    public static Address spengergasse20(Country country) {
        return address("Spengergasse 20", country);
    }

    public static Address address(String streetNumber, Country country) {
        return Address.builder()
                      .street("Spengergasse 20")
                      .postalCode(1050)
                      .doorNumber(12)
                      .houseNumber(20)
                      .country(country)
                      .city("Wien")
                      .addressType(AddressType.PRIVATE)
                      .build();
    }

    public static User SPG(Address mainAddress, Address address) {
        return User.builder()
                            .firstName(Firstname.of("Max"))
                            .lastName(Lastname.of("Mustermann"))
                            .phoneNumber(phoneNumber())
                            .mainAddress(mainAddress)
                            .additionalAddresses(List.of(address))
                            .build();
    }
}