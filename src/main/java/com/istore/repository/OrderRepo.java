package com.istore.repository;

import java.util.List;

import com.istore.entity.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepo extends JpaRepository<Orders, Long> {

    @Query(nativeQuery = true,value = "select * from orders where user_id = ?1 order by id desc")
    List<Orders> findAllByUserIdOrderByIdDesc(Long id);

    @Query(nativeQuery = true,value = "select * from orders order by id desc")
    List<Orders> findAllOrderByIdDesc();

}
