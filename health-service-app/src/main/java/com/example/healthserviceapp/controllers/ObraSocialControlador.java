package com.example.healthserviceapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.healthserviceapp.entity.ObraSocial;
import com.example.healthserviceapp.service.ObraSocialService;

@Controller
@RequestMapping("/obras_sociales")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class ObraSocialControlador {
    @Autowired
    private ObraSocialService obraSocialService;

    @GetMapping("")
    public String listado(ModelMap model){
        List<ObraSocial> obraSociales = obraSocialService.listarObrasSociales();
        model.addAttribute("obraSociales", obraSociales);
        return "obras_sociales.html";
    }

    @GetMapping("/buscar")
    public String buscarPorEmail(String nombre, ModelMap model) {
        ObraSocial obraSocial = obraSocialService.buscarPorNombre(nombre);
        model.addAttribute("obraSociales", obraSocial); 
        return "obras_sociales.html";
    }

    @GetMapping("/eliminar/{id}")
    public String bajaUsuario(@PathVariable String id, ModelMap modelo) {
        obraSocialService.eliminarObraSocial(id);
        return "redirect:/obras_sociales";
    }

    @GetMapping("/modificarPrecio")
    public String modificar(@RequestParam String id, @RequestParam Double precio){
        obraSocialService.modificarPrecio(id, precio);
        return "redirect:/obras_sociales";
    }

    @GetMapping("/modificarNombre")
    public String modificar(@RequestParam String id, @RequestParam String nombre){
        obraSocialService.modificarNombre(id, nombre);
        return "redirect:/obras_sociales";
    }

    @GetMapping("/crear")
    public String crear(@RequestParam String nombre, @RequestParam Double precio){
        obraSocialService.crearObraSocial(nombre, precio);
        return "redirect:/obras_sociales";
    }
}
