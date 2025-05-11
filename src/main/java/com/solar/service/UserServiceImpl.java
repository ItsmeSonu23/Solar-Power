package com.solar.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.solar.dto.AccountType;
import com.solar.dto.LoginDto;
import com.solar.dto.UserDto;
import com.solar.entity.Otp;
import com.solar.entity.User;
import com.solar.repo.OtpRepo;
import com.solar.repo.UserRepo;
import com.solar.utility.Data;
import com.solar.utility.Utilities;

import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OtpRepo otpRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserDto userDto) {
        Optional<User> user = userRepo.findByUserEmail(userDto.getUserEmail());
        if (user.isPresent()) {
            throw new RuntimeException("User already exists with this email id");
        }
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setUserEmail(userDto.getUserEmail());
        userDto.setName(userDto.getName());
        userDto.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        userDto.setIsVerified(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setRole(AccountType.USER);
        userDto.setProfileImage(null);
        userDto.setLocation("");
        userDto.setUserPhone("0000000000");
        User usermap = modelMapper.map(userDto, User.class);
        usermap = userRepo.save(usermap);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto loginUser(LoginDto loginDto) {
        User user = userRepo.findByUserEmail(loginDto.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginDto.getUserPassword(), user.getUserPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public Boolean sendOtp(String email) throws Exception {
        User user = userRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("OTP for Password Reset from Solar System");
        String newotp = Utilities.generateOTP();
        Otp otp = new Otp(email, newotp, LocalDateTime.now());
        otpRepo.save(otp);
        helper.setText(Data.getMessageBody(user.getName(), newotp), true);
        mailSender.send(message);
        return true;
    }

    @Override
    public Boolean verifyOtp(String email, String otp) throws Exception {
        Otp otpobj = otpRepo.findById(email).orElseThrow(() -> new RuntimeException("Otp not found"));
        if (!otpobj.getOtpCode().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        return true;
    }

    @Override
    public Boolean resetPassword(String email, String newPassword) {
        User user = userRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        return true;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUserProfile(String userEmail, UserDto userDto) throws Exception {
        User existingUser = userRepo.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("PROFILE_NOT_FOUND"));
        if (userDto.getProfileImage() != null) {
            existingUser.setProfileImage(Base64.getDecoder().decode(userDto.getProfileImage()));
        }
        userRepo.save(modelMapper.map(userDto, User.class));
        // Convert the Profile entity to ProfileDto using ModelMapper
        return userDto;
    }

    @Override
    public List<UserDto> getAllProfile() throws Exception {
        List<User> profiles = userRepo.findAll();
        return profiles.stream().map(profile -> modelMapper.map(profile, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean sendVerificationOtp(String email) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("OTP for Email Verification from Solar System");
        String newotp = Utilities.generateOTP();
        Otp otp = new Otp(email, newotp, LocalDateTime.now());
        otpRepo.save(otp);
        helper.setText(Data.getVerificationEmailBody(email, newotp), true);
        mailSender.send(message);
        return true;
    }

    @Override
    public Boolean verifyVerificationOtp(String email, String otp) throws Exception {
        Otp otpobj = otpRepo.findById(email).orElseThrow(() -> new RuntimeException("Otp not found"));
        if (!otpobj.getOtpCode().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        User user = userRepo.findByUserEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsVerified(true);
        return true;
    }

}
