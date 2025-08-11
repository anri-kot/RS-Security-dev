package com.rssecurity.storemanager.excel.writter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.excel.headers.CompraExcelHeader;
import com.rssecurity.storemanager.excel.headers.ExcelHeader;
import com.rssecurity.storemanager.util.RecordUtils;

@Component
public class CompraExcelWritter extends ExcelWritter<CompraDTO> {
    private final CompraExcelHeader HEADERS[] = CompraExcelHeader.values();

    @Override
    protected String getSheetName() {
        return "compras";
    }

    @Override
    protected ExcelHeader[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected List<Map<String, Object>> mapData(List<CompraDTO> compras) {
        List<Map<String,Object>> result = new ArrayList<>();

        for (CompraDTO compra : compras) {
            Map<String,Object> compraMap = RecordUtils.recordToMap(compra);
            List<Map<String,Object>> itemsMap = (List<Map<String,Object>>) compraMap.get("itens");
            Map<String,Object> fornMap = (Map<String, Object>) compraMap.get("fornecedor");

            for (Map<String,Object> itemMap : itemsMap) {
                Map<String,Object> produtoMap = itemMap.get("produto") != null
                    ? (Map<String, Object>) itemMap.get("produto")
                    : new HashMap<>();

                Map<String, Object> row = new HashMap<>();
                for (CompraExcelHeader header : HEADERS) {
                    String key = header.getKey();
                    Object value;

                    if (key.contains("Produto")) {
                        value = produtoMap.getOrDefault(key, produtoMap
                            .getOrDefault(key.replace("Produto", ""), null));
                    } else if (key.contains("Fornecedor")) {
                        value = fornMap.getOrDefault(key, fornMap
                            .getOrDefault(key.replace("Fornecedor", ""), null));
                    } else {
                        value = compraMap.getOrDefault(key, itemMap
                            .getOrDefault(key, null));
                    }
                    
                    row.put(key, value);
                }
                result.add(row);
            }
        }
        return result;
    }
    
}
