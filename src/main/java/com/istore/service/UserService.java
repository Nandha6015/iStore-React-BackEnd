package com.istore.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.istore.common.ApiResponse;
import com.istore.common.BadRequestException;
import com.istore.common.Error;
import com.istore.data.AddressData;
import com.istore.data.LoginData;
import com.istore.data.ProfileData;
import com.istore.dto.AddressDTO;
import com.istore.dto.LoginRequestDTO;
import com.istore.dto.LoginResponseDTO;
import com.istore.dto.ProfileDTO;
import com.istore.dto.SignUpRequestDTO;
import com.istore.entity.Address;
import com.istore.entity.User;
import com.istore.jwt.JwtUtil;
import com.istore.repository.AddressRepo;
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
    private AddressRepo addressRepo;

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
            Address address = new Address();
            addressRepo.save(address);
            user.setAddress(address);
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
            profile.setImg(user.getProfileImg());
            profileData.setProfile(profile);
            apiResponse.setStatus(HttpStatus.OK.value());
            apiResponse.setData(profileData);
        } else {
            throw new BadRequestException("Bad Request", errors);
        }
        return apiResponse;
    }

    // To Update Profile
    public ApiResponse updateProfile(Long id, ProfileDTO profileDTO,String img) {
        ApiResponse apiResponse = new ApiResponse();
        ProfileData profileData = new ProfileData();
        User user = userRepo.findById(id).orElse(null);
        if(img!=null){
            user.setProfileImg(profileDTO.getImg());
        }
        else{
            user.setEmail(profileDTO.getEmail());
            user.setPassword(profileDTO.getPassword());
            user.setPhoneNumber(profileDTO.getPhoneNumber());
            user.setName(profileDTO.getName());
        }
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
            profileDTO.setImg(user.getProfileImg());
            profileDTOs.add(profileDTO);
        }

        ProfileData profileData = new ProfileData();
        profileData.setUsers(profileDTOs);

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(profileData);

        return apiResponse;
    }

    public ApiResponse makeUserDisableAndEnable(Long id,boolean enable) {
        ApiResponse apiResponse = new ApiResponse();

        User user = userRepo.findById(id).orElse(null);
        ProfileData profileData = new ProfileData();

        user.setEnable(enable);
        profileData.setMessage("User Status Changed");
        
        userRepo.save(user);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(profileData);

        return apiResponse;
    }

    public ApiResponse getAddress(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        AddressData addressData = new AddressData();

        
        User user = userRepo.findById(id).orElse(null);
        Address address = user.getAddress();
        
        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setName(address.getName());
        addressDTO.setPhoneNumber(address.getPhoneNumber());
        addressDTO.setAddress(address.getAddress());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setEmail(address.getEmail());
        addressDTO.setPincode(address.getPincode());

        addressData.setAddress(addressDTO);

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(addressData);
        return apiResponse;
    }

    public ApiResponse updateAddress(Long id, AddressDTO addressDto) {
        ApiResponse apiResponse = new ApiResponse();
        AddressData addressData = new AddressData();
        User user = userRepo.findById(id).orElse(null);
        Address address = user.getAddress();

        address.setName(addressDto.getName());
        address.setPhoneNumber(addressDto.getPhoneNumber());
        address.setAddress(addressDto.getAddress());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setEmail(addressDto.getEmail());
        address.setPincode(addressDto.getPincode());

        addressRepo.save(address);
        user.setAddress(address);
        userRepo.save(user);

        addressData.setMessage("Address Updated");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(addressData);

        return apiResponse;
    }

}
