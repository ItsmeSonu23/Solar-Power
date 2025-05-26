package com.solar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.solar.dto.Queries;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/queries")
public class QueryController {

    @Autowired
    private JavaMailSender mailSender;

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
}
