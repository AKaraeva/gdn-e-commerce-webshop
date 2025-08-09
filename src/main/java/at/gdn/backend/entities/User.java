package at.gdn.backend.entities;

import at.gdn.backend.enums.UserRole;
import at.gdn.backend.persistence.converter.FirstNameConverter;
import at.gdn.backend.persistence.converter.LastNameConverter;
import at.gdn.backend.persistence.converter.UserRoleConverter;
import at.gdn.backend.richtypes.Firstname;
import at.gdn.backend.richtypes.Lastname;
import at.gdn.backend.richtypes.Username;
import at.gdn.backend.valueobjects.Address;
import at.gdn.backend.valueobjects.PhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @EmbeddedId
    private UserId id;

    @Convert(converter = FirstNameConverter.class)
    private Firstname firstName;



    @Convert(converter = LastNameConverter.class)
    private Lastname lastName;

//    @ElementCollection // JPA annotation to define a collection of basic types (OneToMany)
//    @CollectionTable(name="user_email_addresses",
//            joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_user_email_addresses_2_users")))
//    @OrderColumn(name = "order_idx")
//    private List<Email> email;

    @Embedded
    @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "username", insertable = false, updatable = false))
    })
    private Username username;

    @Email(message = "Ungültige E-Mail Adresse")
    @NotNull(message = "E-Mail Adresse ist erforderlich")
    @NotBlank(message = "E-Mail Adresse darf nicht leer sein")
    @Column(name="email_Address", unique = true, nullable = false)
    private String emailAddress;

    @Column (name ="encoded_password", nullable = false)
    private String encodedPassword;

    //AK: deleted "nullable = false", to be able to save user without phone number
    @AttributeOverrides({
            @AttributeOverride(name = "countryCode", column = @Column(name = "phone_country_code", columnDefinition = "integer check(phone_country_code between 1 and 999)")),
            @AttributeOverride(name = "areaCode", column = @Column(name = "phone_area_code")),
            @AttributeOverride(name = "serialNumber", column = @Column(name = "phone_serial_number")),
            @AttributeOverride(name = "extension", column = @Column(name = "phone_extension"))
    })
    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    @Column(nullable = true)
    @AssociationOverrides({
            @AssociationOverride(name = "country",
                    joinColumns = @JoinColumn(name = "country_id"),
                    foreignKey = @ForeignKey(name = "FK_user_main_address_2_countries"))})
    private Address mainAddress;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_addresses",
            joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_user_addresses_2_users")))
    @OrderColumn(name = "order_idx")
    private List<Address> additionalAddresses;


    @Column(columnDefinition = UserRoleConverter.COLUMN_DEFINTION)
    private UserRole userRole;

    public record UserId(@GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_seq"
    )
                                 @SequenceGenerator(
                                         name = "users_seq",
                                         sequenceName = "users_seq",  // Sequenzname in der Datenbank
                                         allocationSize = 1             // Schrittweite setzen
                                 )
                                 @NotNull Long id) {
        public UserId(int id) {
            this(Long.valueOf(id));
        }
    }
}