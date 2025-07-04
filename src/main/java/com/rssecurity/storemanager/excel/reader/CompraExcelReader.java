package com.rssecurity.storemanager.excel.reader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.dto.CompraKey;
import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.dto.ItemCompraDTO;
import com.rssecurity.storemanager.excel.mapper.CompraExcelMapper;

public class CompraExcelReader {
    
    public List<CompraDTO> readFromExcelSheet(Sheet sheet) {
        CompraExcelMapper mapper = CompraExcelMapper.fromHeaderRow(sheet.getRow(0));        
        Map<CompraKey, List<ItemCompraDTO>> grouped = new LinkedHashMap<>();
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            CompraExcelMapper.CompraParsedRow parsedKey = mapper.parseRow(row);
            grouped.computeIfAbsent(parsedKey.key(), k -> new ArrayList<>()).add(parsedKey.item());
        }

        return mountDTO(grouped);
    }

    private List<CompraDTO> mountDTO(Map<CompraKey, List<ItemCompraDTO>> grouped) {
        return grouped.entrySet().stream()
            .map(entry -> {
                CompraKey key = entry.getKey();
                FornecedorDTO fornecedor = new FornecedorDTO(key.idFornecedor(), key.nomeFornecedor(), null, null, null);
                return new CompraDTO(null, key.data(), key.observacao(), fornecedor, entry.getValue());
            })
            .toList();
    }
}
