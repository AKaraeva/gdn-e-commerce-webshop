package at.gdn.backend.service;

import at.gdn.backend.entities.User;
import at.gdn.backend.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Stream;


@Transactional(readOnly = true)
@Service
public class UserManagementService1 {
    private final UserRepository userRepository;

    public UserManagementService1(
            @Autowired UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public Stream<User> findAll(Pageable pageable) {
        return userRepository.findAll( pageable ).stream();
    }

    public void deleteUser(Set<User> userSet) {
        userRepository.deleteAll(userSet);
    }
}