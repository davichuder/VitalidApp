package com.example.healthserviceapp.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public final class Paciente extends Persona{
    @OneToOne
    private ObraSocial obraSocial;
}
