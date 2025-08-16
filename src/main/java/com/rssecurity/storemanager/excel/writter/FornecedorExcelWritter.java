package com.rssecurity.storemanager.excel.writter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.excel.headers.ExcelHeader;
import com.rssecurity.storemanager.excel.headers.FornecedorExcelHeader;
import com.rssecurity.storemanager.util.RecordUtils;

@Component
public class FornecedorExcelWritter extends ExcelWritter<FornecedorDTO> {
    private final FornecedorExcelHeader HEADERS[] = FornecedorExcelHeader.values();

    @Override
    protected String getSheetName() {
        return "fornecedores";
    }

    @Override
    protected ExcelHeader[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected List<Map<String, Object>> mapData(List<FornecedorDTO> fornecedores) {
        List<Map<String,Object>> result = new ArrayList<>();

        for (FornecedorDTO fornecedor : fornecedores) {
            Map<String, Object> fornMap = RecordUtils.recordToMap(fornecedor);

            Map<String, Object> row = new HashMap<>();
            for (FornecedorExcelHeader header : HEADERS) {
                String key = header.getKey();
                row.put(
                    key, 
                    fornMap.getOrDefault(key, null));
            }
            result.add(row);
        }
        return result;
    }
    
}
