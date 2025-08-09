package at.gdn.backend.entities;

import at.gdn.backend.enums.OrderStatus;
import at.gdn.backend.enums.PaymentMethod;
import at.gdn.backend.persistence.converter.OrderStatusConverter;
import at.gdn.backend.persistence.converter.PaymentMethodConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "orders")
public class Order {

    @EmbeddedId
    private OrderId id;
    //@Min(1)
    //private Integer quantity;
    private LocalDate orderDate;

    //Beziehung - zu Enum OrderStatus
    @Column(columnDefinition = OrderStatusConverter.COLUMN_DEFINITION)
    private OrderStatus status;
    private Double totalPrice;

    private LocalDate shippingDate;
    private LocalDate deliveryDate;


    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "orders_order_items",
            joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_orderitems_2_orders")),
            uniqueConstraints = @UniqueConstraint(name = "U_orders_orderitems",
                    columnNames = {"order_id", "orderitems_idx"}))
    @OrderColumn(name = "orderitems_idx")
    private List<OrderItems> orderItems;



    @ManyToOne(cascade = {CascadeType.PERSIST}) // Many albums can have one owner
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_orders_2_payment"))
    private Payment payment;

    @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "FK_orders_2_users"))
    private User user;

    public record OrderId(@GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orders_seq"
    )
                            @SequenceGenerator(
                                    name = "orders_seq",
                                    sequenceName = "orders_seq",  // Sequenzname in der Datenbank
                                    allocationSize = 1             // Schrittweite setzen
                            )
                            @NotNull Long id){}
}