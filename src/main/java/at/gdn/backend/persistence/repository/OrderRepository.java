package at.gdn.backend.persistence.repository;

import at.gdn.backend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Order.OrderId> {

    List<Order> findAllById(Long id);

}