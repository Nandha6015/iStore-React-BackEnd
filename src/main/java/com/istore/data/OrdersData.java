package com.istore.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.istore.dto.OrderResponseDTO;
import com.istore.dto.TrackerDTO;

import lombok.Data;

@Data
public class OrdersData {
    @JsonInclude(value = Include.NON_NULL)
    private OrderResponseDTO orders;
    @JsonInclude(value = Include.NON_NULL)
    private TrackerDTO tracker;
    @JsonInclude(value = Include.NON_NULL)
    private String message;
}
