package com.rssecurity.storemanager.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.excel.reader.CompraExcelReader;
import com.rssecurity.storemanager.excel.reader.ProdutoExcelReader;

@Service
public class ExcelImportService {
    private final ProdutoService produtoService;
    private final CompraService compraService;
    private final List<String> sheetOrder = List.of("categorias", "fornecedores", "produtos", "compras", "vendas");

    public ExcelImportService(ProdutoService produtoService, CompraService compraService) {
        this.produtoService = produtoService;
        this.compraService = compraService;
    }
    
    public List<String> importFromExcel(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        List<String> successMessages = new ArrayList<>();

        for (String sheetName : sheetOrder) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                continue;
            }

            switch (sheetName) {
                case "produtos" -> {
                    ProdutoExcelReader reader = new ProdutoExcelReader();
                    int created = produtoService.createAll(reader.readFromExcelSheet(sheet)).size();
                    successMessages.add(created + " produtos adicionados.");
                }
                case "fornecedores" -> {}
                case "categorias" -> {}
                case "compras" -> {
                    CompraExcelReader reader = new CompraExcelReader();
                    int created = compraService.createAll(reader.readFromExcelSheet(sheet)).size();
                    successMessages.add(created + " compras adicionadas");
                }
                case "vendas" -> {}
                default -> {}
            }
        }
        return successMessages;
    }

}