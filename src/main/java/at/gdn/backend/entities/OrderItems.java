package at.gdn.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "orderitems")
public class OrderItems {
    @EmbeddedId
    private OrderItemsId id;
    @Min(1)
    private Integer quantity;

    @NotNull
    @Min(1)
    private double price;

    @ManyToOne(cascade = {CascadeType.PERSIST},optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_orderitems_2_products"), nullable = true)
    private Product product;

    //Liste - Produkte (derzeit inaktiv)
//    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "orderItems_products",
//            joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "fk_orderItems_2_products")),
//            uniqueConstraints = @UniqueConstraint(name = "orderItem_product",
//                    columnNames = {"orderItems_id", "product_idx"}))
//    @OrderColumn(name = "product_idx")
//    private List<Product> product;

    public record OrderItemsId(@GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orderitems_seq"
    )
                          @SequenceGenerator(
                                  name = "orderitems_seq",
                                  sequenceName = "orderitems_seq",  // Sequenzname in der Datenbank
                                  allocationSize = 1             // Schrittweite setzen
                          )
                          @NotNull Long id){}
}