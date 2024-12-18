package io.microservices.authentication.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.microservices.authentication.dto.request.UserCreationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "Users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;

    @NotNull
    @Indexed(unique = true)
    private String username;

    @NotNull
    private String firstname;

    private String middlename;

    @NotNull
    private String lastname;

    @NotNull
    @Indexed(unique = true)
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
    private long accountLockTimestamp; // Store time when the account got locked.
    private Date lastLogin;

    private double darkHumourScore;
    private double goodHumourScore;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String profilePictureUrl; // Optional: URL to user's profile picture

    public User(UserCreationRequest userCreationRequest) {
        if (userCreationRequest.getUsername() != null) {
            this.username = userCreationRequest.getUsername();
        }
        if (userCreationRequest.getFirstname() != null) {
            this.firstname = userCreationRequest.getFirstname();
        }
        if (userCreationRequest.getMiddlename() != null) {
            this.middlename = userCreationRequest.getMiddlename();
        }
        if (userCreationRequest.getLastname() != null) {
            this.lastname = userCreationRequest.getLastname();
        }
        if (userCreationRequest.getEmail() != null) {
            this.email = userCreationRequest.getEmail();
        }
        if (userCreationRequest.getDateOfBirth() != null) {
            this.dateOfBirth = userCreationRequest.getDateOfBirth();
        }
        if (userCreationRequest.getGender() != null) {
            this.gender = userCreationRequest.getGender();
        }
        if (userCreationRequest.getAddressLine1() != null) {
            this.addressLine1 = userCreationRequest.getAddressLine1();
        }
        if (userCreationRequest.getAddressLine2() != null) {
            this.addressLine2 = userCreationRequest.getAddressLine2();
        }
        if (userCreationRequest.getCity() != null) {
            this.city = userCreationRequest.getCity();
        }
        if (userCreationRequest.getState() != null) {
            this.state = userCreationRequest.getState();
        }
        if (userCreationRequest.getCountry() != null) {
            this.country = userCreationRequest.getCountry();
        }
        if (userCreationRequest.getPostalCode() != null) {
            this.postalCode = userCreationRequest.getPostalCode();
        }
    }

    public void updatedUser(UserCreationRequest userCreationRequest) {
        if (userCreationRequest.getUsername() != null) {
            this.username = userCreationRequest.getUsername();
        }
        if (userCreationRequest.getFirstname() != null) {
            this.firstname = userCreationRequest.getFirstname();
        }
        if (userCreationRequest.getMiddlename() != null) {
            this.middlename = userCreationRequest.getMiddlename();
        }
        if (userCreationRequest.getLastname() != null) {
            this.lastname = userCreationRequest.getLastname();
        }
        if (userCreationRequest.getEmail() != null) {
            this.email = userCreationRequest.getEmail();
        }
        if (userCreationRequest.getDateOfBirth() != null) {
            this.dateOfBirth = userCreationRequest.getDateOfBirth();
        }
        if (userCreationRequest.getGender() != null) {
            this.gender = userCreationRequest.getGender();
        }
        if (userCreationRequest.getAddressLine1() != null) {
            this.addressLine1 = userCreationRequest.getAddressLine1();
        }
        if (userCreationRequest.getAddressLine2() != null) {
            this.addressLine2 = userCreationRequest.getAddressLine2();
        }
        if (userCreationRequest.getCity() != null) {
            this.city = userCreationRequest.getCity();
        }
        if (userCreationRequest.getState() != null) {
            this.state = userCreationRequest.getState();
        }
        if (userCreationRequest.getCountry() != null) {
            this.country = userCreationRequest.getCountry();
        }
        if (userCreationRequest.getPostalCode() != null) {
            this.postalCode = userCreationRequest.getPostalCode();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map((role) -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !deactivated;
    }
}