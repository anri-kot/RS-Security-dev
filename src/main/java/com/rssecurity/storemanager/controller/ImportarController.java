package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.service.ExcelImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/importar")
public class ImportarController {
    private final ExcelImportService excelImportService;

    public ImportarController(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    @PostMapping
    public ResponseEntity<List<String>> importFromExcel(@RequestPart MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                excelImportService.importFromExcel(file.getInputStream()));
    }
}
