package com.istore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.istore.entity.Images;


public interface ImagesRepo extends JpaRepository<Images,Long>{
    
}
