package com.example.healthserviceapp.controllers;

import com.example.healthserviceapp.Exceptions.MiException;
import com.example.healthserviceapp.entity.Paciente;
import com.example.healthserviceapp.entity.Profesional;
import com.example.healthserviceapp.enums.Especialidad;
import com.example.healthserviceapp.service.ProfesionalService;
import com.example.healthserviceapp.service.UsuarioService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProfesionalService profesionalService;

    @GetMapping("/login")
    public String ingresar(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "email y contraseña no coinciden");
        }
        return "index.html";
    }

    @GetMapping("/especialidades")
    public String listaEspecialidades(ModelMap modelo) throws MiException {

        List<Profesional> profesionales = profesionalService.listarProfesionales();
        modelo.addAttribute("profesionales", profesionales);

        //modelo.put("exito", "La lista de profesionales se muestra a continuación");
        modelo.put("especialidades", Especialidad.values());

        return "especialidades.html";

    }

@GetMapping("/especialidad/busqueda")
public String listarProfesionalPorNombre(String nombre, ModelMap modelo){

    List<Profesional> profesional = profesionalService.listarPorNombre(nombre);
    modelo.addAttribute("profesionales", profesional);
    modelo.put("especialidades", Especialidad.values());

    return "especialidades.html";
}

@GetMapping("/especialidad/profesionales/{especialidad}")
public String listarPorEspecialidad(@PathVariable Especialidad especialidad, ModelMap modelo){
    
    List<Profesional> profesional = profesionalService.listarPorEspecialidad(especialidad);
    modelo.addAttribute("profesionales", profesional);
    modelo.put("especialidades", Especialidad.values());

    return "especialidades.html";
}

    @GetMapping("/")
    public String inicio(HttpSession session, ModelMap modelo) {

        String tipo = "";

        if (session.getAttribute("usuariosession") instanceof Profesional) {
            Profesional profesional = (Profesional) session.getAttribute("usuariosession");
            modelo.put("usuario", profesional);
            tipo = "Profesional";
        }
        if (session.getAttribute("usuariosession") instanceof Paciente) {
            Paciente paciente = (Paciente) session.getAttribute("usuariosession");
            modelo.put("usuario", paciente);
            tipo = "Paciente";
        }

        modelo.put("tipo", tipo);
        return "index.html";
    }

    @PostMapping("/registro")
    public String registroUsuario(@RequestParam String email, @RequestParam String password,
            String password2, ModelMap modelo) throws MiException {

        try {

            usuarioService.guardarUsuario(email, password, password2);

            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (MiException e) {
            System.out.println(e.getMessage());
            modelo.put("error", e.getMessage());
            modelo.put("email", email);
            return "index.html";
        }

    }
}
