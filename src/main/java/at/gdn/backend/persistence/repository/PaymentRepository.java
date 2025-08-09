package at.gdn.backend.persistence.repository;

import at.gdn.backend.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Payment.PaymentId> {

}
