package com.rssecurity.storemanager.excel.writter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.excel.headers.ExcelHeader;
import com.rssecurity.storemanager.excel.headers.RelatorioLucroHeader;
import com.rssecurity.storemanager.relatorio.dto.LucroVendaDTO;
import com.rssecurity.storemanager.util.RecordUtils;

@Component
public class RelatorioLucroWritter extends ExcelWritter<LucroVendaDTO> {
    private final RelatorioLucroHeader[] HEADERS = RelatorioLucroHeader.values();

    @Override
    protected String getSheetName() {
        return "RELATORIO LUCRO";
    }

    @Override
    protected ExcelHeader[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected List<Map<String, Object>> mapData(List<LucroVendaDTO> relatorios) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (LucroVendaDTO relatorio : relatorios) {
            Map<String, Object> relatorioMap = RecordUtils.recordToMap(relatorio);
            Map<String, Object> row = new HashMap<>();

            for (RelatorioLucroHeader HEADER : HEADERS) {
                String key = HEADER.getKey();
                Object value = relatorioMap.get(key);

                row.put(key, value);
            }
            result.add(row);
        }
        return result;
    }

}
