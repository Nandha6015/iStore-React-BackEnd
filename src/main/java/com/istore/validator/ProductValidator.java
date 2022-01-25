package com.istore.validator;

import java.util.ArrayList;
import java.util.List;

import com.istore.common.Error;
import com.istore.repository.ProductRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ProductValidator {

    @Autowired
    private ProductRepo productRepo;

    public List<Error> isProductValid(Long id) {
        List<Error> errors = new ArrayList<>();

        if (productRepo.findById(id).orElse(null) == null) {
            errors.add(new Error("Id", "Product does not exists"));
        }

        return errors;

    }

}
