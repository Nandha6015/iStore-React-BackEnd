package com.istore.dto;

import com.istore.entity.Address;

import lombok.Data;

@Data
public class DeliveryDTO {
    
    private Long id;
    private String pname;
    private String uname;
    private Double amount;
    private String payment;
    private Address address;
    private String tracker;

}
