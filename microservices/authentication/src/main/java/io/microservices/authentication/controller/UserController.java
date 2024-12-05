package io.microservices.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.microservices.authentication.dto.request.UserCreationRequest;
import io.microservices.authentication.dto.response.ApiResponse;
import io.microservices.authentication.exceptions.UserNotFoundException;
import io.microservices.authentication.model.User;
import io.microservices.authentication.services.AuthenticationService;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId) {
        // Todo: Provide the userid data.
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String id) {
        try {
            String message = authenticationService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success(message, "User deleted."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("Unable to delete user.", Collections.singletonList(e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Unable to delete user.", Collections.singletonList(e.getMessage())));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<User>> update(@PathVariable String id,
            @RequestBody UserCreationRequest userData) {
        User user = null;
        try {
            user = authenticationService.updateUser(id, userData);
            return ResponseEntity.ok(ApiResponse.success(user, "Successfull update the user."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("Unable to update user.", Collections.singletonList(e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Unable to update  user.", Collections.singletonList(e.getMessage())));
        }
    }

}
