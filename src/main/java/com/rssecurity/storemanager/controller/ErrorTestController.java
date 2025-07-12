package com.rssecurity.storemanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/test-error")
public class ErrorTestController {
    
    /**
     * Simula erro 400 (Bad Request)
     */
    @GetMapping("/400")
    public void trigger400() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro 400 - Bad Request simulado");
    }

    /**
     * Simula erro 404 (Not Found)
     */
    @GetMapping("/404")
    public void trigger404() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro 404 - Recurso não encontrado");
    }

    /**
     * Simula erro 500 (Internal Server Error)
     */
    @GetMapping("/500")
    public void trigger500() {
        throw new RuntimeException("Erro 500 - Erro interno simulado");
    }

    /**
     * Simula erro genérico (ex: 403 - Forbidden)
     */
    @GetMapping("/403")
    public void trigger403() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Erro 403 - Acesso negado");
    }
}
