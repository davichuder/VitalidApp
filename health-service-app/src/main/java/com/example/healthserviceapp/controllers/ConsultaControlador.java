
package com.example.healthserviceapp.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.example.healthserviceapp.entity.Consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.healthserviceapp.entity.Paciente;
import com.example.healthserviceapp.entity.Profesional;
import com.example.healthserviceapp.service.ConsultaService;
import com.example.healthserviceapp.service.PacienteService;
import com.example.healthserviceapp.service.ProfesionalService;
import com.example.healthserviceapp.utility.Dias;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/consulta")
public class ConsultaControlador {
    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private ConsultaService consultaService;
    
    @Autowired
    private PacienteService pacienteService;

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL')")
    @GetMapping("/paciente")
    public String pacientes(HttpSession session, ModelMap modelo) {
        Profesional profesional = (Profesional) session.getAttribute("usuariosession");
        modelo.addAttribute("pacientes", consultaService.listarPacientes(profesional.getId()));
        return "pacientes_paso1.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL')")
    @GetMapping("/consultas")    
    public String listarConsultasPaciente(ModelMap modelo, @RequestParam String idPaciente){
        modelo.addAttribute("consulta", consultaService.listarHistorial(idPaciente));
        modelo.addAttribute("paciente", pacienteService.listarUnPaciente(idPaciente));
        return "pacientes_paso2.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROFESIONAL')")
    @GetMapping("/diagnostico")
    public String darDiagnostico(ModelMap modelo, @RequestParam String id, String diagnostico, @RequestParam String idPaciente) {
        System.out.println("diagnostico: " + diagnostico + " id consulta: " + id + " id paciente: " + idPaciente);
        consultaService.ingresarDiagnostico(id, diagnostico);
        modelo.addAttribute("consulta", consultaService.listarHistorial(idPaciente));
        modelo.addAttribute("paciente", pacienteService.listarUnPaciente(idPaciente));
        return "pacientes_paso2.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PACIENTE')")
    @GetMapping("/provincia")
    public String provincias(HttpSession session, ModelMap modelo) {
        modelo.put("provincias", profesionalService.listarProvincias());
        return "consulta_paso1.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PACIENTE')")
    @GetMapping("/especialidad")
    public String especialidades(@RequestParam String provincia, HttpSession session, ModelMap modelo) {
        modelo.put("provincia", provincia);
        modelo.put("especialidades", profesionalService.listarEspecialidadesPorProvincia(provincia));
        return "consulta_paso2.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PACIENTE')")
    @GetMapping("/profesional")
    public String profesionales(@RequestParam String provincia, @RequestParam String especialidad, HttpSession session,
            ModelMap modelo) {
        modelo.put("profesionales",
                profesionalService.listarProfesionalPorEspecialidadesPorProvincia(provincia, especialidad));
        return "consulta_paso3.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PACIENTE')")
    @GetMapping("/disponibilidad")
    public String disponibilidadProfesional(@RequestParam String idProfesional, HttpSession session, ModelMap modelo) {
        Profesional profesional = profesionalService.getOne(idProfesional);
        modelo.put("profesional", profesional);

        ArrayList<String> listaA = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        Dias dias = new Dias(profesional.getDisponibilidad().getDias());
        for (LocalDate date = today; date.isBefore(today.plusDays(91)); date = date.plusDays(1)) {
            if (dias.comprobar(date.getDayOfWeek())) {
                listaA.add(date.format(formatter));
            }
        }
        Long nTurnos = profesional.getDisponibilidad().totalDeTurnos();
        List<String> listaB = consultaService.listarConsultasPorProfesionalAgrupadoPorFecha(profesional, nTurnos);
        for (String item : listaB) {
            listaA.remove(item);
        }
        modelo.put("fechas", listaA);
        return "consulta_paso4.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PACIENTE')")
    @GetMapping("/horario")
    public String horarioProfesional(@RequestParam String idProfesional, @RequestParam String fecha,
            HttpSession session, ModelMap modelo) {
        Profesional profesional = profesionalService.getOne(idProfesional);
        modelo.put("profesional", profesional);
        modelo.put("fecha", fecha);
        ArrayList<Integer> listaA = new ArrayList<>();
        Integer entrada = profesional.getDisponibilidad().getEntrada();
        Integer inicioDescanso = profesional.getDisponibilidad().getInicioDescanso();
        for (int i = entrada; i < inicioDescanso; i++) {
            listaA.add(i);
        }
        Integer finDescanso = profesional.getDisponibilidad().getFinDescanso();
        Integer salida = profesional.getDisponibilidad().getSalida();
        for (int i = finDescanso; i < salida; i++) {
            listaA.add(i);
        }
        List<Integer> listaB = consultaService.listarHorarioPorProfesionalPorFecha(profesional, fecha);
        for (Integer item : listaB) {
            listaA.remove(item);
        }
        modelo.put("horarios", listaA);
        return "consulta_paso5.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PACIENTE')")
    @GetMapping("/reservar")
    public String reservar(@RequestParam String idProfesional, @RequestParam String fecha,
            @RequestParam Integer horario, HttpSession session, ModelMap modelo) {
        Paciente paciente = (Paciente) session.getAttribute("usuariosession");
        consultaService.guardarConsulta(idProfesional, paciente.getId(), fecha, horario);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ROLE_PACIENTE')")
    @GetMapping("/eliminar/{id}")
    public String eliminarConsulta(@PathVariable String id, Model modelo, HttpSession session) {
        consultaService.eliminar(id);
        Paciente paciente = (Paciente) session.getAttribute("usuariosession");

        List<Consulta> consulta = consultaService.listarConsulta(paciente.getId());

        modelo.addAttribute("consulta", consulta);
        return "consulta_paciente.html";
    }
    
    @PostMapping("/calificar/{id}")
    public String guardarCalificacion(@PathVariable String id, @RequestParam int calificacion, ModelMap modelo){
        consultaService.guardarCalificacion(id, calificacion);
        Consulta consulta = consultaService.buscarConsulta(id);
        String idProfesional = consulta.getProfesional().getId();
        Double promedio = consultaService.promedioCalificacionPorProfesional(idProfesional);
        profesionalService.guardarCalificacion(idProfesional, promedio);
        return "redirect:/paciente/consulta";
    }
}
