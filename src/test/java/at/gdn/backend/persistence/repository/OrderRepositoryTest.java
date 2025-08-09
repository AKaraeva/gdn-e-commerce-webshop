package at.gdn.backend.persistence.repository;


//import at.gdn.TestcontainerConfiguration;
import at.gdn.backend.entities.*;
import at.gdn.backend.enums.*;
import at.gdn.backend.richtypes.Firstname;
import at.gdn.backend.richtypes.Lastname;

import at.gdn.backend.richtypes.Username;
import at.gdn.backend.valueobjects.Address;
import at.gdn.backend.valueobjects.PhoneNumber;
import at.gdn.backend.valueobjects.ProductImage;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@Import(TestcontainerConfiguration.class)
class OrderRepositoryTest {


    private @Autowired OrderRepository orderRepository;


    @Test
    void save_it_test() {
        var order = Order.builder().build();
        var savedOrder = orderRepository.saveAndFlush(order);
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    void will_find_order_with_id() {

        var austria = Country.builder()
                .areaCode(45)
                .iso2Code("AT")
                .cityName("Vienna")
                .build();


        var mainAddress = Address.builder()
                .street("Spengergasse")
                .houseNumber(20)
                .doorNumber(5)
                .country(austria)
                .postalCode(1050)
                .addressType(AddressType.PRIVATE)
                .build();

        var additionalAddress = Address.builder()
                .street("Mariahilferstraße")
                .houseNumber(1)
                .doorNumber(1)
                .country(austria)
                .postalCode(1070)
                .addressType(AddressType.DELIVERY)
                .build();

        var category = Category.builder()
                .categoryName("TestCategory")
                .categoryDescription("TestDescription")
                .department(Department.ACCESSORIES)
                .build();


        var product01 = Product.builder()
                .productName("TestProduct")
                .productDescription("TestDescription")
                .productPrice(35)
                .productQuantity(1)
                .productImage(List.of(new ProductImage("image.jpg")))
                .categories(List.of(category))
                .build();

        var user01 = User.builder()
                .firstName(Firstname.of("Thomas"))
                .lastName(Lastname.of("Bretzina"))
                .username(Username.builder().username("gdn").build())
                .emailAddress("gdn@gdn.at")
                .phoneNumber(PhoneNumber.builder()
                        .areaCode(22)
                        .serialNumber("22123123")
                        .countryCode(2)
                        .build())
                .mainAddress(mainAddress)
                .additionalAddresses(List.of(additionalAddress))
                .userRole(UserRole.CUSTOMER)
                .encodedPassword("secret")
                .build();

        var orderitems = OrderItems.builder()
                .quantity(1)
                .price(35.0)
                .product(product01)
                .build();

        var payment01 = Payment.builder()
                .paymentDate(LocalDate.now())
                .paymentAmount(35.0)
                .paymentMethod(PaymentMethod.CREDITCARD)
                .paymentStatus(PaymentStatus.PAID)
                .build();

        var order = Order.builder()
               // .quantity(1)
                .orderDate(LocalDate.now())
                .status(OrderStatus.PENDING)
                .totalPrice(34.5)
                .shippingDate(LocalDate.now())
                .deliveryDate(LocalDate.now())
                .orderItems(List.of(orderitems))
                .payment(payment01)
                .user(user01)
                .build();




        orderRepository.saveAndFlush(order);




        // Sicherstellen, dass die Order-Attribute nicht null sind
        assumeThat(order.getId()).isNotNull();
        assumeThat(order.getStatus()).isNotNull();
        assumeThat(order.getTotalPrice()).isNotNull();
        assumeThat(order.getShippingDate()).isNotNull();
        assumeThat(order.getDeliveryDate()).isNotNull();

        assumeThat(user01.getId().id()).isNotNull();
        assumeThat(order.getId().id()).isNotNull();
        assumeThat(austria.getId()).isNotNull();
        assumeThat(orderitems.getId()).isNotNull();
        // assumeThat(payment01.getId()).isNotNull();
        // assumeThat(product01.getProductName()).isNotNull();
        assumeThat(mainAddress.country().getId()).isNotNull();


        // Order aus der Datenbank lesen
        var read = orderRepository.findById(order.getId());

        // Überprüfen
        assertThat(read).contains(order);
        assertThat(read).isPresent();
        assertThat(read.get()).isEqualTo(order);
    }
}