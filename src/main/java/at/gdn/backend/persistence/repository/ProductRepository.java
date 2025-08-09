package at.gdn.backend.persistence.repository;

import at.gdn.backend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Product.ProductId> {

    List<Product> findByCategories_categoryName(String category);

    Optional<Product> findById(Product.ProductId productId);
}