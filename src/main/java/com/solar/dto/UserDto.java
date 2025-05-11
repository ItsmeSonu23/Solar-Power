package com.solar.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object for User")
public class UserDto {

    @Id
    @Schema(description = "User ID", example = "64f70a39427b3f47b8782d65")
    private String userId;

    @NotBlank(message = "Name is required")
    @Schema(description = "Full name of the user", example = "Sonu Mishra", required = true)
    private String name;

    @Indexed(unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the user", example = "sonu@gmail.com", required = true)
    private String userEmail;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,15}$",
        message = "{user.password.invalid}"
    )
    @Schema(description = "Password with complexity requirements", example = "Admin@123", required = true)
    private String userPassword;

    @Schema(description = "Phone number of the user", example = "9508416610")
    private String userPhone;

    @Schema(description = "User's role", example = "ADMIN or CUSTOMER")
    private AccountType role;

    @Schema(description = "Whether the email is verified", example = "true")
    private Boolean isVerified = false;

    @Schema(description = "Date and time of account creation", example = "2024-05-03T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Profile image in byte array format", hidden = true)
    private byte[] profileImage;

    @Schema(description = "User's location", example = "Jaipur, Rajasthan")
    private String location;
}
