package com.example.healthserviceapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.healthserviceapp.entity.Disponibilidad;
import com.example.healthserviceapp.repository.DisponibilidadRepository;

@Service
public class DisponibilidadService {
    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    public Disponibilidad guardar(Disponibilidad disponibilidad){
        return disponibilidadRepository.save(disponibilidad);
    }

    public Disponibilidad modificar(String id, Disponibilidad disponibilidad){
        Optional<Disponibilidad> optional = disponibilidadRepository.findById(id);
        if (optional.isPresent()){
            Disponibilidad disponibilidad2 = optional.get();
            disponibilidad2.setDias(disponibilidad.getDias());;
            disponibilidad2.setEntrada(disponibilidad.getEntrada());
            disponibilidad2.setInicioDescanso(disponibilidad.getInicioDescanso());
            disponibilidad2.setFinDescanso(disponibilidad.getFinDescanso());
            disponibilidad2.setFinDescanso(disponibilidad.getFinDescanso());
            return disponibilidadRepository.save(disponibilidad2);
        }
        return null;
    }
}
