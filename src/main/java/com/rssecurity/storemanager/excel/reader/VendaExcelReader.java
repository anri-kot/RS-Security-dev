package com.rssecurity.storemanager.excel.reader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.rssecurity.storemanager.dto.ItemVendaDTO;
import com.rssecurity.storemanager.dto.UsuarioResumoDTO;
import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.dto.VendaKey;
import com.rssecurity.storemanager.excel.mapper.VendaExcelMapper;

public class VendaExcelReader {
    
    public List<VendaDTO> readFromExcelSheet(Sheet sheet) {
        VendaExcelMapper mapper = VendaExcelMapper.fromHeaderRow(sheet.getRow(0));
        Map<VendaKey, List<ItemVendaDTO>> grouped = new LinkedHashMap<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            VendaExcelMapper.VendaParsedRow parsedKey = mapper.parseRow(row);
            if (parsedKey == null) continue;
            
            grouped.computeIfAbsent(parsedKey.key(), k -> new ArrayList<>()).add(parsedKey.item());
        }

        return mountDTO(grouped);
    }

    private List<VendaDTO> mountDTO(Map<VendaKey, List<ItemVendaDTO>> grouped) {
        return grouped.entrySet().stream()
                .map(entry -> {
                    VendaKey key = entry.getKey();
                    UsuarioResumoDTO usuario = new UsuarioResumoDTO(null, key.username(), null, null);
                    return new VendaDTO(null, key.data(), key.observacao(), usuario, entry.getValue(), key.metodoPagamento(), key.valorRecebido(), key.troco());
                })
                .toList();
    }
}
