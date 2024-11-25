package io.microservices.authentication.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document(collection = "Users")
@Data
public class User {
    
    @Id
    private String id;
    
    private String firstname;
    private String middlename;
    private String lastname;
    private String email;
    private String password; // Store hashed password
    private String phoneNumber;
    private Date dateOfBirth;
    private String gender;
    
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    
    private List<String> roles; // E.g., User, Admin, Moderator
    
    private boolean isActive; // To indicate if the user's account is active
    private boolean isLocked; // For account lockout after multiple failed login attempts
    
    private Date lastLogin;
    
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private String profilePictureUrl; // Optional: URL to user's profile picture
    
}
