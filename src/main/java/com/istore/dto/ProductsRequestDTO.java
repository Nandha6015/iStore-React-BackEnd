package com.istore.dto;

import lombok.Data;

@Data
public class ProductsRequestDTO {

    private String name;
    private Double price;
    private String description;
    private String keyFeature1;
    private String keyFeature2;
    private String keyFeature3;
    private String imgSrc1;
    private String imgSrc2;
    private String imgSrc3;
    private String imgSrc4;
    private String imgSrc5;
    private int stock;
}
