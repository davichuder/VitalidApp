package com.example.healthserviceapp.controllers;

import com.example.healthserviceapp.entity.Paciente;
import com.example.healthserviceapp.entity.Persona;
import com.example.healthserviceapp.entity.Profesional;
import com.example.healthserviceapp.entity.Usuario;
import com.example.healthserviceapp.enums.Sexo;
import com.example.healthserviceapp.service.ObraSocialService;
import com.example.healthserviceapp.service.ProfesionalService;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/perfil")
public class PersonaControlador {
    @Autowired
    private ObraSocialService obraSocialService;

    @Autowired
    private ProfesionalService profesionalService;
   
    @GetMapping("/datos")
    @PreAuthorize("hasAnyRole('ROLE_PACIENTE', 'ROLE_PROFESIONAL')")
    public String vistaDatosPerfil(HttpSession session, ModelMap modelo) {

        modelo.put("sexos", Sexo.values());
        modelo.put("obrasSociales", obraSocialService.listarNombresObrasSociales());
        String tipo = "";

        if (session.getAttribute("usuariosession") instanceof Usuario) {
            tipo = "Usuario";
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            modelo.put("usuario", usuario);
        }
        if (session.getAttribute("usuariosession") instanceof Paciente) {
            tipo = "Paciente";
            Paciente paciente = (Paciente) session.getAttribute("usuariosession");
            modelo.put("paciente", paciente);
        }
        modelo.put("tipo", tipo);
        return "form.html";
    }

    @GetMapping("/imagen")
    public ResponseEntity<byte[]> imagenUsuario(HttpSession session) {
        Persona persona = (Persona) session.getAttribute("usuariosession");
        byte[] imagen = persona.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    @GetMapping("/imagen/profesional/{id}")
    public ResponseEntity<byte[]> imagenProfesional(@PathVariable String id, HttpSession session) {
        Profesional profesional = profesionalService.buscarProfesional(id);
        byte[] imagen = profesional.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
}
