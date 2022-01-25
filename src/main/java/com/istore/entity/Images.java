package com.istore.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imgSrc1;
    private String imgSrc2;
    private String imgSrc3;
    private String imgSrc4;
    private String imgSrc5;

}
