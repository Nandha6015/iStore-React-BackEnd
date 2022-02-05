package com.istore.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.istore.dto.AddressDTO;

import lombok.Data;

@Data
public class AddressData {
    @JsonInclude(value = Include.NON_NULL)
    private AddressDTO address;
    @JsonInclude(value = Include.NON_NULL)
    private String message;
    
}
