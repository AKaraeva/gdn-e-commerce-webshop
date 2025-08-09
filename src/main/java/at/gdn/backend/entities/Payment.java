package at.gdn.backend.entities;

import at.gdn.backend.enums.PaymentMethod;
import at.gdn.backend.enums.PaymentStatus;
import at.gdn.backend.persistence.converter.PaymentMethodConverter;
import at.gdn.backend.persistence.converter.PaymentStatusConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "payments")
public class Payment {

    @EmbeddedId
    private PaymentId id;
    private LocalDate paymentDate;
    private double paymentAmount;

    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethod paymentMethod;

    @Column(columnDefinition = PaymentStatusConverter.COLUMN_DEFINITION)
    private PaymentStatus paymentStatus;


//Korrektur - ist in Order
//@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//@JoinColumn(foreignKey = @ForeignKey(name = "fk_payment_2_orders"))
//private Order order;

    public record PaymentId(@GeneratedValue(
                                strategy = GenerationType.SEQUENCE,
                                generator = "payments_seq"
                            )
                            @SequenceGenerator(
                                name = "payments_seq",
                                sequenceName = "payments_seq",  // Sequenzname in der Datenbank
                                allocationSize = 1             // Schrittweite setzen
                            )
                            @NotNull Long id){}

}
