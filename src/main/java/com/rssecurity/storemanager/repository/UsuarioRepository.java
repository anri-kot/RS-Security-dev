package com.rssecurity.storemanager.repository;

import com.rssecurity.storemanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findByUsernameContains(String username);
    List<Usuario> findByNomeContainingOrSobrenomeContaining(String nome, String sobrenome);
    List<Usuario> findByNomeContainingOrSobrenomeContainingOrUsernameContaining(String nome, String sobrenome, String username);

    List<Usuario> findAllByUsernameIn(List<String> usernames);

    boolean existsByCpf(String cpf);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);
}
