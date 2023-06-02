package com.example.healthserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.healthserviceapp.entity.Profesional;
import com.example.healthserviceapp.enums.Especialidad;
import com.example.healthserviceapp.enums.Provincias;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, String> {

    @Query("SELECT p FROM Profesional p")
    public List<Profesional> listarProfesionales();

    @Query("SELECT p FROM Profesional p WHERE p.especialidad = :especialidad")
    public List<Profesional> buscarPorEspecialidad(@Param("especialidad") Especialidad especialidad);

    @Query("SELECT p FROM Profesional p WHERE p.provincia = :provincia")
    public List<Profesional> buscarPorLocalidad(@Param("provincia") Provincias provincia);

    @Query("SELECT DISTINCT(p.provincia) FROM Profesional p")
    public List<String> listarProvincias();

    @Query("SELECT DISTINCT(p.especialidad) FROM Profesional p WHERE p.provincia = :provincia")
    public List<String> listarEspecialidadesPorProvincia(@Param("provincia") Provincias provincia);

    @Query("SELECT DISTINCT(p) FROM Profesional p WHERE (p.provincia = :provincia AND p.especialidad = :especialidad AND p.disponibilidad.dias IS NOT NULL)")
    public List<Profesional> listarProfesionalPorEspecialidadesPorProvincia(@Param("provincia") Provincias provincia,
            @Param("especialidad") Especialidad especialidad);

    @Query("SELECT p FROM Profesional p WHERE p.nombre LIKE %:nombre%")
    public List<Profesional> buscarPorNombre(@Param("nombre") String nombre);   
    
    @Query("SELECT p FROM Profesional p WHERE p.id = :id")
    public Profesional buscarPorId(@Param("id") String id);

    @Query("SELECT COUNT(p) FROM Profesional p")
    public Integer contarProfesionales();
}
