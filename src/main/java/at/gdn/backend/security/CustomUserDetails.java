package at.gdn.backend.security;
import at.gdn.backend.entities.User;
import at.gdn.backend.persistence.repository.UserRepository;
import at.gdn.backend.richtypes.Lastname;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetails implements UserDetailsService {
    @Autowired
private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: "+ email));

       GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" +
               user.getUserRole().name());

        Set<GrantedAuthority> authorities = Set.of(authority);
//        Set<GrantedAuthority> authorities = new HashSet<>();
//                     authorities.add(authority);


        return new org.springframework.security.core.userdetails.User(user.getEmailAddress(),
                user.getEncodedPassword(),
                authorities);
    }
}
