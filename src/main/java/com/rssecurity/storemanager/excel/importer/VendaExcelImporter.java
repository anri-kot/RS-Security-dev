package com.rssecurity.storemanager.excel.importer;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.excel.reader.VendaExcelReader;
import com.rssecurity.storemanager.venda.dto.VendaDTO;
import com.rssecurity.storemanager.venda.service.VendaService;

import jakarta.validation.Validator;

@Component
public class VendaExcelImporter implements ExcelSheetImporter {
    private final Validator validator;
    private final VendaService service;

    public VendaExcelImporter(Validator validator, VendaService service) {
        this.validator = validator;
        this.service = service;
    }

    @Override
    public String getSheetName() {
        return "vendas";
    }

    @Override
    public int importSheet(Sheet sheet) {
        VendaExcelReader reader = new VendaExcelReader(validator);
        List<VendaDTO> vendas = reader.readFromExcelSheet(sheet);
        return service.createAll(vendas).size();
    }
}
