package com.rssecurity.storemanager.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.excel.ExcelModelo;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;

@Service
public class FileDownloadService {

    public Resource getModelo(String tipo) throws IOException {
        Path path = Paths.get("data/modelo-" + tipo + ".xlsx");

        if (!Files.exists(path)) {
            throw new ResourceNotFoundException("Modelo não encontrado. Tipo: " + tipo);
        }

        try {
            return new InputStreamResource(Files.newInputStream(path));
        } catch (IOException e) {
            throw new IOException("Erro ao ler arquivo '" + ExcelModelo.GERAL + "'");
        }
    }
}
