package com.rssecurity.storemanager.excel.writter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.excel.headers.ExcelHeader;
import com.rssecurity.storemanager.excel.headers.ProdutoExcelHeader;
import com.rssecurity.storemanager.util.RecordUtils;

@Component
public class ProdutoExcelWritter extends ExcelWritter<ProdutoDTO> {
    private final ProdutoExcelHeader[] HEADERS = ProdutoExcelHeader.values();

    @Override
    protected String getSheetName() {
        return "produtos";
    }

    @Override
    protected ExcelHeader[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected List<Map<String, Object>> mapData(List<ProdutoDTO> produtos) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (ProdutoDTO produto : produtos) {
            Map<String, Object> produtoMap = RecordUtils.recordToMap(produto);
            Map<String, Object> catMap = (Map<String, Object>) produtoMap.get("categoria");

            Map<String, Object> row = new HashMap<>();
            for (ProdutoExcelHeader header : HEADERS) {
                String key = header.getKey();
                Object value;

                if (key.contains("Categoria") && catMap != null) {
                    value = catMap.getOrDefault(key, catMap
                        .getOrDefault(key.replace("Categoria", ""), null));
                } else {
                    value = produtoMap.getOrDefault(key, null);
                }
                row.put(key, value);
            }
            result.add(row);
        }
        return result;
    }
    
}
