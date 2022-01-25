package com.istore.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderResponseDTO {

    private List<ProductDTO> products;

}
