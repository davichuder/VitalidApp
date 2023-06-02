package com.example.healthserviceapp.repository;

import org.springframework.stereotype.Repository;

import com.example.healthserviceapp.entity.Usuario;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<Usuario> buscarPorEmailOptional(@Param("email") String email);
    
    @Query("SELECT u FROM Usuario u")
    public List<Usuario> buscarUsuarios();
}
