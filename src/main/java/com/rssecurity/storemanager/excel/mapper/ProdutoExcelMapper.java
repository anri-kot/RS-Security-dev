package com.rssecurity.storemanager.excel.mapper;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.excel.headers.ProdutoExcelHeader;

import org.apache.coyote.BadRequestException;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
    public void initializeHeader(Row headerRow) {
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

        Long idProduto = null;
        String nome = null;
        String descricao = null;
        BigDecimal precoAtual = null;
        Integer estoque = null;
        CategoriaDTO categoria = null;

        idProduto = tryGetLong(row, ProdutoExcelHeader.ID.getHeaderName());
        nome = getCellValue(row, ProdutoExcelHeader.NOME.getHeaderName());
        descricao = getCellValue(row, ProdutoExcelHeader.DESCRICAO.getHeaderName());
        precoAtual = tryParseBigDecimal(row, ProdutoExcelHeader.PRECO_ATUAL.getHeaderName());
        estoque = tryParseInteger(row, ProdutoExcelHeader.ESTOQUE.getHeaderName());

        // TODO: CategoriaDTO not defined, Return ProdutoDTO object

        return null;
    }

    /* === UTIL === */

    private String getCellValue(Row row, String headerName) {
        Integer index = headerIndexMap.get(headerName.toLowerCase());

        // getLastCellNum() == last cell index + 1
        if (index == null || index >= row.getLastCellNum()) {
            return "";
        }

        Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return formatter.formatCellValue(cell).trim();
    }

    private Long tryGetLong(Row row, String headerName) {
        String value = getCellValue(row, headerName);

        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value));
        }
    }

    private BigDecimal tryParseBigDecimal(Row row, String headerName) {
        String value = getCellValue(row, headerName);

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value));
        }
    }

    private Integer tryParseInteger(Row row, String headerName) {
        String value = getCellValue(row, headerName);

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
                            headerName, row.getRowNum() + 1, value));
        }
    }

}