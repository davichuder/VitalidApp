package com.example.healthserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.healthserviceapp.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String>{
    @Query("SELECT COUNT(p) FROM Paciente p")
    public Integer contarPacientes();
}
