package com.rssecurity.storemanager.excel.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.rssecurity.storemanager.dto.CompraKey;
import com.rssecurity.storemanager.dto.ItemCompraDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.excel.headers.CompraExcelHeader;
import com.rssecurity.storemanager.excel.reader.ExcelRowReader;
import com.rssecurity.storemanager.exception.BadRequestException;

public class CompraExcelMapper {
    private final Map<String, Integer> headerIndexMap = new HashMap<>();
    private final DataFormatter formatter = new DataFormatter();
    private ExcelRowReader excel = null;

    private final Set<CompraExcelHeader> REQUIRED_HEADERS = Set.of(
        CompraExcelHeader.DATA,
        CompraExcelHeader.ID_PRODUTO,
        CompraExcelHeader.NOME_PRODUTO,
        CompraExcelHeader.QUANTIDADE,
        CompraExcelHeader.VALOR_UNITARIO
    );

    // Private constructor to force the use of fromHeaderRow for correct setup
    private CompraExcelMapper() {}

    /**
     * Factory method that initializes the mapper from the header row.
     * Ensures headers are correctly mapped before usage.
     */
    public static CompraExcelMapper fromHeaderRow(Row headerRow) {
        CompraExcelMapper mapper = new CompraExcelMapper();
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
     * Parses a single row from an Excel sheet representing a purchase and its item.
     * This method extracts both the high-level purchase information (data, observacao, fornecedor)
     * and the item-level information (produto, quantidade, valorUnitario) from the given row.
     * It returns a CompraParsedRow
     */
    public CompraParsedRow parseRow(Row row) {
        if (excel.isBlankRow(row)) return null;
        
        LocalDateTime data = excel.tryGetLocalDateTime(row, CompraExcelHeader.DATA.getHeaderName());
        String obs = excel.getCellStringValue(row, CompraExcelHeader.OBSERVACAO.getHeaderName());
        Long idFornecedor = excel.tryGetLong(row, CompraExcelHeader.ID_FORNECEDOR.getHeaderName());
        String nomeFornecedor = excel.getCellStringValue(row, CompraExcelHeader.NOME_FORNECEDOR.getHeaderName());

        CompraKey key = new CompraKey(data, obs, idFornecedor, nomeFornecedor);

        Long idProduto = excel.tryGetLong(row, CompraExcelHeader.ID_PRODUTO.getHeaderName());
        String nomeProduto = excel.getCellStringValue(row, CompraExcelHeader.NOME_PRODUTO.getHeaderName());
        Integer qtd = excel.tryGetInteger(row, CompraExcelHeader.QUANTIDADE.getHeaderName());
        BigDecimal valor = excel.tryGetBigDecimal(row, CompraExcelHeader.VALOR_UNITARIO.getHeaderName());

        ItemCompraDTO item = new ItemCompraDTO(null, qtd, valor, new ProdutoDTO(idProduto, nomeProduto, null, null, null, null, null));

        return new CompraParsedRow(key, item);
    }

    // public CompraDTO fromRow(Row row) {
    //     if (headerIndexMap.isEmpty()) {
    //         throw new IllegalStateException("Cabeçalho não foi inicializado. Envie um cabeçado válido.");
    //     }

    //     Long idCompra = excel.tryGetLong(row, CompraExcelHeader.ID.getHeaderName());
    //     LocalDateTime data = LocalDateTime.parse(excel.getCellStringValue(row, CompraExcelHeader.DATA.getHeaderName()));
    //     String observacao = excel.getCellStringValue(row, CompraExcelHeader.OBSERVACAO.getHeaderName());
    //     Long idFornecedor = excel.tryGetLong(row, CompraExcelHeader.ID_FORNECEDOR.getHeaderName());
    //     String nomeFornecedor = excel.getCellStringValue(row, CompraExcelHeader.NOME_FORNECEDOR.getHeaderName());
    //     Long idProduto = excel.tryGetLong(row, CompraExcelHeader.ID_PRODUTO.getHeaderName());
    //     String nomeProduto = excel.getCellStringValue(row, CompraExcelHeader.NOME_PRODUTO.getHeaderName());
    //     Integer quantidade = excel.tryGetInteger(row, CompraExcelHeader.QUANTIDADE.getHeaderName());
    //     BigDecimal valorUnitario = excel.tryGetBigDecimal(row, CompraExcelHeader.VALOR_UNITARIO.getHeaderName());

    //     FornecedorDTO fornecedor = new FornecedorDTO(idFornecedor, nomeFornecedor, null, null, null);
    //     ProdutoDTO produto = new ProdutoDTO(idProduto, nomeProduto, null, null, null, null);
    //     List<ItemCompraDTO> itens = List.of(new ItemCompraDTO(null, quantidade, valorUnitario, produto));

    //     return new CompraDTO(idCompra, null, observacao, fornecedor, itens);
    // }

    private void validateRequiredHeaders(Set<CompraExcelHeader> requiredHeaders) {
        List<String> missing = requiredHeaders.stream()
            .map(CompraExcelHeader::getHeaderName)
            .filter(header -> !headerIndexMap.containsKey(header.toLowerCase()))
            .toList();

        if (!missing.isEmpty()) {
            throw new BadRequestException(
                "Na planilha Compras está faltando os seguintes cabeçalhos obrigatórios: " + String.join(", ", missing)
            );
        }
    }

    public record CompraParsedRow(CompraKey key, ItemCompraDTO item) {}
}
