package com.istore.validator;

import java.util.ArrayList;
import java.util.List;

import com.istore.common.Error;
import com.istore.dto.LoginRequestDTO;
import com.istore.dto.SignUpRequestDTO;
import com.istore.entity.User;
import com.istore.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UserValidator {

    @Autowired
    private UserRepo userRepo;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    public List<Error> validateSignUpData(SignUpRequestDTO newUserData) {

        List<Error> errors = new ArrayList<>();

        if (userRepo.findByEmail(newUserData.getEmail()) != null) {
            errors.add(new Error("Email", "Email already exists"));
        }

        return errors;
    }

    public List<Error> validateLoginData(LoginRequestDTO userData) {
        List<Error> errors = new ArrayList<>();

        User user = userRepo.findByEmail(userData.getEmail());

        if (user == null) {
            errors.add(new Error("Email", "Email not found"));
        } else {
            if (!user.isEnabled()) {
                errors.add(new Error("Account", "Your Account is not active"));
            }
            if (!user.getPassword().equals(userData.getPassword())) {
                errors.add(new Error("Password", "Password Not Matched"));
            }
        }

        return errors;
    }

    public String getUserType(String email) {

        if (email.contains("@istore.com")) {
            return "ADMIN";
        }
        return "USER";
    }

    // public String getEncodedPassword(String password) {
    // return passwordEncoder.encode(password);
    // }

    public List<Error> validateUser(Long id) {
        List<Error> errors = new ArrayList<>();
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            errors.add(new Error("id", "Id not found"));
        }
        return errors;
    }

}
