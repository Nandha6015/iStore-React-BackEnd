package com.istore.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.istore.dto.ProductsResponseDTO;
import com.istore.entity.Product;

import lombok.Data;

@Data
public class ProductData {

    @JsonInclude(value = Include.NON_NULL)
    private List<ProductsResponseDTO> products;
    @JsonInclude(value = Include.NON_NULL)
    private List<Product> productsList;
    @JsonInclude(value = Include.NON_NULL)
    private ProductsResponseDTO product;
    @JsonInclude(value = Include.NON_NULL)
    private String message;

}
