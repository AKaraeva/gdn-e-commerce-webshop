package at.gdn.backend.service;

import at.gdn.backend.entities.Country;
import at.gdn.backend.entities.Product;
import at.gdn.backend.entities.User;
import at.gdn.backend.persistence.repository.ProductRepository;
import at.gdn.backend.persistence.repository.UserRepository;
import at.gdn.backend.richtypes.Firstname;
import at.gdn.backend.richtypes.Lastname;
import at.gdn.backend.richtypes.Username;
import at.gdn.backend.valueobjects.Address;
import at.gdn.backend.valueobjects.PhoneNumber;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.Long.valueOf;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> saveUser(@Valid User user) {
        try{
            return Optional.of(userRepository.save(user));
        } catch (Exception e) {
            System.out.println("Error while saving user: " + e.getMessage());
            return Optional.empty();
        }
    }



    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional <User> getUserById(Long userId) {
        return userRepository.findById(new User.UserId(valueOf(userId)));
    }


    public boolean emailExists(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress).isPresent();
    }


    public Optional<Firstname> findFirstNameByEmail(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress).map(User::getFirstName);
    }

    public Optional<Lastname> findLastNameByEmail(String email) {
        return userRepository.findByEmailAddress(email).map(User::getLastName);
    }

    public Optional<Username> findUserNameByEmail(String email) {
            return userRepository.findByEmailAddress(email).map(User::getUsername);
    }

    public Optional<String> findStreetByEmail(String email) {
        return userRepository.findByEmailAddress(email)
                .map(User::getMainAddress)
                .map(Address::street)
                .or(() -> Optional.of("")); //or other default value
    }


    public Optional<String> findCityByEmail(String email) {
        return userRepository.findByEmailAddress(email)
                .map(User::getMainAddress)
                .map(Address::city)
                .or(() -> Optional.of("")); //or other default value
    }


//COUNTRY NICHT IN DER ENTITY IMPLEMENTIERT
//        public Optional<Country> findCountryByEmail(String email) {
//
//            return userRepository.findByEmailAddress(email)
//                    .map(User::getMainAddress)
//                    .map(Address::country)
//                    .or(() -> Optional.of()); //or other default value
//
//    }


    public Optional<Integer> findDoorNumberByEmail(String email) {
        return userRepository.findByEmailAddress(email)
                .map(User::getMainAddress)
                .map(Address::doorNumber)
                .or(() -> Optional.of(Integer.valueOf(0))); //or other default value
    }

    public Optional<Integer> findPostalCodeByEmail(String email) {
        return userRepository.findByEmailAddress(email)
                .map(User::getMainAddress)
                .map(Address::postalCode)
                .or(() -> Optional.of(Integer.valueOf(0))); //or other default value

    }


    public Optional<Integer> findHouseNumberByEmail(String email) {
        return userRepository.findByEmailAddress(email)
                .map(User::getMainAddress)
                .map(Address::houseNumber)
                .or(() -> Optional.of(Integer.valueOf(0))); //or other default value
    }

    public Optional<Integer> findAreaCodeByEmail(String email) {
        return userRepository.findByEmailAddress(email)
                    .map(User::getPhoneNumber)
                    .map(PhoneNumber::areaCode)
                    .or(() -> Optional.of(Integer.valueOf(0))); //or other default value
        }

    public Optional<Integer> findCountryCodeByEmail(String email) {
        return userRepository.findByEmailAddress(email)
                .map(User::getPhoneNumber)
                .map(PhoneNumber::countryCode)
                .or(() -> Optional.of(Integer.valueOf(0))); //or other default value
    }

    public Optional<String> findSerialNumberByEmail(String email) {
        return userRepository.findByEmailAddress(email)
                .map(User::getPhoneNumber)
                .map(PhoneNumber::serialNumber)
                .or(() -> Optional.of("")); //or other default value
    }

    public Optional <User> findUserByEmail(String email) {
        return userRepository.findByEmailAddress(email);
    }
}