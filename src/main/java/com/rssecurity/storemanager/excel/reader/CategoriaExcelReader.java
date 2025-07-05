package com.rssecurity.storemanager.excel.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.excel.mapper.CategoriaExcelMapper;

public class CategoriaExcelReader {

    public List<CategoriaDTO> readFromExcelSheet(Sheet sheet) {
        List<CategoriaDTO> categorias = new ArrayList<>();
        CategoriaExcelMapper mapper = CategoriaExcelMapper.fromHeaderRow(sheet.getRow(0));

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            categorias.add(mapper.fromRow(row));
        }
        return categorias;
    }
    
}
