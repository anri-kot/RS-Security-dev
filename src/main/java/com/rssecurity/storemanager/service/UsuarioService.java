package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.UsuarioDTO;
import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.UsuarioMapper;
import com.rssecurity.storemanager.model.Usuario;
import com.rssecurity.storemanager.repository.UsuarioRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        return new org.springframework.security.core.userdetails.User(
            usuario.getUsername(),
            usuario.getSenha(),
            authorities);
    }

    public List<UsuarioDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }
    
    public UsuarioDTO findById(Long idUsuario) {
        Usuario usuario = repository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado. ID: " + idUsuario));
        return mapper.toDTO(usuario);
    }

    public UsuarioDTO findByUsername(String username) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado. Username: " + username));
        return mapper.toDTO(usuario);
    }

    public List<UsuarioDTO> findByUsernameContains(String username) {
        return repository.findByUsernameContains(username).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<UsuarioDTO> findByNomeContainingOrSobrenomeContaining(String nome) {
        return repository.findByNomeContainingOrSobrenomeContaining(nome, nome).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public UsuarioDTO create(UsuarioDTO usuario) {
        if (usuario.idUsuario() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }
        validateCreate(usuario);
        Usuario entity = setPassword(usuario);

        return mapper.toDTO(repository.save(entity));
    }

    public void update(Long id, UsuarioDTO usuario) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario não encontrado. ID: " + id);
        }
        Usuario toUpdate = setPassword(usuario);
        repository.save(toUpdate);
    }

    public void deleteById(Long idUsuario) {
        if (!repository.existsById(idUsuario)) {
            throw new ResourceNotFoundException("Usuario não encontrado. ID: " + idUsuario);
        }
        repository.deleteById(idUsuario);
    }

    private void validateCreate(UsuarioDTO usuario) {
        if (repository.existsByCpf(usuario.cpf())) throw new ConflictException("CPF já existe: " + usuario.cpf());
        if (repository.existsByEmail(usuario.email())) throw new ConflictException("email já existe: " + usuario.email());
        if (repository.existsByTelefone(usuario.telefone())) throw new ConflictException("telefone já existe: " + usuario.telefone());
        if (repository.existsByUsername(usuario.username())) throw new ConflictException("username já existe: " + usuario.username());
    }

    private Usuario setPassword(UsuarioDTO dto) {
        Usuario entity = mapper.toEntity(dto);
        String encodedPwd = passwordEncoder.encode(dto.senha());
        entity.setSenha(encodedPwd);
        return entity;
    }
}
