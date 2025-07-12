package com.rssecurity.storemanager.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.excel.reader.CategoriaExcelReader;
import com.rssecurity.storemanager.excel.reader.CompraExcelReader;
import com.rssecurity.storemanager.excel.reader.FornecedorExcelReader;
import com.rssecurity.storemanager.excel.reader.ProdutoExcelReader;
import com.rssecurity.storemanager.excel.reader.VendaExcelReader;
import com.rssecurity.storemanager.exception.BadRequestException;

@Service
public class ExcelImportService {
    private final ProdutoService produtoService;
    private final CompraService compraService;
    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;
    private final VendaService vendaService;
    private final List<String> sheetOrder = List.of("categorias", "fornecedores", "produtos", "compras", "vendas");

    public ExcelImportService(ProdutoService produtoService, CompraService compraService, CategoriaService categoriaService, FornecedorService fornecedorService, VendaService vendaService) {
        this.produtoService = produtoService;
        this.compraService = compraService;
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;
        this.vendaService = vendaService;
    }
    
    public List<String> importFromExcel(InputStream inputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(inputStream);) {

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
                    case "fornecedores" -> {
                        FornecedorExcelReader reader = new FornecedorExcelReader();
                        int created = fornecedorService.createAll(reader.readFromExcelSheet(sheet)).size();
                        successMessages.add(created + " fornecedores adicionados.");
                    }
                    case "categorias" -> {
                        CategoriaExcelReader reader = new CategoriaExcelReader();
                        int created = categoriaService.createAll(reader.readFromExcelSheet(sheet)).size();
                        successMessages.add(created + " categorias adicionados");
                    }
                    case "compras" -> {
                        CompraExcelReader reader = new CompraExcelReader();
                        int created = compraService.createAll(reader.readFromExcelSheet(sheet)).size();
                        successMessages.add(created + " compras adicionadas");
                    }
                    case "vendas" -> {
                        VendaExcelReader reader = new VendaExcelReader();
                        int created = vendaService.createAll(reader.readFromExcelSheet(sheet)).size();
                        successMessages.add(created + " vendas adicionadas.");
                    }
                    default -> {
                        return List.of("Nome de página inválido. Nome: " + sheetName);
                    }
                }
            }

            return successMessages;

        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new BadRequestException("Não foi possível ler o arquivo.");
        } catch (POIXMLException e) {
            System.err.println(e.getMessage());
            throw new BadRequestException("Dados inválidos encontrados. Não foi possível processar arquivo.");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            throw new BadRequestException("Problemas de formatação de entrada. Não foi possível processar arquivo.");
        }
    }

}