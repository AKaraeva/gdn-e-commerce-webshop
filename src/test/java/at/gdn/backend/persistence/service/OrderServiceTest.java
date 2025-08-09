//package at.gdn.backend.persistence.service;
//
//
//import at.gdn.backend.entities.Order;
//import at.gdn.backend.entities.User;
//import at.gdn.backend.persistence.repository.OrderRepository;
//import at.gdn.backend.persistence.repository.UserRepository;
////import at.gdn.backend.service.OrderService;
////import at.gdn.backend.service.ServiceException;
//import jakarta.persistence.PersistenceException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.assertj.core.api.Assumptions.assumeThat;
//import static org.mockito.ArgumentMatchers.any;
//
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.mockito.Mockito.*;
//import static at.gdn.backend.persistence.service.FixtureFactory.*;
//import static at.gdn.backend.persistence.service.FixtureFactory.SPG;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//class OrderServiceTest {
//
//    private @Mock OrderRepository orderRepository;
//    private @Mock UserRepository userRepository;
//    private OrderService orderService;
//
//    @BeforeEach
//    void setUp() {
//        assumeThat(orderRepository).isNotNull();
//        assumeThat(userRepository).isNotNull();
//        orderService = new OrderService(orderRepository, userRepository);
//    }
//
//    @Test
//    void will_return_empty_optional_when_user_id_is_unknown() {
//
//        var quantity = 50;
//        var userId = 1L;
//
//        when(userRepository.findById(any())).thenReturn(Optional.empty());
//
//        Optional<Order> createOrder = orderService.createOrder(quantity, new User.UserId(userId));
//
//        assertThat(createOrder).isEmpty();
//        verifyNoMoreInteractions(userRepository);
//        verifyNoMoreInteractions(orderRepository);
//    }
//
//    @Test
//    void will_throw_service_exception_for_persistence_exception() {
//
//        var quantity = 50;
//        var userId = 1L;
//
//        when(userRepository.findById(any())).thenThrow(
//                new PersistenceException());
//
//        assertThatThrownBy(() -> orderService.createOrder(quantity, new User.UserId(userId)))
//
//                .isInstanceOf(ServiceException.class);
//
//        verifyNoMoreInteractions(userRepository);
//        verifyNoMoreInteractions(orderRepository);
//    }
//
//    @Test
//    void will_return_optional_when_user_id_is_valid() {
//
//        var quantity = 50;
//        var userId = 1L;
//
//        var austria = austria();
//        var address = spengergasse20(austria);
//        var mainAddress = address("Spengergasse 20", austria);
//        var user = SPG(mainAddress, address);
//
//        when(userRepository.findById(any())).thenReturn(Optional.of(user));
//
//        Optional<Order> createOrder = orderService.createOrder(quantity, new User.UserId(userId));
//
//        assertThat(createOrder).isNotEmpty()
//                               .hasValueSatisfying(order -> {
//                                   assertThat(order.getQuantity()).isEqualTo(quantity);
//                                   assertThat(order.getUser()).isEqualTo(user);
//                               });
//
//        verify(orderRepository).save(any(Order.class));
//        verifyNoMoreInteractions(userRepository);
//    }
//}