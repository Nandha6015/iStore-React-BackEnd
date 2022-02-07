package com.istore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class TrackerDTO {

    private String tracker;
    @JsonInclude(value = Include.NON_NULL)
    private Boolean isCancel;
    
}
