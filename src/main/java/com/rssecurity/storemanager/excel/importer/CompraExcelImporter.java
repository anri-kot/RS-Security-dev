package com.rssecurity.storemanager.excel.importer;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.compra.service.CompraService;
import com.rssecurity.storemanager.excel.reader.CompraExcelReader;

import jakarta.validation.Validator;

@Component
public class CompraExcelImporter implements ExcelSheetImporter {
    private final CompraService service;
    private final Validator validator;

    public CompraExcelImporter(CompraService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    @Override
    public String getSheetName() {
        return "compras";
    }

    @Override
    public int importSheet(Sheet sheet) {
        CompraExcelReader reader = new CompraExcelReader(validator);
        return service.createAll(reader.readFromExcelSheet(sheet)).size();
    }
}
