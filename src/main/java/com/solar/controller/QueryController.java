package com.solar.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.solar.dto.Queries;
import com.solar.entity.Otp;
import com.solar.repo.OtpRepo;
import com.solar.utility.Data;
import com.solar.utility.Utilities;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Query Controller", description = "Handles customer queries and OTP verification")
@RestController
@RequestMapping("/api/v1/queries")
@CrossOrigin("*")
public class QueryController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpRepo otpRepo;

    @Operation(summary = "Send a customer query via email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Query sent successfully"),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PostMapping("/sendQuery")
    public ResponseEntity<String> sendQuery(
            @RequestBody @Parameter(description = "Query data including email, name, location, and message") Queries queries)
            throws MessagingException {
        // Logic to send email using mailSender
        MimeMessage mm = mailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
        mmh.setTo("asasino3180@gmail.com");
        mmh.setSubject(queries.getConnectionType());
        mmh.setText(Data.getQueryEmailBody(
                queries.getFirstName(),
                queries.getLastName(),
                queries.getPhoneNumber(),
                queries.getEmail(),
                queries.getCountry(),
                queries.getCity(),
                queries.getConnectionType(),
                queries.getMessage()), true); // true to indicate HTML content
        mmh.setFrom(queries.getEmail());

        mailSender.send(mm);

        return ResponseEntity.ok("Query sent successfully!");
    }

    @Operation(summary = "Send an OTP to the user's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PostMapping("/sendOtp/{email}")
    public ResponseEntity<String> sendOtp(
            @PathVariable @Parameter(description = "Email address to send OTP") String email)
            throws MessagingException {
        // Logic to send OTP email using mailSender
        MimeMessage mm = mailSender.createMimeMessage();
        MimeMessageHelper msg = new MimeMessageHelper(mm, true);
        msg.setTo(email);
        msg.setSubject("Your OTP code from Clover");

        String genOtp = Utilities.generateOTP();

        Otp otp = new Otp(email, genOtp, LocalDateTime.now());
        otpRepo.save(otp);
        msg.setText(Data.getMessageBody(email, genOtp), true);
        mailSender.send(mm);
        return ResponseEntity.ok("OTP sent successfully to " + email + "! Please check your inbox.");
    }

    @Operation(summary = "Verify the OTP sent to the user's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid OTP or Email"),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam @Parameter(description = "Email used for OTP") String email,
            @RequestParam @Parameter(description = "OTP code to verify") String otp) throws Exception {
        Otp otpentity = otpRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("OTP_NOT_FOUND"));
        if (!otpentity.getOtpCode().equals(otp))
            throw new RuntimeException("OTP_INCORRECT");
        return ResponseEntity.ok("OTP verified successfully!");
    }

}
