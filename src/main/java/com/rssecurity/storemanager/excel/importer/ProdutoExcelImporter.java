package com.rssecurity.storemanager.excel.importer;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.excel.reader.ProdutoExcelReader;
import com.rssecurity.storemanager.produto.service.ProdutoService;

import jakarta.validation.Validator;

@Component
public class ProdutoExcelImporter implements ExcelSheetImporter {
    private final Validator validator;
    private final ProdutoService service;

    public ProdutoExcelImporter(Validator validator, ProdutoService service) {
        this.validator = validator;
        this.service = service;
    }

    @Override
    public String getSheetName() {
        return "produtos";
    }

    @Override
    public int importSheet(Sheet sheet) {
        ProdutoExcelReader reader = new ProdutoExcelReader(validator);
        int created = service.createAll(reader.readFromExcelSheet(sheet)).size();
        return created;
    }

}
