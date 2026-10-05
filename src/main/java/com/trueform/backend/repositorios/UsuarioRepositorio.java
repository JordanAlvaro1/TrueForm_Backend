package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
}
