package io.microservices.authentication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserLoginRequest {

    @Size(min = 3, max = 50, message = "Username should be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, digits, underscores, and hyphens")
    private String username;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email should be up to 100 characters")
    private String email;
    
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])", message = "Password must contain at least one uppercase letter, one digit, and one special character")
    private String password; 

}
