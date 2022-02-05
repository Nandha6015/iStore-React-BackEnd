package com.istore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.istore.entity.Images;

import lombok.Data;

@Data
public class ProductsResponseDTO {

    private Long id;
    private String name;
    private Double price;
    @JsonInclude(value = Include.NON_NULL)
    private String description;
    private String keyFeature1;
    private String keyFeature2;
    private String keyFeature3;
    private String imgSrc;
    @JsonInclude(value = Include.NON_NULL)
    private String category;
    @JsonInclude(value = Include.NON_NULL)
    private String stockStatus;
    @JsonInclude(value = Include.NON_NULL)
    private int quantityInStock;
    @JsonInclude(value = Include.NON_NULL)
    private Images images;

}
