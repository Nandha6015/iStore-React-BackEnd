package com.istore.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderedAt;
    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private UserProductDetails userProductDetails;
    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;
    @OneToOne
    @JoinColumn(referencedColumnName = "id",nullable = true)
    private Address address;
    private String tracker;
    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private User delivery;
    private String paymentType="COD";
    private Boolean isCancel=false;
}
