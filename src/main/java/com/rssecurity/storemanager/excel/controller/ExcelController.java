package com.rssecurity.storemanager.excel.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rssecurity.storemanager.excel.service.ExcelImportService;
import com.rssecurity.storemanager.excel.service.FileDownloadService;
import com.rssecurity.storemanager.infra.exception.BadRequestException;

@RestController
public class ExcelController {
    private final ExcelImportService excelImportService;
    private final FileDownloadService service;

    public ExcelController(ExcelImportService excelImportService, FileDownloadService service) {
        this.excelImportService = excelImportService;
        this.service = service;
    }

    @PostMapping("api/importar")
    public ResponseEntity<List<String>> importFromExcel(@RequestPart MultipartFile file) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                excelImportService.importFromExcel(file.getInputStream()));
        } catch (IOException e) {
            throw new BadRequestException("Arquivo XLSX não pôde ser lido.");
        }
    }

    @GetMapping("/download/modelo")
    public ResponseEntity<Resource> getModelo(@RequestParam String tipo) throws IOException {
        Resource file = service.downloadModelo(tipo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"modelo-" + tipo + ".xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);

    }
}
