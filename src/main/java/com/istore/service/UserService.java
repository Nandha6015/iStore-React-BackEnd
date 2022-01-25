package com.istore.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.istore.common.ApiResponse;
import com.istore.common.BadRequestException;
import com.istore.common.Error;
import com.istore.data.LoginData;
import com.istore.data.ProfileData;
import com.istore.dto.LoginRequestDTO;
import com.istore.dto.LoginResponseDTO;
import com.istore.dto.ProfileDTO;
import com.istore.dto.SignUpRequestDTO;
import com.istore.entity.User;
import com.istore.jwt.JwtUtil;
import com.istore.repository.UserRepo;
import com.istore.validator.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Invalid UserEmail", email));
        }
        return user;
    }

    // To validate and store the user signup data
    public ApiResponse getSignUp(SignUpRequestDTO newUserData) {
        ApiResponse apiResponse = new ApiResponse();
        LoginData loginData = new LoginData();

        List<Error> errors = userValidator.validateSignUpData(newUserData);
        if (errors.size() == 0) {
            User user = new User();
            user.setName(newUserData.getName());
            user.setEmail(newUserData.getEmail());
            user.setPassword(newUserData.getPassword());
            user.setRole(userValidator.getUserType(newUserData.getEmail()));
            user.setCreatedAt(LocalDateTime.now());
            user.setLoginCount(1);
            userRepo.save(user);

            loginData.setUser(
                    new LoginResponseDTO(user.getId(), user.getRole(), "Bearer " + jwtUtil.generateToken(user)));

        } else {
            throw new BadRequestException("Bad Request", errors);
        }

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(loginData);

        return apiResponse;
    }

    // To Validate user login data
    public ApiResponse getLogin(LoginRequestDTO userData) {
        ApiResponse apiResponse = new ApiResponse();

        List<Error> errors = userValidator.validateLoginData(userData);
        if (errors.size() == 0) {

            User user = userRepo.findByEmail(userData.getEmail());
            user.setLoginCount(user.getLoginCount() + 1);
            userRepo.save(user);

            LoginData loginData = new LoginData();
            loginData.setUser(
                    new LoginResponseDTO(user.getId(), user.getRole(), "Bearer " + jwtUtil.generateToken(user)));

            apiResponse.setStatus(HttpStatus.OK.value());
            apiResponse.setData(loginData);
        } else {
            throw new BadRequestException("Bad Request", errors);
        }

        return apiResponse;
    }

    // To Get Profile
    public ApiResponse getProfile(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        ProfileData profileData = new ProfileData();
        List<Error> errors = userValidator.validateUser(id);
        if (errors.size() == 0) {
            User user = userRepo.findById(id).orElse(null);
            ProfileDTO profile = new ProfileDTO();
            profile.setName(user.getName());
            profile.setEmail(user.getEmail());
            profile.setPassword(user.getPassword());
            profile.setPhoneNumber(user.getPhoneNumber());
            profile.setAddress(user.getAddress());
            profileData.setProfile(profile);
            apiResponse.setStatus(HttpStatus.OK.value());
            apiResponse.setData(profileData);
        } else {
            throw new BadRequestException("Bad Request", errors);
        }
        return apiResponse;
    }

    // To Update Profile
    public ApiResponse updateProfile(Long id, ProfileDTO profileDTO) {
        ApiResponse apiResponse = new ApiResponse();
        ProfileData profileData = new ProfileData();
        User user = userRepo.findById(id).orElse(null);
        user.setName(profileDTO.getName());
        user.setEmail(profileDTO.getEmail());
        user.setPassword(profileDTO.getPassword());
        user.setPhoneNumber(profileDTO.getPhoneNumber());
        user.setAddress(profileDTO.getAddress());
        userRepo.save(user);
        apiResponse.setStatus(HttpStatus.OK.value());
        profileData.setMessage("Profile Updated Successfully..!!!");
        apiResponse.setData(profileData);
        return apiResponse;
    }

    public ApiResponse getAllUsers() {
        ApiResponse apiResponse = new ApiResponse();

        List<ProfileDTO> profileDTOs = new ArrayList<>();
        List<User> users = userRepo.findAll();

        for (User user : users) {
            if(user.getRole().equals("ADMIN")) continue;

            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setId(user.getId());
            profileDTO.setEmail(user.getEmail());
            profileDTO.setName(user.getName());
            profileDTO.setIsEnable(user.isEnabled());
            profileDTOs.add(profileDTO);
        }

        ProfileData profileData = new ProfileData();
        profileData.setUsers(profileDTOs);

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(profileData);

        return apiResponse;
    }

    public ApiResponse makeUserDisableAndEnable(Long id) {
        ApiResponse apiResponse = new ApiResponse();

        User user = userRepo.findById(id).orElse(null);
        ProfileData profileData = new ProfileData();

        user.setEnable(!user.isEnabled());
        profileData.setMessage("User Status Changed");
        
        userRepo.save(user);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(profileData);

        return apiResponse;
    }

}