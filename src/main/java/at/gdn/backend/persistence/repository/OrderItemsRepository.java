package at.gdn.backend.persistence.repository;


import at.gdn.backend.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, OrderItems.OrderItemsId> {
}
