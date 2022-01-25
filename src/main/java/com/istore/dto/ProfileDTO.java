package com.istore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class ProfileDTO {

    @JsonInclude(value = Include.NON_NULL)
    private Long id;
    private String name;
    private String email;
    @JsonInclude(value = Include.NON_NULL)
    private String password;
    @JsonInclude(value = Include.NON_NULL)
    private String phoneNumber;
    @JsonInclude(value = Include.NON_NULL)
    private String address;
    @JsonInclude(value = Include.NON_NULL)
    private Boolean isEnable;

}