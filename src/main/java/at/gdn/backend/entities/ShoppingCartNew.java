package at.gdn.backend.entities;

import at.gdn.backend.entities.Product;
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
public class ShoppingCartNew {

    @EmbeddedId
    private ShoppingCartId id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "shopping_cart_products",
            joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "FK_shopping_cart_products_2_shopping_cart")),
            inverseForeignKey = @ForeignKey(name = "FK_shopping_cart_products_2_products"),
            uniqueConstraints = @UniqueConstraint(name = "U_shopping_cart_products",
                    columnNames = {"shopping_cart_id", "product_id"}))
    //private List<Product> products;
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        System.out.println("Attempting to add product: " + product.getProductName());

        for (Product p : products) {
            if (p.getProductName().equals(product.getProductName())) {
                p.setProductQuantity(p.getProductQuantity() + product.getProductQuantity());
                System.out.println("Updated product quantity: " + p.getProductQuantity());
                return;
            }
        }

        products.add(product);
        System.out.println("Product added successfully! New cart size: " + products.size());
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
    public record ShoppingCartId(@GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shoppingcart_seq"
    )
                            @SequenceGenerator(
                                    name = "shoppingcart_seq",
                                    sequenceName = "shoppingcart_seq",  // Sequenzname in der Datenbank
                                    allocationSize = 1             // Schrittweite setzen
                            )
                            @NotNull Long id){}
}
