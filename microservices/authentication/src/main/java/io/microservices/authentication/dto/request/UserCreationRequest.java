package io.microservices.authentication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Date;

import lombok.Data;

@Data
public class UserCreationRequest {

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message = "Username should be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, digits, underscores, and hyphens")
    private String username;

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    private String firstname;

    @Size(max = 50, message = "Middle name should be up to 50 characters")
    private String middlename;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name should be between 2 and 50 characters")
    private String lastname;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email should be up to 100 characters")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])", message = "Password must contain at least one uppercase letter, one digit, and one special character")
    private String password; // Store hashed password

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format") // E.g., +1234567890 or 1234567890
    private String phoneNumber;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @NotNull(message = "Gender cannot be null")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be one of: Male, Female, Other")
    private String gender;

    @NotNull(message = "Address Line 1 cannot be null")
    @Size(min = 5, max = 100, message = "Address Line 1 should be between 5 and 100 characters")
    private String addressLine1;

    @Size(max = 100, message = "Address Line 2 should be up to 100 characters")
    private String addressLine2;

    @NotNull(message = "City cannot be null")
    @Size(min = 2, max = 50, message = "City should be between 2 and 50 characters")
    private String city;

    @NotNull(message = "State cannot be null")
    @Size(min = 2, max = 50, message = "State should be between 2 and 50 characters")
    private String state;

    @NotNull(message = "Country cannot be null")
    @Size(min = 2, max = 50, message = "Country should be between 2 and 50 characters")
    private String country;

    @NotNull(message = "Postal code cannot be null")
    @Pattern(regexp = "^[0-9]{5,6}$", message = "Postal code must be a 5 or 6 digit number")
    private String postalCode;

    private String profilePictureUrl; // Optional: URL to user's profile picture
}
