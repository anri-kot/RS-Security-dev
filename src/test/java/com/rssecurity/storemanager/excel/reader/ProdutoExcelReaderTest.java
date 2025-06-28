package com.rssecurity.storemanager.excel.reader;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoExcelReaderTest {

    @Test
    void shouldReadValidExcelFileAndReturnProdutos() throws Exception {
        // Arrange: creates xlsx in memory
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Produtos");
        
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("id");
        header.createCell(1).setCellValue("nome");
        header.createCell(2).setCellValue("preco atual");
        header.createCell(3).setCellValue("descricao");
        header.createCell(4).setCellValue("estoque");
        header.createCell(5).setCellValue("id categoria");

        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(1);
        row.createCell(1).setCellValue("Produto Teste");
        row.createCell(2).setCellValue(19.99);
        row.createCell(3).setCellValue("Descrição do produto");
        row.createCell(4).setCellValue(10);
        row.createCell(5).setCellValue(3);

        // Serializes to InputStream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());

        // Act
        ProdutoExcelReader reader = new ProdutoExcelReader();
        List<ProdutoDTO> produtos = reader.readFromExcelSheet(inputStream);

        // Assert
        assertEquals(1, produtos.size());
        ProdutoDTO produto = produtos.get(0);
        assertEquals("Produto Teste", produto.nome());
        assertEquals(new BigDecimal("19.99"), produto.precoAtual());
        assertEquals("Descrição do produto", produto.descricao());
        assertEquals(10, produto.estoque());
        assertEquals(3L, produto.categoria().idCategoria());
    }
}
