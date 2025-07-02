package com.rssecurity.storemanager.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExcelImportService {

    private final List<String> sheetOrder = List.of("categorias", "fornecedores", "produtos", "compras", "vendas");
    
    public void importFromExcel(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        List<String> previousSheetNames;
        
        for (String sheetName : sheetOrder) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                continue;
            }

            previousSheetNames.add(sheetName);

            switch (sheetName) {
                case "produtos" -> {}
                case "fornecedores" -> {}
                case "categorias" -> {}
                case "compras" -> {}
                case "vendas" -> {}
                default -> {}
            }
        }
    }

}