package com.istore.repository;

import com.istore.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User, Long> {
    @Query(nativeQuery = true, value = "select * from user where email=?1")
    User findByEmail(String email);

    @Query(nativeQuery = true, value = "select * from user where name=?1")
    User findByName(String name);

}