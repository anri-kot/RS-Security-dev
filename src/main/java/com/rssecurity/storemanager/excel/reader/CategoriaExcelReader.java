package com.rssecurity.storemanager.excel.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.rssecurity.storemanager.categoria.dto.CategoriaDTO;
import com.rssecurity.storemanager.excel.mapper.CategoriaExcelMapper;

import jakarta.validation.Validator;

public class CategoriaExcelReader {
    private final Validator validator;

    public CategoriaExcelReader(Validator validator) {
        this.validator = validator;
    }

    public List<CategoriaDTO> readFromExcelSheet(Sheet sheet) {
        List<CategoriaDTO> categorias = new ArrayList<>();
        CategoriaExcelMapper mapper = CategoriaExcelMapper.fromHeaderRow(sheet.getRow(0));

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            CategoriaDTO categoria = mapper.fromRow(row);
            if (categoria == null) continue;

            ReaderValidator.validate(validator.validate(categoria));

            categorias.add(categoria);
        }
        return categorias;
    }
}
