package com.rssecurity.storemanager.excel.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.excel.mapper.ProdutoExcelMapper;

public class ProdutoExcelReader {

    public List<ProdutoDTO> readFromExcelSheet(Sheet sheet) {
        List<ProdutoDTO> produtos = new ArrayList<>();
        ProdutoExcelMapper mapper = ProdutoExcelMapper.fromHeaderRow(sheet.getRow(0));

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                continue;
            produtos.add(mapper.fromRow(row));
        }

        return produtos;
    }
}
