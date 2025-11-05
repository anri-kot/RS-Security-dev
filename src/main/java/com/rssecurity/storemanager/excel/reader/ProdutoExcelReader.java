package com.rssecurity.storemanager.excel.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.rssecurity.storemanager.excel.mapper.ProdutoExcelMapper;
import com.rssecurity.storemanager.produto.dto.ProdutoDTO;

import jakarta.validation.Validator;

public class ProdutoExcelReader {

    private final Validator validator;

    public ProdutoExcelReader(Validator validator) {
        this.validator = validator;
    }

    public List<ProdutoDTO> readFromExcelSheet(Sheet sheet) {
        List<ProdutoDTO> produtos = new ArrayList<>();
        ProdutoExcelMapper mapper = ProdutoExcelMapper.fromHeaderRow(sheet.getRow(0));

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            ProdutoDTO produto = mapper.fromRow(row);

            if (produto == null) continue;
            
            ReaderValidator.validate(validator.validate(produto));
            produtos.add(produto);
        }

        return produtos;
    }
}
