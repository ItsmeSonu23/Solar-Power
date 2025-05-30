package com.solar.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.solar.entity.Otp;
import com.solar.repo.OtpRepo;
import com.solar.utility.Data;
import com.solar.utility.Utilities;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class OtpService {
    @Autowired
    private OtpRepo otpRepo;

    @Autowired
    private JavaMailSender mailSender;

    public String generateOtp(String email) throws MessagingException {
        // Check if an OTP already exists for the email
        if (otpRepo.findByEmail(email).isPresent()) {
            otpRepo.deleteByEmail(email);
        }
        MimeMessage mm = mailSender.createMimeMessage();
        MimeMessageHelper msg = new MimeMessageHelper(mm, true);
        msg.setTo(email);
        msg.setSubject("Your OTP code from Solar Solutions");
        String genOtp = Utilities.generateOTP();
        Otp otp = new Otp(email, genOtp, LocalDateTime.now());
        otpRepo.save(otp);
        msg.setText(Data.getMessageBody(email, genOtp), true);
        mailSender.send(mm); 
        return genOtp;
    }

    public Boolean verifyOtp(String email, String otp) throws Exception {
        Otp otpentity = otpRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("OTP_NOT_FOUND"));
        if (!otpentity.getOtpCode().equals(otp))
            throw new RuntimeException("OTP_INCORRECT");
        return true;
    }

    @Scheduled(fixedRate = 60000)
    public void removeExpiredOtps() {
        LocalDateTime expiry = LocalDateTime.now().minusMinutes(5);
        List<Otp> expiredOtps = otpRepo.findByCreationTimeBefore(expiry);
        if (!expiredOtps.isEmpty()) {
            otpRepo.deleteAll(expiredOtps);
        }
    }
}
