package com.nexus.projetokallopolli.repository;

import com.nexus.projetokallopolli.entity.Cargo;
import com.nexus.projetokallopolli.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByTelefone(String telefone);
    boolean existsByTelefone(String telefone);
    List<Usuario> findByCargo(Cargo cargo);
    List<Usuario> findByAtivo(Boolean ativo);
}
