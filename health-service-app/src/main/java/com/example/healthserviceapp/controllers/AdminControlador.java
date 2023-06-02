package com.example.healthserviceapp.controllers;

import com.example.healthserviceapp.entity.Usuario;
import com.example.healthserviceapp.enums.Rol;
import com.example.healthserviceapp.repository.UsuarioRepository;
import com.example.healthserviceapp.service.UsuarioService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioRepository usuarioRep;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String lista(ModelMap modelo) {

        List<Usuario> usuario = new ArrayList<>();
        usuario = usuarioRep.buscarUsuarios();

        modelo.put("roles", Rol.values());
        modelo.addAttribute("usuarios", usuario);
        return "panel.html";
    }

    @GetMapping("/baja/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String bajaUsuario(@PathVariable String id, ModelMap modelo) {
        usuarioService.darBaja(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/alta/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String altaUsuario(@PathVariable String id, ModelMap modelo) {
        usuarioService.darAlta(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String buscarPorEmail(String email, ModelMap model) {
        Usuario usuario = usuarioRep.buscarPorEmail(email);
        model.addAttribute("usuarios", usuario);
        model.put("roles", Rol.values());
        return "panel.html";
    }

    @GetMapping("/rol/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String cambiarRolUsuario(@PathVariable String id, Rol rol, ModelMap model) {
        usuarioService.modificarRol(rol, id);
        return "redirect:/admin/dashboard";
    }
}
