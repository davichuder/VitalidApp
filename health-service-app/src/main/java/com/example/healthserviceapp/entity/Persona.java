package com.example.healthserviceapp.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.example.healthserviceapp.enums.Sexo;
import javax.persistence.EnumType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Persona extends Usuario{
    
    protected Integer dni;

    protected String nombre;
    protected String apellido;

    @Enumerated(EnumType.STRING)
    protected Sexo sexo;
    
    @Temporal(TemporalType.DATE)
    protected Date fechaNacimiento;

    @OneToOne
    protected Imagen imagen;

    private String domicilio;
}
