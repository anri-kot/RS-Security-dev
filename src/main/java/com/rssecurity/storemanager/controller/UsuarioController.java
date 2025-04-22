package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.UsuarioDTO;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    private UsuarioService service;

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

    @GetMapping("/by-username")
    public ResponseEntity<UsuarioDTO> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(service.findByUsername(username));
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
    public ResponseEntity update(@PathVariable Long idUsuario, @RequestBody UsuarioDTO usuario) {
        if (!usuario.idUsuario().equals(idUsuario)) {
            throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL.");
        }
        service.update(idUsuario, usuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity deleteById(@PathVariable Long idUsuario) {
        service.deleteById(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
