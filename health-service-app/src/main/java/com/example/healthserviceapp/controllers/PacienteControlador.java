package com.example.healthserviceapp.controllers;

import com.example.healthserviceapp.entity.Consulta;
import com.example.healthserviceapp.entity.ObraSocial;
import com.example.healthserviceapp.entity.Paciente;
import com.example.healthserviceapp.entity.Usuario;
import com.example.healthserviceapp.enums.Sexo;
import com.example.healthserviceapp.service.ConsultaService;
import com.example.healthserviceapp.service.ObraSocialService;
import com.example.healthserviceapp.service.PacienteService;
import com.example.healthserviceapp.service.UsuarioService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/paciente")
public class PacienteControlador {

    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ConsultaService consultaService;
    @Autowired
    private ObraSocialService obraSocialService;

    @PostMapping("/registro")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PACIENTE', 'ROLE_PROFESIONAL')")
    public String crearPaciente(@RequestParam String nombre, @RequestParam String apellido, MultipartFile imagen,
            @RequestParam String domicilio, @RequestParam Integer dni, @RequestParam Sexo sexo,
            @RequestParam String fechaNacimiento, @RequestParam String obraSocialNombre, HttpSession session) throws Exception {

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date dataFormateada = formato.parse(fechaNacimiento);

        ObraSocial obraSocial = obraSocialService.buscarPorNombre(obraSocialNombre);

        if (session.getAttribute("usuariosession") instanceof Paciente) {
            Paciente paciente = (Paciente) session.getAttribute("usuariosession");
            pacienteService.modifyPaciente(paciente, nombre, apellido, sexo, dataFormateada, domicilio, dni, imagen, obraSocial);
            usuarioService.loadUserByUsername(paciente.getEmail());

        } else if (session.getAttribute("usuariosession") instanceof Usuario) {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            pacienteService.guardarPaciente(nombre, apellido, sexo, dataFormateada, domicilio, dni, usuario, imagen, obraSocial);
            usuarioService.loadUserByUsername(usuario.getEmail());
        }
        return "redirect:/";
    }

    @GetMapping("/consulta")
    @PreAuthorize("hasRole('ROLE_PACIENTE')")
    public String mostrarConsulta(Model modelo, HttpSession session) {

            Paciente paciente = (Paciente) session.getAttribute("usuariosession");
            
            List<Consulta> consulta = consultaService.listarConsulta(paciente.getId());
          
            modelo.addAttribute("consulta", consulta);
        
        return "consulta_paciente.html";
    }
}
