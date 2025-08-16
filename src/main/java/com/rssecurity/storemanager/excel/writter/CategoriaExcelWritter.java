package com.rssecurity.storemanager.excel.writter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.excel.headers.CategoriaExcelHeader;
import com.rssecurity.storemanager.excel.headers.ExcelHeader;
import com.rssecurity.storemanager.util.RecordUtils;

@Component
public class CategoriaExcelWritter extends ExcelWritter<CategoriaDTO> {
    private final CategoriaExcelHeader HEADERS[] = CategoriaExcelHeader.values();

    @Override
    protected String getSheetName() {
        return "categorias";
    }

    @Override
    protected ExcelHeader[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected List<Map<String, Object>> mapData(List<CategoriaDTO> categorias) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (CategoriaDTO categoria : categorias) {
            Map<String, Object> catMap = RecordUtils.recordToMap(categoria);

            Map<String, Object> row = new HashMap<>();
            for (CategoriaExcelHeader header : HEADERS) {
                String key = header.getKey();
                row.put(
                    key, 
                    catMap.getOrDefault(key, null));
            }
            result.add(row);
        }
        return result;
    }
    
}
