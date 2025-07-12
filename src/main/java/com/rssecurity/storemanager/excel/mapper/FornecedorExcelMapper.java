package com.rssecurity.storemanager.excel.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.excel.headers.FornecedorExcelHeader;
import com.rssecurity.storemanager.excel.reader.ExcelRowReader;
import com.rssecurity.storemanager.exception.BadRequestException;

public class FornecedorExcelMapper {
    private final Map<String, Integer> headerIndexMap = new HashMap<>();
    private final DataFormatter formatter = new DataFormatter();
    private ExcelRowReader excel = null;

    private final Set<FornecedorExcelHeader> REQUIRED_HEADERS = Set.of(
        FornecedorExcelHeader.NOME,
        FornecedorExcelHeader.CNPJ,
        FornecedorExcelHeader.TELEFONE,
        FornecedorExcelHeader.EMAIL
    );

    // Private constructor to force the use of fromHeaderRow for correct setup
    private FornecedorExcelMapper(){}

    /**
     * Factory method that initializes the mapper from the header row.
     * Ensures headers are correctly mapped before usage.
     */
    public static FornecedorExcelMapper fromHeaderRow(Row headerRow) {
        FornecedorExcelMapper mapper = new FornecedorExcelMapper();
        mapper.initializeHeader(headerRow);
        return mapper;
    }

    private void initializeHeader(Row headerRow) {
        for (Cell cell : headerRow) {
            String value = formatter.formatCellValue(cell).trim().toLowerCase();
            headerIndexMap.put(value, cell.getColumnIndex());
        }
        validateRequiredHeaders(REQUIRED_HEADERS);
        excel = new ExcelRowReader(headerIndexMap);
    }

    /**
     * Converts a single Excel row into a FornecedorDTO object.
     * The header must have been initialized beforehand.
     */
    public FornecedorDTO fromRow(Row row) {
        if (headerIndexMap.isEmpty()) {
            throw new IllegalStateException("Cabeçalho não foi inicializado. Envie um cabeçado válido.");
        }

        Long idFornecedor = excel.tryGetLong(row, FornecedorExcelHeader.ID.getHeaderName());
        String nome = excel.getCellStringValue(row, FornecedorExcelHeader.NOME.getHeaderName());
        String cnpj = excel.getCellStringValue(row, FornecedorExcelHeader.CNPJ.getHeaderName());
        String telefone = excel.getCellStringValue(row, FornecedorExcelHeader.TELEFONE.getHeaderName());
        String email = excel.getCellStringValue(row, FornecedorExcelHeader.EMAIL.getHeaderName());

        return new FornecedorDTO(idFornecedor, nome, cnpj, telefone, email);
    }

    private void validateRequiredHeaders(Set<FornecedorExcelHeader> requiredHeaders) {
        List<String> missing = requiredHeaders.stream()
            .map(FornecedorExcelHeader::getHeaderName)
            .filter(header -> !headerIndexMap.containsKey(header.toLowerCase()))
            .toList();

        if (!missing.isEmpty()) {
            throw new BadRequestException(
                "Na planilha Fornecedores está faltando os seguintes cabeçalhos obrigatórios: " + String.join(", ", missing)
            );
        }
    }    
}
