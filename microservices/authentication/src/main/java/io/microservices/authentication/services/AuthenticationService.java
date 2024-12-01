package io.microservices.authentication.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.microservices.authentication.constants.Role;
import io.microservices.authentication.dto.request.UserCreationRequest;
import io.microservices.authentication.dto.request.UserLoginRequest;
import io.microservices.authentication.exceptions.AccountLocked;
import io.microservices.authentication.exceptions.IncorrectPassword;
import io.microservices.authentication.exceptions.UserEmailAlreadyExistException;
import io.microservices.authentication.exceptions.UserNotFoundException;
import io.microservices.authentication.model.User;
import io.microservices.authentication.repository.UserRepository;
import io.microservices.authentication.serviceTemplates.AuthenticationServiceTemplate;

@Service
public class AuthenticationService implements AuthenticationServiceTemplate {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(UserCreationRequest userCreationRequest) {
        if (userEmailExist(userCreationRequest.getEmail()) != null) {
            throw new UserEmailAlreadyExistException();
        }
        User user = new User(userCreationRequest);
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        user.setRoles(List.of(Role.NORMAL.name()));
        userRepository.save(user);
        return user;
    }

    @Override
    public String loginUser(UserLoginRequest userLoginRequest) {
        if (userLoginRequest.getUsername() == null && userLoginRequest.getEmail() == null) {
            throw new UserNotFoundException("Please provide either username or email.");
        }
        String loginDetail = "";
        if (userLoginRequest.getEmail() == null) {
            loginDetail = userLoginRequest.getUsername();
        }
        if (userLoginRequest.getUsername() == null) {
            loginDetail = userLoginRequest.getEmail();
        }
        User user = userEmailExist(loginDetail);
        if (user == null) {
            throw new UserNotFoundException("User not exit with " + loginDetail);
        }
        if (!user.isAccountNonLocked()) {
            if (!duration30Min(user.getAccountLockTimestamp())) {
                throw new AccountLocked("Account is locked for 30m for userID:" + user.getId());
            } else {
                user.setFailedLoginAttempt((byte) 0);
                user.setLocked(false);
                userRepository.save(user);
            }
        }
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            user.setFailedLoginAttempt((byte) (user.getFailedLoginAttempt() + 1));
            if (user.getFailedLoginAttempt() >= 5) {
                user.setLocked(true);
                userRepository.save(user);
                throw new AccountLocked("Account is locked for 30m for userID:" + user.getId());
            }
            user.setAccountLockTimestamp(System.currentTimeMillis());
            userRepository.save(user);
            throw new IncorrectPassword("You have only " + (5 - user.getFailedLoginAttempt()) + " attempts left.");
        }
        user.setFailedLoginAttempt((byte) 0);
        user.setLocked(false);
        userRepository.save(user);
        return generateToken(user);
    }

    private User userEmailExist(String userNameOrEmail) {
        Optional<List<User>> user = userRepository.findByUsernameOrEmail(userNameOrEmail, userNameOrEmail);
        if (user.isPresent()) {
            if (user.get().size() != 0) {
                return user.get().get(0);
            }
        }
        return null;
    }

    @Override
    public User updateUser(String id, UserCreationRequest userCreationRequest) {
        // Optional<User> user = userRepository.findById(id);
        // if(user.isPresent()){
        // user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        // }
        // Todo: Updating the user
        return null;
    }

    @Override
    public String deleteUser(String id) {
        if (id == null) {
            throw new UserNotFoundException("Please provide a correct id.");
        }
        userRepository.deleteById(id);
        return "User got deleted with id: " + id + " .";
    }

    private boolean duration30Min(long lockTime) {
        long thirtyMinutesInMillis = 30 * 60 * 1000;
        return (System.currentTimeMillis() - lockTime) >= thirtyMinutesInMillis;
    }

    private String generateToken(User user) {
        String token = jwtService.generateToken(user);
        return token;
    }

    private boolean validateToken(String token) {
        boolean isValid = jwtService.validateToken(token);
        return isValid;
    }
}
