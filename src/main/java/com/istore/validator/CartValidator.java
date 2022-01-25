package com.istore.validator;

import java.util.ArrayList;
import java.util.List;

import com.istore.common.Error;
import com.istore.repository.ProductRepo;
import com.istore.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class CartValidator {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    public List<Error> isUserValid(Long id) {
        List<Error> errors = new ArrayList<>();

        if (!userRepo.existsById(id)) {
            errors.add(new Error("Id", "User Id not exists"));
        }

        return errors;
    }

    public List<Error> validateAddToCart(Long id, Long pid) {
        List<Error> errors = new ArrayList<>();

        if (!userRepo.existsById(id)) {
            errors.add(new Error("Id", "Id not exists"));
        }

        if (!productRepo.existsById(pid)) {
            errors.add(new Error("Product Id", "Product Id not exists"));
        }

        return errors;
    }

}
