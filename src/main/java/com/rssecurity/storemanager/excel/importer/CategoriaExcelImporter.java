package com.rssecurity.storemanager.excel.importer;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.categoria.service.CategoriaService;
import com.rssecurity.storemanager.excel.reader.CategoriaExcelReader;

import jakarta.validation.Validator;

@Component
public class CategoriaExcelImporter implements ExcelSheetImporter {
    private final CategoriaService categoriaService;
    private final Validator validator;

    public CategoriaExcelImporter(CategoriaService categoriaService, Validator validator) {
        this.categoriaService = categoriaService;
        this.validator = validator;
    }

    @Override
    public String getSheetName() {
        return "categorias";
    }

    @Override
    public int importSheet(Sheet sheet) {
        CategoriaExcelReader reader = new CategoriaExcelReader(validator);
        return categoriaService.createAll(reader.readFromExcelSheet(sheet)).size();
    }
}
