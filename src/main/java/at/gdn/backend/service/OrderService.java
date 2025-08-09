package at.gdn.backend.service;

import at.gdn.backend.entities.Order;
import at.gdn.backend.entities.OrderItems;
import at.gdn.backend.entities.User;
import at.gdn.backend.persistence.repository.OrderItemsRepository;
import at.gdn.backend.persistence.repository.OrderRepository;
import at.gdn.backend.persistence.repository.UserRepository;
import jakarta.persistence.PersistenceException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemsRepository orderItemsRepository;

    public Optional<Order> createOrder(User.@NotNull UserId userId, @NotNull List<OrderItems> orderItems) {
        try {
            return userRepository.findById(userId)
                    .map(user -> {
                        // Save OrderItems first
                        List<OrderItems> savedOrderItems = orderItems.stream()
                                .map(orderItemsRepository::save)
                                .toList();

                        // Create and save the Order
                        Order order = Order.builder()
                                .user(user)
                                .orderItems(savedOrderItems)
                                .build();
                        orderRepository.save(order);
                        return order;
                    });
        } catch (PersistenceException pEx) {
            throw ServiceException.whileCreatingOrder(userId, pEx);
        }
    }
}