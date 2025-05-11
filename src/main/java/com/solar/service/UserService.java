package com.solar.service;

import java.util.List;

// import org.springframework.stereotype.Service;

import com.solar.dto.LoginDto;
import com.solar.dto.UserDto;

// @Service(value = "userService")
public interface UserService {
    public UserDto registerUser(UserDto userDto);
    public UserDto loginUser(LoginDto loginDto);
    public Boolean sendOtp(String email) throws Exception;
    public Boolean verifyOtp(String email, String otp) throws Exception;
    public Boolean resetPassword(String email, String newPassword);
    public UserDto getUserByEmail(String email);
    public UserDto updateUserProfile(String email, UserDto userDto) throws Exception;
    public List<UserDto> getAllProfile() throws Exception;
    public Boolean sendVerificationOtp(String email) throws Exception;
    public Boolean verifyVerificationOtp(String email, String otp) throws Exception;
}
