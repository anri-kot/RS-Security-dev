package com.rssecurity.storemanager.usuario.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rssecurity.storemanager.infra.exception.ConflictException;
import com.rssecurity.storemanager.usuario.dto.UsuarioDTO;
import com.rssecurity.storemanager.usuario.dto.UsuarioResumoDTO;
import com.rssecurity.storemanager.usuario.dto.UsuarioStatusDTO;
import com.rssecurity.storemanager.usuario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.findById(idUsuario));
    }

    @GetMapping("/username/")
    public ResponseEntity<UsuarioDTO> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(service.findByUsername(username));
    }

    @GetMapping("/short/username")
    public ResponseEntity<UsuarioResumoDTO> findByUsernameResumo(@RequestParam String username) {
        return ResponseEntity.ok(service.findByUsernameResumo(username));
    }

    @GetMapping("/search/username")
    public ResponseEntity<List<UsuarioDTO>> findByUsernameContains(@RequestParam String username) {
        return ResponseEntity.ok(service.findByUsernameContains(username));
    }

    @GetMapping("/search/nome")
    public ResponseEntity<List<UsuarioDTO>> findByNomeContainingOrSobrenomeContaining(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByNomeContainingOrSobrenomeContaining(nome));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@RequestBody UsuarioDTO usuario) {
        UsuarioDTO created = service.create(usuario);
        URI location = URI.create("/api/usuario/" + usuario.idUsuario());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<Void> update(@PathVariable Long idUsuario, @RequestBody UsuarioDTO usuario, @RequestParam(defaultValue = "false") boolean changePw) {
        if (!usuario.idUsuario().equals(idUsuario)) {
            throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL.");
        }

        if (!changePw) {
            service.updateWithoutPassword(idUsuario, usuario);
        } else {
            service.update(idUsuario, usuario);
        }

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idUsuario}")
    public ResponseEntity<Void> setUsuarioAtivo(@PathVariable Long idUsuario, @RequestBody @Valid UsuarioStatusDTO usuStatus) {
        service.updateAtivoByUsername(idUsuario, usuStatus.status());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> deleteById(@PathVariable Long idUsuario) {
        service.deleteById(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
