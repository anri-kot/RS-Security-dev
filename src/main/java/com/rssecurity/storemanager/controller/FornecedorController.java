package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.excel.headers.FornecedorExcelHeader;
import com.rssecurity.storemanager.excel.writter.FornecedorExcelWritter;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.FileDownloadService;
import com.rssecurity.storemanager.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedor")
public class FornecedorController {
    private final FornecedorService service;
    private final FileDownloadService downloadService;
    private final FornecedorExcelWritter excelWritter;

    public FornecedorController(FornecedorService service, FileDownloadService downloadService, FornecedorExcelWritter excelWritter) {
        this.service = service;
        this.downloadService = downloadService;
        this.excelWritter = excelWritter;
    }

    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> findAll() {
        List<FornecedorDTO> fornecedores = service.findAll();
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{idFornecedor}")
    public ResponseEntity<FornecedorDTO> findById(@PathVariable Long idFornecedor) {
        return ResponseEntity.ok(service.findById(idFornecedor));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FornecedorDTO>> findByNomeContains(@RequestParam(required = false) String nome) {
        return ResponseEntity.ok(service.findByNomeContains(nome));
    }

    @GetMapping("/search/cnpj")
    public ResponseEntity<List<FornecedorDTO>> findByCnpjContains(@RequestParam(required = false) String cnpj) {
        return ResponseEntity.ok(service.findByCnpjContains(cnpj));
    }

    @GetMapping("/search/fone")
    public ResponseEntity<List<FornecedorDTO>> findByTelefoneContains(@RequestParam(required = false) String telefone) {
        return ResponseEntity.ok(service.findByTelefoneContains(telefone));
    }

    @GetMapping("/search/email")
    public ResponseEntity<List<FornecedorDTO>> findByEmailContains(@RequestParam(required = false) String email) {
        return ResponseEntity.ok(service.findByEmailContains(email));
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> export() {
        List<FornecedorDTO> fornecedores = service.findAll();
        Resource resource = downloadService.workbookToResource(excelWritter.export(fornecedores));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fornecedores.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    // ACTIONS

    @PostMapping
    public ResponseEntity<FornecedorDTO> create(@RequestBody FornecedorDTO fornecedor) {
        FornecedorDTO created = service.create(fornecedor);
        URI location = URI.create("/api/fornecedor/" + created.idFornecedor());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idFornecedor}")
    public ResponseEntity<Void> update(@PathVariable Long idFornecedor, @RequestBody FornecedorDTO fornecedor) {
        if (!fornecedor.idFornecedor().equals(idFornecedor)) {
            throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL.");
        }
        service.update(idFornecedor, fornecedor);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idFornecedor}")
    public ResponseEntity<Void> deleteById(@PathVariable Long idFornecedor) {
        service.deleteById(idFornecedor);
        return ResponseEntity.noContent().build();
    }
}
