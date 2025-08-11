package com.rssecurity.storemanager.excel.writter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.excel.headers.ExcelHeader;
import com.rssecurity.storemanager.excel.headers.VendaExcelHeader;
import com.rssecurity.storemanager.util.RecordUtils;

@Component
public class VendaExcelWritter extends ExcelWritter<VendaDTO> {
    private final VendaExcelHeader[] HEADERS = VendaExcelHeader.values();

    @Override
    protected String getSheetName() {
        return "vendas";
    }

    @Override
    protected ExcelHeader[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected List<Map<String, Object>> mapData(List<VendaDTO> vendas) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (VendaDTO venda : vendas) {
            Map<String, Object> vendaMap = RecordUtils.recordToMap(venda);
            List<Map<String, Object>> itemsMap = (List<Map<String, Object>>) vendaMap.get("itens");
            Map<String, Object> usuMap = (Map<String, Object>) vendaMap.get("usuario");

            for (Map<String, Object> itemMap : itemsMap) {
                Map<String, Object> produtoMap = itemMap.get("produto") != null
                        ? (Map<String, Object>) itemMap.get("produto")
                        : new HashMap<>();

                Map<String, Object> row = new HashMap<>();
                for (VendaExcelHeader header : HEADERS) {
                    String key = header.getKey();
                    
                    Object value;
                    System.out.println("TESTE ============");
                    System.out.println(key.toLowerCase().contains("produto"));
                    System.out.println(key);
                    if (key.toLowerCase().contains("produto")) {
                        value = produtoMap.getOrDefault(key, produtoMap
                            .getOrDefault(key.replace("Produto", ""), null));
                    } else {
                        value = vendaMap.getOrDefault(key, usuMap
                            .getOrDefault(key, itemMap
                                .getOrDefault(key, null)));
                    }
                    row.put(key, value);
                }
                result.add(row);
            }
        }
        return result;
    }

}