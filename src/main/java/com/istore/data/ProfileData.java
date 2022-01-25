package com.istore.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.istore.dto.ProfileDTO;

import lombok.Data;

@Data
public class ProfileData {
    @JsonInclude(value = Include.NON_NULL)
    private ProfileDTO profile;
    @JsonInclude(value = Include.NON_NULL)
    private String message;
    @JsonInclude(value = Include.NON_NULL)
    private List<ProfileDTO> users;
}
