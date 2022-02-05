package com.istore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class AddressDTO {
    private String name;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String pincode;
    @JsonInclude(value = Include.NON_NULL)
    private String email;
}
