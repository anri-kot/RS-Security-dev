package com.rssecurity.storemanager.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.service.FileDownloadService;

@Controller
@RequestMapping("/download")
public class FileDownloadController {
    private final FileDownloadService service;

    public FileDownloadController(FileDownloadService service) {
        this.service = service;
    }

    @GetMapping("/modelo")
    public ResponseEntity<Resource> getModelo(@RequestParam String tipo) throws IOException {
        Resource file = service.downloadModelo(tipo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"modelo-" + tipo + ".xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);

    }
}
