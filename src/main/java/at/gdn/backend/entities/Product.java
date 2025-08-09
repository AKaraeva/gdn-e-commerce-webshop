package at.gdn.backend.entities;

import at.gdn.backend.valueobjects.ProductImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "products")
public class Product {

    @EmbeddedId
    private ProductId id;

    private String productName;
    private String productDescription;
    @NotNull
    private Integer productPrice;
    @Positive
    private Integer productQuantity;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_image",
            joinColumns = @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_product_image_2_products")))
    @OrderColumn(name = "order_idx")
    private List<ProductImage> productImage;
// = new ArrayList<>()


    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER) // One photo can have many categories
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_product_categories_2_products"), nullable = true),
            inverseForeignKey = @ForeignKey(name = "FK_product_categories_2_categories"),
            uniqueConstraints = @UniqueConstraint(name = "U_product_categories",
                    columnNames = {"product_id", "category_idx"}))
    @OrderColumn(name = "category_idx")
    private List<Category> categories;

    public record ProductId(@GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "products_seq"
    )
                               @SequenceGenerator(
                                       name = "products_seq",
                                       sequenceName = "products_seq",  // Sequenzname in der Datenbank
                                       allocationSize = 1             // Schrittweite setzen
                               )
                               @NotNull Long id){}
}