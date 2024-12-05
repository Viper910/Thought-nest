package io.microservices.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.microservices.authentication.dto.request.UserCreationRequest;
import io.microservices.authentication.dto.request.UserLoginRequest;
import io.microservices.authentication.dto.response.ApiResponse;
import io.microservices.authentication.exceptions.UserNotFoundException;
import io.microservices.authentication.model.User;
import io.microservices.authentication.services.AuthenticationService;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> logIn(@RequestBody UserLoginRequest userData) {
        try {
            String token = authenticationService.loginUser(userData);
            return ResponseEntity.ok(ApiResponse.success(token, "Successfull logged in to user."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("Unable to logged in.", Collections.singletonList(e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Unable to logged in.", Collections.singletonList(e.getMessage())));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<User>> signIn(@RequestBody UserCreationRequest userData) {
        User user = null;
        try {
            user = authenticationService.saveUser(userData);
            return ResponseEntity.ok(ApiResponse.success(user, "Successfull created an user."));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Unable to create an user.", Collections.singletonList(e.getMessage())));
        }
    }

}
