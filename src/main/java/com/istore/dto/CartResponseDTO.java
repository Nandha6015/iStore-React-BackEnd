package com.istore.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class CartResponseDTO {

    @JsonInclude(value = Include.NON_NULL)
    private List<ProductDTO> products;
    @JsonInclude(value = Include.NON_NULL)
    private int count;

}
