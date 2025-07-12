package com.rssecurity.storemanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rssecurity.storemanager.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findByUsernameContains(String username);
    List<Usuario> findByNomeContainingOrSobrenomeContaining(String nome, String sobrenome);
    List<Usuario> findByNomeContainingOrSobrenomeContainingOrUsernameContaining(String nome, String sobrenome, String username);
    List<Usuario> findByCpfContaining(String cpf);
    List<Usuario> findByTelefoneContaining(String telefone);
    List<Usuario> findByEmailContaining(String email);
    List<Usuario> findByEnderecoContaining(String endereco);

    List<Usuario> findAllByUsernameIn(List<String> usernames);

    // Pages
    Page<Usuario> findByUsernameContains(String username, Pageable p);
    Page<Usuario> findByNomeContainingOrSobrenomeContaining(String nome, String sobrenome, Pageable p);
    Page<Usuario> findByCpfContaining(String cpf, Pageable p);
    Page<Usuario> findByTelefoneContaining(String telefone, Pageable p);
    Page<Usuario> findByEmailContaining(String email, Pageable p);
    Page<Usuario> findByEnderecoContaining(String endereco, Pageable p);

    boolean existsByCpf(String cpf);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);
}
