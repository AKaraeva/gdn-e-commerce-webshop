package at.gdn.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
@Component
@SessionScope

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "shopping_cart")
public class ProductInventory {

    @EmbeddedId
    private ProductInventoryId id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "shopping_cart_products",
            joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_shopping_cart_products_2_shopping_cart")),
            inverseForeignKey = @ForeignKey(name = "FK_shopping_cart_products_2_products"),
            uniqueConstraints = @UniqueConstraint(name = "U_shopping_cart_products",
                    columnNames = {"shopping_cart_id", "product_id"}))
    //private List<Product> products;
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        // Check if product already exists in the cart, update quantity instead of adding a duplicate
        for (Product p : products) {
            if (p.getProductName().equals(product.getProductName())) {
                p.setProductQuantity(p.getProductQuantity() + product.getProductQuantity());
                return;
            }
        }
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getTotalPrice() {
        return products.stream().mapToDouble(p->p.getProductPrice()*p.getProductQuantity()).sum();
    }
    public record ProductInventoryId(@GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "productinventory_seq"
    )
                            @SequenceGenerator(
                                    name = "productinventory_seq",
                                    sequenceName = "productinventory_seq",  // Sequenzname in der Datenbank
                                    allocationSize = 1             // Schrittweite setzen
                            )
                            @NotNull Long id){}
}