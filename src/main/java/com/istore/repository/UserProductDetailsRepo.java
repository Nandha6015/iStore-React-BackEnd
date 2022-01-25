package com.istore.repository;

import java.util.List;

import com.istore.entity.UserProductDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProductDetailsRepo extends JpaRepository<UserProductDetails, Long> {

    @Query(nativeQuery = true, value = "select id from user_product_details where product_id = ?1")
    List<Long> findAllByProductId(Long pid);

}
