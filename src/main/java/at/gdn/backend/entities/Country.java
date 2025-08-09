package at.gdn.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.lang.NonNull;


@Data // Lombok annotation to create all getters, setters, equals, hash, toString
@AllArgsConstructor // Lombok annotation to create a constructor with all arguments
@NoArgsConstructor // Lombok annotation to create a constructor with no arguments
@Builder // Lombok annotation to create a builder

@Entity
@Table(name = "countries")
public class Country {

    @EmbeddedId
    private CountryId id;
    private String cityName;
    @Column(length = 2)
    private String iso2Code;
    @Min(1) @Max(999)
    private Integer areaCode;

    public record CountryId(@GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "countries_seq"
    )
                             @SequenceGenerator(
                                     name = "countries_seq",
                                     sequenceName = "countries_seq",  // Sequenzname in der Datenbank
                                     allocationSize = 1             // Schrittweite setzen
                             )
                             @NotNull Long id){}
}
