 package com.example.healthserviceapp.controllers;

import com.example.healthserviceapp.Exceptions.MiException;
import com.example.healthserviceapp.service.UsuarioService;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioService usuarioServicio;

    @GetMapping("/login")
    public String ingresar(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "usuario o contraseña invalidos");
        }
        return "index.html";
    }

    @GetMapping("/")
    public String inicio(HttpSession session) {
        return "index.html";
    }

    @PostMapping("/registro")
    public String registroUsuario(@RequestParam String email, @RequestParam String password,
            String password2, ModelMap modelo) throws MiException{

        try {

            usuarioServicio.guardarUsuario(email, password, password2);

            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        }  catch (MiException e) {
            System.out.println(e.getMessage());
            modelo.put("error", e.getMessage());
            modelo.put("email", email);
            return "index.html";
        }

    }

}
