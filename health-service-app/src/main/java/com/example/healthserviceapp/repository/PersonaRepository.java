package com.example.healthserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.healthserviceapp.entity.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String>{
    
}
