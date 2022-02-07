package com.istore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    @JsonInclude(value = Include.NON_NULL)
    private Double price;
    @JsonInclude(value = Include.NON_NULL)
    private int nos;
    private String img;
    @JsonInclude(value = Include.NON_NULL)
    private String pname;
    @JsonInclude(value = Include.NON_NULL)
    private String tracker;
    @JsonInclude(value = Include.NON_NULL)
    private String dname;
}
