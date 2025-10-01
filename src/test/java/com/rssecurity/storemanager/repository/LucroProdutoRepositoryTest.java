package com.rssecurity.storemanager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.rssecurity.storemanager.model.LucroProduto;

@DataJpaTest
@ActiveProfiles("test")
class LucroProdutoRepositoryTest {

    @Autowired
    private LucroProdutoRepository repository;

    @Test
    void deveRetornarLucroProdutos() {
        // Act
        List<LucroProduto> lucros = repository.findAll();

        // Assert
        assertThat(lucros).isNotEmpty();

        // Validar produto específico (exemplo id_produto = 1)
        LucroProduto camisetas = lucros.stream()
                .filter(lp -> lp.getIdProduto() == 1L)
                .findFirst()
                .orElseThrow();

        assertThat(camisetas.getReceita()).isEqualByComparingTo(BigDecimal.valueOf(160.00));
        assertThat(camisetas.getCusto()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(camisetas.getLucro()).isEqualByComparingTo(BigDecimal.valueOf(60.00));
    }
}
