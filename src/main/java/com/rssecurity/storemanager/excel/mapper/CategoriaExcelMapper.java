package com.rssecurity.storemanager.excel.mapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.excel.headers.CategoriaExcelHeader;
import com.rssecurity.storemanager.excel.reader.ExcelRowReader;
import com.rssecurity.storemanager.exception.BadRequestException;

public class CategoriaExcelMapper {
    private final Map<String, Integer> headerIndexMap = new HashMap<>();
    private final DataFormatter formatter = new DataFormatter();
    private ExcelRowReader excel = null;

    // Private constructor to force the use of fromHeaderRow for correct setup
    private CategoriaExcelMapper(){}

    /**
     * Factory method that initializes the mapper from the header row.
     * Ensures headers are correctly mapped before usage.
     */
    public static CategoriaExcelMapper fromHeaderRow(Row headerRow) {
        CategoriaExcelMapper mapper = new CategoriaExcelMapper();
        mapper.initializeHeader(headerRow);
        return mapper;
    }

    /**
     * Maps column headers (by name) to their index in the row.
     * This allows flexible column ordering in the Excel file.
     */
    private void initializeHeader(Row headerRow) {
        for (Cell cell : headerRow) {
            String value = formatter.formatCellValue(cell).trim().toLowerCase();
            headerIndexMap.put(value, cell.getColumnIndex());
        }
        if (!headerIndexMap.containsKey("nome")) {
            throw new BadRequestException(
                "Na planilha categorias está faltando o cabeçalho obrigatório: nome");
        }
    }

    /**
     * Converts a single Excel row into a CategoriaDTO object.
     * The header must have been initialized beforehand.
     */
    public CategoriaDTO fromRow(Row row) {
        if (headerIndexMap.isEmpty()) {
            throw new IllegalStateException("Cabeçalho não foi inicializado. Envie um cabeçado válido.");
        }

        Long idCategoria = excel.tryGetLong(row, CategoriaExcelHeader.ID.getHeaderName());
        String nome = excel.getCellStringValue(row, CategoriaExcelHeader.NOME.getHeaderName());

        return new CategoriaDTO(idCategoria, nome);
    }

}
