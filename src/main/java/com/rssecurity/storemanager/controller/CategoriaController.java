package com.rssecurity.storemanager.controller;

import java.net.URI;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.excel.writter.CategoriaExcelWritter;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.CategoriaService;
import com.rssecurity.storemanager.service.FileDownloadService;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {
    private final CategoriaService service;
    private final FileDownloadService downloadService;
    private final CategoriaExcelWritter excelWritter;

    public CategoriaController(CategoriaService service, FileDownloadService downloadService, CategoriaExcelWritter excelWritter) {
        this.service = service;
        this.downloadService = downloadService;
        this.excelWritter = excelWritter;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{idCategoria}")
    public ResponseEntity<CategoriaDTO> findById(@PathVariable Long idCategoria) {
        return ResponseEntity.ok(service.findById(idCategoria));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoriaDTO>> findByNomeContains(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByNomeContains(nome));
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> export() {
        List<CategoriaDTO> categorias = service.findAll();
        Resource resource = downloadService.workbookToResource(excelWritter.export(categorias));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=categorias.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    // ACTIONS

    @PostMapping
    public ResponseEntity<CategoriaDTO> create(@RequestBody CategoriaDTO categoria) {
        CategoriaDTO created = service.create(categoria);
        URI location = URI.create("api/categoria/" + created.idCategoria());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idCategoria}")
    public ResponseEntity<Void> update(@PathVariable Long idCategoria, @RequestBody CategoriaDTO categoria) {
        if (!idCategoria.equals(categoria.idCategoria())) {
            throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL. IDs: " + idCategoria + " " + categoria.idCategoria());
        }
        service.update(idCategoria, categoria);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<Void> deleteById(@PathVariable Long idCategoria) {
        service.deleteById(idCategoria);
        return ResponseEntity.noContent().build();
    }
}
