package com.rssecurity.storemanager.excel.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.excel.mapper.ProdutoExcelMapper;

public class ProdutoExcelReader {

    public List<ProdutoDTO> readFromExcelSheet(InputStream inputStream) throws IOException {
        List<ProdutoDTO> produtos = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            ProdutoExcelMapper mapper = ProdutoExcelMapper.fromHeaderRow(sheet.getRow(0));

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                produtos.add(mapper.fromRow(row));
            }

            return produtos;
        } catch (IOException e) {
            throw new IOException("Erro ao tentar ler arquivo excel.");
        }
    }
}
