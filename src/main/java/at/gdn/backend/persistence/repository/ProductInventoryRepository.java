package at.gdn.backend.persistence.repository;

import at.gdn.backend.entities.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, ProductInventory.ProductInventoryId> {
}
