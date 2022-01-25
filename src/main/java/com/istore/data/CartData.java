package com.istore.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.istore.dto.CartResponseDTO;

import lombok.Data;

@Data
public class CartData {

    @JsonInclude(value = Include.NON_NULL)
    private CartResponseDTO cart;
    @JsonInclude(value = Include.NON_NULL)
    private String message;

}
