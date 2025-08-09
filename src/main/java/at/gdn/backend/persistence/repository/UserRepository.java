package at.gdn.backend.persistence.repository;


import at.gdn.backend.entities.Product;
import at.gdn.backend.entities.User;
import at.gdn.backend.richtypes.Lastname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, User.UserId> {

    Optional<User> findByEmailAddress(String emailAddress);

    Optional<User> findById(User.UserId userId);
}