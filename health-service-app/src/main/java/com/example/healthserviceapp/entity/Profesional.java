package com.example.healthserviceapp.entity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import com.example.healthserviceapp.enums.Especialidad;
import com.example.healthserviceapp.enums.Provincias;
import javax.persistence.EnumType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Profesional extends Persona{
    
    private String matricula;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    @OneToOne
    private Disponibilidad disponibilidad;
    
    @Enumerated(EnumType.STRING)
    private Provincias provincia;
    
    private Double calificacion;

    private Double precioConsulta;
}
