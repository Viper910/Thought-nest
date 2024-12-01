package io.microservices.authentication.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.microservices.authentication.model.User;
import io.microservices.authentication.repository.UserRepository;

public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<List<User>> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user.isPresent()) {
            if(user.get().size() == 0){
                throw new UsernameNotFoundException("The following username not exist.");
            }
        }
        return user.get().get(0);
    }
    
}
