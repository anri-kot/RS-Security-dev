package com.rssecurity.storemanager.excel.mapper;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.excel.headers.ProdutoExcelHeader;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProdutoExcelMapper {

    private final Map<String, Integer> headerIndexMap = new HashMap<>();
    private final DataFormatter formatter = new DataFormatter();

    // Private constructor to force the use of fromHeaderRow for correct setup
    private ProdutoExcelMapper() {
    }

    /**
     * Factory method that initializes the mapper from the header row.
     * Ensures headers are correctly mapped before usage.
     */
    public static ProdutoExcelMapper fromHeaderRow(Row headerRow) {
        ProdutoExcelMapper mapper = new ProdutoExcelMapper();
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
    }

    /**
     * Converts a single Excel row into a ProdutoDTO object.
     * The header must have been initialized beforehand.
     */
    public ProdutoDTO fromRow(Row row) {
        if (headerIndexMap.isEmpty()) {
            throw new IllegalStateException("Cabeçalho não foi inicializado. Envie um cabeçado válido.");
        }

        Long idProduto = tryGetLong(row, ProdutoExcelHeader.ID.getHeaderName());
        String nome = getCellStringValue(row, ProdutoExcelHeader.NOME.getHeaderName());
        String descricao = getCellStringValue(row, ProdutoExcelHeader.DESCRICAO.getHeaderName());
        BigDecimal precoAtual = tryGetBigDecimal(row, ProdutoExcelHeader.PRECO_ATUAL.getHeaderName());
        Integer estoque = tryGetInteger(row, ProdutoExcelHeader.ESTOQUE.getHeaderName());
        Long idCategoria = tryGetLong(row, ProdutoExcelHeader.ID_CATEGORIA.getHeaderName());
        String nomeCategoria = getCellStringValue(row, ProdutoExcelHeader.NOME_CATEGORIA.getHeaderName());

        return new ProdutoDTO(idProduto, nome, precoAtual, descricao, estoque, new CategoriaDTO(idCategoria, nomeCategoria));
    }

    /* === UTIL === */

    private String getCellStringValue(Row row, String headerName) {
        Integer index = headerIndexMap.get(headerName.toLowerCase());

        // getLastCellNum() == last cell index + 1
        if (index == null || index >= row.getLastCellNum()) {
            return "";
        }

        Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return formatter.formatCellValue(cell).trim();
    }

    private Long tryGetLong(Row row, String headerName) {
        String value = getCellStringValue(row, headerName);

        if (value.isBlank()) {
            return null;
        }

        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value));
        }
    }

    private BigDecimal tryGetBigDecimal(Row row, String headerName) {
        String value = getCellStringValue(row, headerName);

        if (value.isBlank()) {
            return new BigDecimal(0);
        }

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value));
        }
    }

    private Integer tryGetInteger(Row row, String headerName) {
        String value = getCellStringValue(row, headerName);

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value));
        }
    }

}