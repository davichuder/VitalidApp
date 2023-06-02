package com.example.healthserviceapp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
public class Disponibilidad {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private Integer entrada;

    private Integer salida;

    private Integer inicioDescanso;

    private Integer finDescanso;

    private String[] dias;

    public Long totalDeTurnos(){
        return new Long((salida - entrada) - (finDescanso - inicioDescanso));
    }
}
