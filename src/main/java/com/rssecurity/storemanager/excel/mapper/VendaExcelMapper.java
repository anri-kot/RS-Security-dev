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

import com.rssecurity.storemanager.dto.ItemVendaDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.dto.VendaKey;
import com.rssecurity.storemanager.excel.headers.VendaExcelHeader;
import com.rssecurity.storemanager.excel.reader.ExcelRowReader;
import com.rssecurity.storemanager.exception.BadRequestException;

public class VendaExcelMapper {
    private final Map<String, Integer> headerIndexMap = new HashMap<>();
    private final DataFormatter formatter = new DataFormatter();
    private ExcelRowReader excel = null;

    private final Set<VendaExcelHeader> REQUIRED_HEADERS = Set.of(
        VendaExcelHeader.DATA,
        VendaExcelHeader.USERNAME,
        VendaExcelHeader.METODO_PAGAMENTO,
        VendaExcelHeader.ID_PRODUTO,
        VendaExcelHeader.NOME_PRODUTO,
        VendaExcelHeader.QUANTIDADE,
        VendaExcelHeader.DESCONTO,
        VendaExcelHeader.VALOR_RECEBIDO,
        VendaExcelHeader.VALOR_UNITARIO
    );

    // Private constructor to force the use of fromHeaderRow for correct setup
    private VendaExcelMapper(){}

    /**
     * Factory method that initializes the mapper from the header row.
     * Ensures headers are correctly mapped before usage.
     */
    public static VendaExcelMapper fromHeaderRow(Row headerRow) {
        VendaExcelMapper mapper = new VendaExcelMapper();
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
    public VendaParsedRow parseRow(Row row) {
        if (excel.isBlankRow(row)) return null;

        LocalDateTime data = excel.tryGetLocalDateTime(row, VendaExcelHeader.DATA.getHeaderName());
        String obs = excel.getCellStringValue(row, VendaExcelHeader.OBSERVACAO.getHeaderName());
        String username = excel.getCellStringValue(row, VendaExcelHeader.USERNAME.getHeaderName());
        String metodoPagamento = excel.getCellStringValue(row, VendaExcelHeader.METODO_PAGAMENTO.getHeaderName());
        BigDecimal troco = excel.tryGetBigDecimal(row, VendaExcelHeader.TROCO.getHeaderName());
        BigDecimal valorRecebido = excel.tryGetBigDecimal(row, VendaExcelHeader.VALOR_RECEBIDO.getHeaderName());
        BigDecimal desconto = excel.tryGetBigDecimal(row, VendaExcelHeader.DESCONTO.getHeaderName());

        VendaKey key = new VendaKey(data, obs, username, metodoPagamento, troco, valorRecebido, desconto);

        Long idProduto = excel.tryGetLong(row, VendaExcelHeader.ID_PRODUTO.getHeaderName());
        String nomeProduto = excel.getCellStringValue(row, VendaExcelHeader.NOME_PRODUTO.getHeaderName());
        Integer qtd = excel.tryGetInteger(row, VendaExcelHeader.QUANTIDADE.getHeaderName());
        BigDecimal valorUnitario = excel.tryGetBigDecimal(row, VendaExcelHeader.VALOR_UNITARIO.getHeaderName());
        
        ItemVendaDTO item = new ItemVendaDTO(null, qtd, valorUnitario, desconto, new ProdutoDTO(idProduto, nomeProduto, null, null, null, null, null));

        return new VendaParsedRow(key, item);

    }

    private void validateRequiredHeaders(Set<VendaExcelHeader> requiredHeaders) {
        List<String> missing = requiredHeaders.stream()
            .map(VendaExcelHeader::getHeaderName)
            .filter(header -> !headerIndexMap.containsKey(header.toLowerCase()))
            .toList();

        if (!missing.isEmpty()) {
            throw new BadRequestException(
                "Na planilha Vendas está faltando os seguintes cabeçalhos obrigatórios: " + String.join(", ", missing)
            );
        }
    }

    public record VendaParsedRow(VendaKey key, ItemVendaDTO item) {}

}
