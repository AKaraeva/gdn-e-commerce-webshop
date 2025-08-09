package at.gdn.backend.valueobjects;

import at.gdn.backend.entities.Country;
import at.gdn.backend.enums.AddressType;
import at.gdn.backend.persistence.converter.AddressTypeConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Access(AccessType.PROPERTY)
@Embeddable
public record Address(
        String street,
        Integer houseNumber,
        Integer doorNumber,
        String city,
        Integer postalCode,

        @ManyToOne(cascade = {CascadeType.PERSIST}) // Many addresses can have one country
        @JoinColumn(foreignKey = @ForeignKey(name = "FK_addresses_2_countries"))
        Country country,

        @Column(columnDefinition = AddressTypeConverter.COLUMN_DEFINITION)
        AddressType addressType

) {
}