package com.example.healthserviceapp.service;

import com.example.healthserviceapp.Exceptions.MiException;
import com.example.healthserviceapp.entity.Imagen;
import com.example.healthserviceapp.entity.ObraSocial;
import com.example.healthserviceapp.entity.Paciente;
import com.example.healthserviceapp.entity.Usuario;
import com.example.healthserviceapp.enums.Sexo;
import com.example.healthserviceapp.repository.PacienteRepository;
import com.example.healthserviceapp.repository.UsuarioRepository;
import java.util.Date;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PacienteService {

    @Autowired
    public PacienteRepository pacienteRep;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private UsuarioRepository usuarioRep;

    @Transactional
    public void guardarPaciente(String nombre, String apellido,
            Sexo sexo, Date fechaNacimiento, String domicilio, Integer dni,
            Usuario usuario, MultipartFile archivo, ObraSocial obraSocial) throws MiException {

        verificarNombre(nombre);
        verificarApellido(apellido);
        verificarDomicilio(domicilio);

        Paciente paciente = new Paciente();

        paciente.setId(usuario.getId());
        paciente.setEmail(usuario.getEmail());
        paciente.setPassword(usuario.getPassword());
        paciente.setRol(usuario.getRol());
        paciente.setActivo(usuario.getActivo());
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDni(dni);
        paciente.setDomicilio(domicilio);
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setSexo(sexo);
        paciente.setObraSocial(obraSocial);
        Imagen imagen = imagenService.guardar(archivo);
        paciente.setImagen(imagen);
        pacienteRep.save(paciente);

        borrarUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public Paciente listarUnPaciente(String id){
        return pacienteRep.getOne(id);
    }

    @Transactional
    public void borrarUsuario(Usuario usuario) {
        usuarioRep.delete(usuario);
    }

    @Transactional
    public void modifyPaciente(Paciente paciente, String nombre, String apellido,
            Sexo sexo, Date fechaNacimiento, String domicilio, Integer dni,
            MultipartFile archivo, ObraSocial obraSocial) throws MiException {

        Optional<Paciente> respuesta = pacienteRep.findById(paciente.getId());

        if (respuesta.isPresent()) {
            Paciente nuevo_paciente = respuesta.get();
            nuevo_paciente.setNombre(nombre);
            nuevo_paciente.setApellido(apellido);
            nuevo_paciente.setSexo(sexo);
            nuevo_paciente.setDomicilio(domicilio);
            nuevo_paciente.setFechaNacimiento(fechaNacimiento);
            nuevo_paciente.setDni(dni);
            nuevo_paciente.setObraSocial(obraSocial);
            String id_imagen = nuevo_paciente.getImagen().getId();
            Imagen imagen = imagenService.actualizar(id_imagen, archivo);
            nuevo_paciente.setImagen(imagen);
            pacienteRep.save(nuevo_paciente);
        }
    }

    public void verificarNombre(String nombre) throws MiException {
        if (nombre == null || nombre.isEmpty() || nombre.trim().isEmpty()) {
            throw new MiException("Error, el nombre no puede estar vacio");
        }

    }

    public void verificarApellido(String apellido) throws MiException {
        if (apellido == null || apellido.isEmpty() || apellido.trim().isEmpty()) {
            throw new MiException("Error, el apellido no puede estar vacio");
        }
    }

    public void verificarDomicilio(String domicilio) throws MiException {
        if (domicilio == null || domicilio.isEmpty() || domicilio.trim().isEmpty()) {
            throw new MiException("Error, el domicilio no puede estar vacio");
        }
    }

    public Paciente getOne(String id) {
        return pacienteRep.getById(id);
    }

    @Transactional
    public void crearPaciente(Paciente paciente){
        pacienteRep.save(paciente);
    }

    @Transactional(readOnly = true)
    public Integer contarPacientes(){
        return pacienteRep.contarPacientes();
    }
}
