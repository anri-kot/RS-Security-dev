package com.rssecurity.storemanager.excel.mapper;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.excel.headers.ProdutoExcelHeader;
import com.rssecurity.storemanager.excel.reader.ExcelRowReader;
import com.rssecurity.storemanager.exception.BadRequestException;

import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProdutoExcelMapper {

    private final Map<String, Integer> headerIndexMap = new HashMap<>();
    private final DataFormatter formatter = new DataFormatter();
    private ExcelRowReader excel = null;

    private final Set<ProdutoExcelHeader> REQUIRED_HEADERS = Set.of(
            ProdutoExcelHeader.NOME,
            ProdutoExcelHeader.PRECO_ATUAL,
            ProdutoExcelHeader.ESTOQUE);

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

        validateRequiredHeaders(REQUIRED_HEADERS);

        excel = new ExcelRowReader(headerIndexMap);
    }

    /**
     * Converts a single Excel row into a ProdutoDTO object.
     * The header must have been initialized beforehand.
     */
    public ProdutoDTO fromRow(Row row) {
        if (headerIndexMap.isEmpty()) {
            throw new IllegalStateException("Cabeçalho não foi inicializado. Envie um cabeçado válido.");
        }
        if (excel.isBlankRow(row)) return null;

        Long idProduto = excel.tryGetLong(row, ProdutoExcelHeader.ID.getHeaderName());
        String nome = excel.getCellStringValue(row, ProdutoExcelHeader.NOME.getHeaderName());
        String codigoBarras = excel.getCellStringValue(row, ProdutoExcelHeader.CODIGO_BARRAS.getHeaderName());
        String descricao = excel.getCellStringValue(row, ProdutoExcelHeader.DESCRICAO.getHeaderName());
        BigDecimal precoAtual = excel.tryGetBigDecimal(row, ProdutoExcelHeader.PRECO_ATUAL.getHeaderName());
        Integer estoque = excel.tryGetInteger(row, ProdutoExcelHeader.ESTOQUE.getHeaderName());
        Long idCategoria = excel.tryGetLong(row, ProdutoExcelHeader.ID_CATEGORIA.getHeaderName());
        String nomeCategoria = excel.getCellStringValue(row, ProdutoExcelHeader.NOME_CATEGORIA.getHeaderName());

        return new ProdutoDTO(idProduto, nome, codigoBarras, precoAtual, descricao, estoque, new CategoriaDTO(idCategoria, nomeCategoria));
    }

    private void validateRequiredHeaders(Set<ProdutoExcelHeader> requiredHeaders) {
        List<String> missing = requiredHeaders.stream()
            .map(ProdutoExcelHeader::getHeaderName)
            .filter(header -> !headerIndexMap.containsKey(header.toLowerCase()))
            .toList();

        if (!missing.isEmpty()) {
            throw new BadRequestException(
                "Na planilha produtos está faltando os seguintes cabeçalhos obrigatórios: " + String.join(", ", missing)
            );
        }
    }

    /* === UTIL === */

    // private String getCellStringValue(Row row, String headerName) {
    //     Integer index = headerIndexMap.get(headerName.toLowerCase());

    //     // getLastCellNum() == last cell index + 1
    //     if (index == null || index >= row.getLastCellNum()) {
    //         return "";
    //     }

    //     Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    //     return formatter.formatCellValue(cell).trim();
    // }

    // private Long tryGetLong(Row row, String headerName) {
    //     String value = getCellStringValue(row, headerName);

    //     if (value.isBlank()) {
    //         return null;
    //     }

    //     try {
    //         return Long.valueOf(value);
    //     } catch (NumberFormatException e) {
    //         throw new NumberFormatException(
    //                 String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
    //                         headerName, row.getRowNum() + 1, value));
    //     }
    // }

    // private BigDecimal tryGetBigDecimal(Row row, String headerName) {
    //     String value = getCellStringValue(row, headerName);

    //     if (value.isBlank()) {
    //         return new BigDecimal(0);
    //     }

    //     try {
    //         return new BigDecimal(value);
    //     } catch (NumberFormatException e) {
    //         throw new NumberFormatException(
    //                 String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
    //                         headerName, row.getRowNum() + 1, value));
    //     }
    // }

    // private Integer tryGetInteger(Row row, String headerName) {
    //     String value = getCellStringValue(row, headerName);

    //     try {
    //         return Integer.valueOf(value);
    //     } catch (NumberFormatException e) {
    //         throw new NumberFormatException(
    //                 String.format("Coluna '%s', linha %d: valor '%s' não é um número válido.",
    //                         headerName, row.getRowNum() + 1, value));
    //     }
    // }

}