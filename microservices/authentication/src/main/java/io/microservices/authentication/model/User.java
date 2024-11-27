package io.microservices.authentication.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document(collection = "Users")
@Data
public class User {

    @Id
    private String id;

    @NotNull
    private String firstname;

    private String middlename;

    @NotNull
    private String lastname;

    @NotNull
    private String email;

    @NotNull
    private String password; // Store hashed password

    @NotNull
    private String phoneNumber;

    @NotNull
    private Date dateOfBirth;

    @NotNull
    private String gender;

    @NotNull
    private String addressLine1;
    private String addressLine2;
    @NotNull
    private String city;
    @NotNull
    private String state;
    @NotNull
    private String country;
    @NotNull
    private String postalCode;

    private List<String> roles; // E.g., GOLD,NORMAL

    private boolean deactivated; // To indicate if the user's account has temporarily deactivated
    private boolean isLocked; // For account lockout after multiple failed login attempts

    private byte failedLoginAttempt;
    private long accountLockTimestamp; //Store time when the account got locked.
    private Date lastLogin;

    private double darkHumourScore;
    private double goodHumourScore;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String profilePictureUrl; // Optional: URL to user's profile picture

}