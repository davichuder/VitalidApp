package com.example.healthserviceapp.service;

import com.example.healthserviceapp.Exceptions.MiException;
import com.example.healthserviceapp.entity.Disponibilidad;
import com.example.healthserviceapp.entity.Imagen;
import com.example.healthserviceapp.entity.Profesional;
import com.example.healthserviceapp.entity.Usuario;
import com.example.healthserviceapp.enums.Especialidad;
import com.example.healthserviceapp.enums.Provincias;
import com.example.healthserviceapp.enums.Sexo;
import com.example.healthserviceapp.repository.ProfesionalRepository;
import static java.lang.Boolean.FALSE;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfesionalService {

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Transactional
    public void guardarProfesional(String nombre, String apellido,
            Sexo sexo, Date fechaNacimiento, String domicilio, Integer dni,
            MultipartFile archivo, Provincias provincia, String matricula,
            Especialidad especialidad, Disponibilidad disponibilidad, Usuario usuario, Double precio) throws MiException {

        verificarProfesional(nombre, apellido, domicilio, matricula);

        Profesional profesional = new Profesional();

        profesional.setId(usuario.getId());
        profesional.setEmail(usuario.getEmail());
        profesional.setPassword(usuario.getPassword());
        profesional.setRol(usuario.getRol());
        profesional.setActivo(usuario.getActivo());
        profesional.setNombre(nombre);
        profesional.setApellido(apellido);
        profesional.setDni(dni);
        profesional.setDomicilio(domicilio);
        profesional.setFechaNacimiento(fechaNacimiento);
        profesional.setMatricula(matricula);
        profesional.setSexo(sexo);
        Imagen imagen = imagenService.guardar(archivo);
        profesional.setImagen(imagen);
        profesional.setProvincia(provincia);
        profesional.setEspecialidad(especialidad);
        profesional.setDisponibilidad(disponibilidadService.guardar(disponibilidad));
        profesional.setPrecioConsulta(precio);
        profesional.setCalificacion(0d);
        profesionalRepository.save(profesional);

        usuarioService.eliminarUsuario(usuario.getId());
    }

    @Transactional
    public void borrarProfesional(Profesional profesional) {

        profesionalRepository.delete(profesional);

    }

    @Transactional(readOnly = true)
    public List<Profesional> listarProfesionales() {

        List<Profesional> profesionales = new ArrayList();

        profesionales = profesionalRepository.findAll();

        return profesionales;

    }

    @Transactional(readOnly = true)
    public List<Profesional> listarPorNombre(String nombre) {
        List<Profesional> profesionales = new ArrayList();

        profesionales = profesionalRepository.buscarPorNombre(nombre);

        return profesionales;
    }

    public List<Profesional> listarPorEspecialidad(Especialidad especialidad) {
        List<Profesional> profesionales = new ArrayList();
        profesionales = profesionalRepository.buscarPorEspecialidad(especialidad);
        return profesionales;
    }

    public List<Profesional> listarPorLocalidad(Provincias provincia) {
        List<Profesional> profesionales = new ArrayList();
        profesionales = profesionalRepository.buscarPorLocalidad(provincia);
        return profesionales;
    }

    @Transactional
    public void modificarProfesional(Profesional profesional, String nombre, String apellido,
            Sexo sexo, Date fechaNacimiento, String domicilio, Integer dni, MultipartFile archivo,
            Provincias provincia, String matricula, Especialidad especialidad, Disponibilidad disponibilidad, Double precio)
            throws MiException {

        Optional<Profesional> respuesta = profesionalRepository.findById(profesional.getId());

        if (respuesta.isPresent()) {
            Profesional nuevo_profesional = respuesta.get();

            nuevo_profesional.setNombre(nombre);
            nuevo_profesional.setApellido(apellido);
            nuevo_profesional.setSexo(sexo);
            nuevo_profesional.setDomicilio(domicilio);
            nuevo_profesional.setFechaNacimiento(fechaNacimiento);
            nuevo_profesional.setDni(dni);
            nuevo_profesional.setDisponibilidad(disponibilidad);
            nuevo_profesional.setEspecialidad(especialidad);
            nuevo_profesional.setMatricula(matricula);
            nuevo_profesional.setProvincia(provincia);
            nuevo_profesional.setPrecioConsulta(precio);
            String id_imagen = nuevo_profesional.getImagen().getId();
            Imagen imagen = imagenService.actualizar(id_imagen, archivo);
            nuevo_profesional.setImagen(imagen);
            String idDisponibilidad = profesional.getDisponibilidad().getId();
            nuevo_profesional.setDisponibilidad(disponibilidadService.modificar(idDisponibilidad, disponibilidad));
            profesionalRepository.save(nuevo_profesional);
        }
    }

    @Transactional
    public void eliminarProfesional(String id) {

        Optional<Profesional> presente = profesionalRepository.findById(id);
        if (presente.isPresent()) {

            Profesional profesional = presente.get();
            profesional.setActivo(FALSE);
            profesionalRepository.save(profesional);

        }

    }

    public void verificarProfesional(String nombre, String apellido, String domicilio, String matricula)
            throws MiException {

        if (nombre == null || nombre.isEmpty() || nombre.trim().isEmpty()) {

            throw new MiException("Error, el nombre no puede estar vacio");

        }
        if (apellido == null || apellido.isEmpty() || apellido.trim().isEmpty()) {

            throw new MiException("Error, el apellido no puede estar vacio");

        }
        if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {

            throw new MiException("Error, el domicilio no puede estar vacio");

        }
        if (matricula == null || matricula.isEmpty() || matricula.trim().isEmpty()) {

            throw new MiException("Error, la matrícula no puede estar vacía o ser un valor nulo");

        }

    }

@Transactional(readOnly = true)
public Profesional buscarProfesional(String id){
    Profesional profesional = profesionalRepository.buscarPorId(id);
    return profesional;
}

    @Transactional(readOnly = true)
    public List<String> listarProvincias() {
        List<String> provincias = new ArrayList();
        provincias = profesionalRepository.listarProvincias();
        return provincias;
    }

    @Transactional(readOnly = true)
    public List<String> listarEspecialidadesPorProvincia(String provincia) {
        List<String> especialidades = new ArrayList();
        Provincias prov = Provincias.valueOf(provincia);
        especialidades = profesionalRepository.listarEspecialidadesPorProvincia(prov);
        return especialidades;
    }

    @Transactional(readOnly = true)
    public List<Profesional> listarProfesionalPorEspecialidadesPorProvincia(String provincia, String especialidad) {
        List<Profesional> profesionales = new ArrayList();
        Provincias prov = Provincias.valueOf(provincia);
        Especialidad esp = Especialidad.valueOf(especialidad);
        profesionales = profesionalRepository.listarProfesionalPorEspecialidadesPorProvincia(prov, esp);
        return profesionales;
    }

    public Profesional getOne(String id) {
        return profesionalRepository.getById(id);
    }

    @Transactional
    public void crearProfesional(Profesional profesional){
        profesionalRepository.save(profesional);
    }

    @Transactional(readOnly = true)
    public Integer contarProfesionales(){
        return profesionalRepository.contarProfesionales();
    }

    @Transactional
    public void guardarCalificacion(String id, Double promedio) {
        Optional<Profesional> respuesta = profesionalRepository.findById(id);

        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();
            profesional.setCalificacion(promedio);
            profesionalRepository.save(profesional);
        }
    }
}
