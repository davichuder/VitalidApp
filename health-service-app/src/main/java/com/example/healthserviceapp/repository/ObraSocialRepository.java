package com.example.healthserviceapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.healthserviceapp.entity.ObraSocial;

@Repository
public interface ObraSocialRepository extends JpaRepository<ObraSocial, String>{
    @Query("SELECT DISTINCT(o.nombre) FROM ObraSocial o")
    public List<String> listarNombresObrasSociales();

    @Query("SELECT o FROM ObraSocial o WHERE o.nombre = :nombre")
    public Optional<ObraSocial> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT DISTINCT(o) FROM ObraSocial o")
    public List<ObraSocial> listarObrasSociales();
}
