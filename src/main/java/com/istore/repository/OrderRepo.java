package com.istore.repository;

import java.util.List;

import com.istore.entity.Orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Orders, Long> {

    List<Orders> findAllByUserId(Long id);

}
