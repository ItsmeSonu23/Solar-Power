package com.solar.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.solar.dto.Queries;
import com.solar.entity.Otp;
import com.solar.repo.OtpRepo;
import com.solar.utility.Data;
import com.solar.utility.Utilities;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/queries")
public class QueryController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpRepo otpRepo;
    
    @Autowired
    private Queries queries;// Assuming you have an OtpRepository to handle Otp entities

    @PostMapping("/sendQuery")
    public ResponseEntity<String> sendQuery(@RequestBody Queries queries) throws MessagingException {
        // Logic to send email using mailSender
        MimeMessage mm = mailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
        mmh.setTo("asasino3180@gmail.com");
        mmh.setSubject(queries.getConnectionType());
        mmh.setText("First Name: " + queries.getFirstName() + "\n" +
                    "Last Name: " + queries.getLastName() + "\n"+
                    "Phone Number: " + queries.getPhoneNumber() + "\n" +
                    "Email: " + queries.getEmail() + "\n" +
                    "Country: " + queries.getCountry() + "\n" + 
                    "City: " + queries.getCity() + "\n" +
                    "Message: " + queries.getMessage() + "\n");
        mmh.setFrom(queries.getEmail());
        mailSender.send(mm);

        return ResponseEntity.ok("Query sent successfully!");
    }


    @PostMapping("/sendOtp")
   public ResponseEntity<String> sendOtp(@PathVariable String email) throws MessagingException {
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

   @PostMapping("/verifyOtp/{email}/{otp}")
    public ResponseEntity<String> verifyOtp(@PathVariable String otp) throws Exception {
        Otp otpentity = otpRepo.findById(queries.getEmail()).orElseThrow(() -> new RuntimeException("OTP_NOT_FOUND"));
        if (!otpentity.getOtpCode().equals(otp))
            throw new RuntimeException("OTP_INCORRECT");
        return ResponseEntity.ok("OTP verified successfully!");
    }
    
}
