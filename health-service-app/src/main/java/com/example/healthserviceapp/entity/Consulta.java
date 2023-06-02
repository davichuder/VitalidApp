package com.example.healthserviceapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
public class Consulta {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @OneToOne
    private Profesional profesional;

    @OneToOne
    private Paciente paciente;

    @Column(name = "diagnostico", length = 6000)
    private String diagnostico;

    private String fecha;

    private Integer horario;

    private Double precio;

    private Integer calificacion;
}
