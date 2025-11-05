package com.rssecurity.storemanager.excel.importer;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.excel.reader.FornecedorExcelReader;
import com.rssecurity.storemanager.fornecedor.service.FornecedorService;

import jakarta.validation.Validator;

@Component
public class FornecedorExcelImporter implements ExcelSheetImporter {
    private final FornecedorService service;
    private final Validator validator;

    public FornecedorExcelImporter(FornecedorService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    @Override
    public String getSheetName() {
        return "fornecedores";
    }

    @Override
    public int importSheet(Sheet sheet) {
        FornecedorExcelReader reader = new FornecedorExcelReader(validator);
        int created = service.createAll(reader.readFromExcelSheet(sheet)).size();
        return created;
    }

}