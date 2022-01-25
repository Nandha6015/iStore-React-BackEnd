package com.istore.repository;

import java.util.List;

import com.istore.entity.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {

    List<Cart> findAllByUserId(Long id);

}
