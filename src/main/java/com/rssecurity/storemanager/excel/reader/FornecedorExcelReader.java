package com.rssecurity.storemanager.excel.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.rssecurity.storemanager.excel.mapper.FornecedorExcelMapper;
import com.rssecurity.storemanager.fornecedor.dto.FornecedorDTO;

public class FornecedorExcelReader {
    
    public List<FornecedorDTO> readFromExcelSheet(Sheet sheet) {
        List<FornecedorDTO> fornecedores = new ArrayList<>();
        FornecedorExcelMapper mapper = FornecedorExcelMapper.fromHeaderRow(sheet.getRow(0));

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            FornecedorDTO fornecedor = mapper.fromRow(row);
            if (fornecedor == null) continue;
            fornecedores.add(fornecedor);
        }
        return fornecedores;
    }
}
