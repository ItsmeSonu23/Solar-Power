package com.solar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.solar.dto.LoginDto;
import com.solar.dto.ResponseDto;
import com.solar.dto.UserDto;
import com.solar.service.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(
        @RequestBody @Valid UserDto userDto) {  // Ensure @Valid is used here
        userDto = userService.registerUser(userDto);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Update an existing user by email")
    @PutMapping("/update/{email}")
    public ResponseEntity<UserDto> updateUser(
        @PathVariable String email,
        @RequestBody @Valid UserDto userDto) throws Exception {  // Ensure @Valid is used here
        userService.updateUserProfile(email, userDto);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Get a user by email")
    @GetMapping("/getUser/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Get all users")
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() throws Exception {
        List<UserDto> userDtos = userService.getAllProfile();
        return ResponseEntity.ok(userDtos);
    }

    @Operation(summary = "Login user and return JWT")
    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(
        @RequestBody @Valid LoginDto loginDto) throws Exception {  // Ensure @Valid is used here
        return new ResponseEntity<>(userService.loginUser(loginDto), HttpStatus.OK);
    }

    @Operation(summary = "Send OTP to user email")
    @PostMapping("/sendOtp/{email}")
    public ResponseEntity<String> sendOtp(@PathVariable String email) throws Exception {
        Boolean result = userService.sendOtp(email);
        if (result) {
            return new ResponseEntity<>("OTP sent successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to send OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Verify OTP for password reset")
    @PostMapping("/verifyOtp/{email}/{otp}")
    public ResponseEntity<ResponseDto> verifyOtp(
        @PathVariable String email,
        @PathVariable String otp) throws Exception {
        Boolean result = userService.verifyOtp(email, otp);
        if (result) {
            return new ResponseEntity<>(new ResponseDto("OTP verified successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseDto("Invalid OTP"), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Reset user password")
    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseDto> resetPassword(
       @RequestBody @Valid LoginDto loginDto) throws Exception {  // Ensure @Valid is used here
        Boolean result = userService.resetPassword(loginDto.getUserEmail(), loginDto.getUserPassword());
        if (result) {
            return new ResponseEntity<>(new ResponseDto("Password reset successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseDto("Failed to reset password"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Send verification OTP to user email")
    @PostMapping("/sendVerificationOtp/{email}")
    public ResponseEntity<String> sendVerificationOtp(@PathVariable String email) throws Exception {
        Boolean result = userService.sendVerificationOtp(email);
        if (result) {
            return new ResponseEntity<>("Verification OTP sent successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to send verification OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Verify verification OTP for email verification")
    @PostMapping("/verifyVerificationOtp/{email}/{otp}")
    public ResponseEntity<ResponseDto> verifyVerificationOtp(
        @PathVariable String email,
        @PathVariable String otp) throws Exception {
        Boolean result = userService.verifyVerificationOtp(email, otp);
        if (result) {
            return new ResponseEntity<>(new ResponseDto("Verification OTP verified successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseDto("Invalid verification OTP"), HttpStatus.BAD_REQUEST);
        }
    }

}
