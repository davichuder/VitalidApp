package com.example.healthserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.healthserviceapp.entity.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, String>{
    
}
