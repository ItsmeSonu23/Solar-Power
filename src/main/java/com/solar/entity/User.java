package com.solar.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.solar.dto.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor 
@NoArgsConstructor
@Document(collection = "users")
@Schema(description = "User entity representing application users")
public class User {

    @Id
    @Schema(description = "Unique ID of the user", example = "64f70a39427b3f47b8782d65")
    private String userId;

    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Indexed(unique = true)
    @Schema(description = "Unique email of the user", example = "example@gmail.com")
    private String userEmail;

    @Schema(description = "User password (encrypted)", example = "hashed_password")
    private String userPassword;

    @Schema(description = "User's phone number", example = "0000000000")
    private String userPhone;

    @Schema(description = "User role/account type (e.g., ADMIN, CUSTOMER)")
    private AccountType role;

    @Schema(description = "Whether the user is email-verified", example = "false")
    private Boolean isVerified = false;

    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Profile image stored as bytes", hidden = true)
    private byte[] profileImage;

    @Schema(description = "Location of the user", example = "Jaipur, Rajasthan")
    private String location;
}
