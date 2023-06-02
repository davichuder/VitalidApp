package com.example.healthserviceapp.service;

import com.example.healthserviceapp.Exceptions.MiException;
import com.example.healthserviceapp.entity.Usuario;
import com.example.healthserviceapp.enums.Rol;
import static com.example.healthserviceapp.enums.Rol.PACIENTE;
import com.example.healthserviceapp.repository.UsuarioRepository;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    public UsuarioRepository usuarioRepository;

    @Transactional
    public void guardarUsuario(String email, String password, String password2) throws MiException {

        verificarEmail(email);
        verificarPassword(password, password2);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setActivo(TRUE);
        usuario.setRol(PACIENTE);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void modificarUsuario(String email, String password, String password2, String id) throws MiException {
        verificarPassword(password, password2);

        Optional<Usuario> presente = usuarioRepository.findById(id);

        if (presente.isPresent()) {
            Usuario usuario = presente.get();
            usuario.setPassword(password);
            usuarioRepository.save(usuario);
        }

    }

    @Transactional
    public void modificarRol(Rol rol, String id) {
        Usuario usuario = new Usuario();

        Optional<Usuario> presente = usuarioRepository.findById(id);

        if (presente.isPresent()) {
            usuario = presente.get();
            usuario.setRol(rol);
            usuarioRepository.save(usuario);
        }
    }

    @Transactional
    public void eliminarUsuario(String id) {
        Usuario usuario = new Usuario();

        Optional<Usuario> presente = usuarioRepository.findById(id);

        if (presente.isPresent()) {
            usuario = presente.get();
            usuarioRepository.delete(usuario);
        }
    }

    @Transactional
    public void darBaja(String id) {
        Usuario usuario = new Usuario();

        Optional<Usuario> presente = usuarioRepository.findById(id);

        if (presente.isPresent()) {
            usuario = presente.get();
            usuario.setActivo(FALSE);
            usuarioRepository.save(usuario);
        }
    }

    @Transactional
    public void darAlta(String id) {
        Usuario usuario = new Usuario();

        Optional<Usuario> presente = usuarioRepository.findById(id);

        if (presente.isPresent()) {
            usuario = presente.get();
            usuario.setActivo(TRUE);
            usuarioRepository.save(usuario);
        }
    }

    public Usuario getOne(String id) {
        return usuarioRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepository.findAll();

        return usuarios;
    }

    public void verificarEmail(String email) throws MiException {
        if (email.isEmpty()) {
            throw new MiException("El email no puede estar vacío");
        }
        if (usuarioRepository.buscarPorEmailOptional(email).isPresent()) {
            throw new MiException("El email ya está registrado");
        }

    }

    public boolean verificarPassword(String password, String password2) throws MiException {
        if (password.isEmpty()) {
            throw new MiException("La constraseña no puede estar vacía");
        }
        if (password.trim().isEmpty()) {
            throw new MiException("La contraseña no puede estar vacía");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas no coinciden");
        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);

        if (usuario == null) {
            throw new UsernameNotFoundException("ERROR: Usuario no encontrado");
        }

        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("ERROR: Usuario inactivo");
        }

        List<GrantedAuthority> permisos = new ArrayList<>();
        GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
        permisos.add(p);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("usuariosession", usuario);
        return new User(usuario.getEmail(), usuario.getPassword(), permisos);
    }

    @Transactional
    public void crearUsuario(String email, String password, Rol rol) throws MiException {
        Usuario presente = usuarioRepository.buscarPorEmail(email);
        if (presente == null) {
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setActivo(TRUE);
            usuario.setRol(rol);
            usuarioRepository.save(usuario);
        }
    }
}