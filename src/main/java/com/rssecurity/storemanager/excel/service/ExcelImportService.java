package com.rssecurity.storemanager.excel.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.excel.importer.ExcelSheetImporter;
import com.rssecurity.storemanager.infra.exception.BadRequestException;

@Service
public class ExcelImportService {
    private final List<ExcelSheetImporter> importers;

    public ExcelImportService(List<ExcelSheetImporter> importers) {
        this.importers = importers;
    }

    public List<String> importFromExcel(InputStream inputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(inputStream);) {
            List<String> messages = new ArrayList<>();

            for (ExcelSheetImporter importer : importers) {
                String sheetName = importer.getSheetName();
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) continue;
                int inserted = importer.importSheet(sheet);

                messages.add(inserted + " " + sheetName + " inseridos");

                // switch (sheetName) {
                //     case "produtos" -> {
                //         ProdutoExcelReader reader = new ProdutoExcelReader(validator);
                //         int created = produtoService.createAll(reader.readFromExcelSheet(sheet)).size();
                //         successMessages.add(created + " produtos adicionados.");
                //     }
                //     case "fornecedores" -> {
                //         FornecedorExcelReader reader = new FornecedorExcelReader(validator);
                //         int created = fornecedorService.createAll(reader.readFromExcelSheet(sheet)).size();
                //         successMessages.add(created + " fornecedores adicionados.");
                //     }
                //     case "categorias" -> {
                //         CategoriaExcelReader reader = new CategoriaExcelReader();
                //         int created = categoriaService.createAll(reader.readFromExcelSheet(sheet)).size();
                //         successMessages.add(created + " categorias adicionados");
                //     }
                //     case "compras" -> {
                //         CompraExcelReader reader = new CompraExcelReader();
                //         int created = compraService.createAll(reader.readFromExcelSheet(sheet)).size();
                //         successMessages.add(created + " compras adicionadas");
                //     }
                //     case "vendas" -> {
                //         VendaExcelReader reader = new VendaExcelReader();
                //         List<VendaDTO> vendas = reader.readFromExcelSheet(sheet);
                //         int created = vendaService.createAll(vendas).size();
                //         successMessages.add(created + " vendas adicionadas.");
                //     }
                //     default -> {
                //         return List.of("Nome de página inválido. Nome: " + sheetName);
                //     }
                // }
            }

            return messages;

        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new BadRequestException("Não foi possível ler o arquivo.");
        } catch (POIXMLException e) {
            System.err.println(e.getMessage());
            throw new BadRequestException("Dados inválidos encontrados. Não foi possível processar arquivo.");
        } catch (IllegalArgumentException e) {      
            System.err.println(e.getMessage());      
            throw new BadRequestException("Problemas de formatação nos dados. Não foi possível processar arquivo. Converta todos os campos para texto para melhor compatibilidade");
        }
    }

}