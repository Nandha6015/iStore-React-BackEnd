package com.istore.data;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.istore.dto.DeliveryDTO;

@Data
public class DeliveryData {
    
    @JsonInclude(value = Include.NON_NULL)
    private List<DeliveryDTO> deliveries;
    @JsonInclude(value = Include.NON_NULL)
    private String message;
    
}
