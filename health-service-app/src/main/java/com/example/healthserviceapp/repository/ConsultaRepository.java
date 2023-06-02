package com.example.healthserviceapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.example.healthserviceapp.entity.Consulta;
import com.example.healthserviceapp.entity.Paciente;
import com.example.healthserviceapp.entity.Profesional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, String> {
    @Query("SELECT c.fecha FROM Consulta c WHERE c.profesional = :profesional GROUP BY c.fecha HAVING COUNT(*) = :nTurnos")
    public List<String> listarConsultasPorProfesionalAgrupadoPorFecha(@Param("profesional") Profesional profesional, @Param("nTurnos") Long nTurnos);

    @Query("SELECT c.horario FROM Consulta c WHERE (c.profesional = :profesional AND c.fecha = :fechaConsulta)")
    public List<Integer> listarHorarioPorProfesionalPorFecha(@Param("profesional") Profesional profesional, @Param("fechaConsulta") String fecha);

    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :id ORDER BY c.fecha ASC")
    public List<Consulta> listarConsulta(@Param("id") String id);    

    @Query("SELECT DISTINCT(c.paciente) FROM Consulta c WHERE c.profesional.id = :id")
    public List<Paciente> listarPacientesPorProfesional(@Param("id") String id);
    
    @Query("SELECT AVG(c.calificacion) FROM Consulta c WHERE (c.calificacion != NULL and c.profesional.id = :id)")
    public Double promedioCalificacionPorProfesional(@Param("id") String id);
}
